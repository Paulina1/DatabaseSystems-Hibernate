package pl.edu.agh.ki.bd.htmlIndexer;

import java.io.IOException;
import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.edu.agh.ki.bd.htmlIndexer.model.ProceedUrl;
import pl.edu.agh.ki.bd.htmlIndexer.model.Sentence;
import pl.edu.agh.ki.bd.htmlIndexer.model.Word;
import pl.edu.agh.ki.bd.htmlIndexer.persistence.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class Index 
{		
	public void indexWebPage(String url) throws IOException
	{
		
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.body().select("*");
		
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		ProceedUrl proceedUrl = new ProceedUrl(url);

		for (Element element : elements) 
		{
			if (element.ownText().trim().length() > 1)
			{
				for (String sentenceContent : element.ownText().split("\\. "))
				{
					Set<Word> words = new HashSet<Word>(0);
					Sentence sentence = new Sentence(proceedUrl, words);

					for (String splitted : sentenceContent.split(" ")) {

						System.out.println("------------");
						System.out.println(splitted);
//						List<Word> result = session.createQuery("select w from Word w where w.content = :sentence").setParameter("sentence", splitted).list();

						Criteria cr = session.createCriteria(Word.class);
						cr.add(Restrictions.eq("content", splitted));
						List<Word> result = cr.list();

						Word word = null;
						if (result.size() != 0) {
							word = result.get(0);
							System.out.println("get");
						} else {
							word = new Word(splitted);
							System.out.println("new");
						}

						words.add(word);
						word.addSentence(sentence);
					}

					session.persist(sentence);
				}
			}
		}	
		
		transaction.commit();
		session.close();
		
	}	
	
	public List<Object[]> findSentencesByWords(String words)
	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();

		String[] wordsList = words.split(" ");
		List<Object[]> result = session.createQuery("select s, count(*) as c from Sentence s join s.words w join fetch s.proceedUrl pu where w.content in (:word_list) " +
				"group by s, pu order by count(*) DESC").setParameterList("word_list", wordsList).list();
		for (Object[] objects : result)	{
			((Sentence)objects[0]).getSentence();
		}
		transaction.commit();
		session.close();
		
		return result;
	}
	
	public List<String> findLongerSentences(long len)
	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		
		//String query = "%" + words.replace(" ", "%") + "%";
		List<Sentence> result = session.createQuery("select s from Sentence s join s.words w group by s having sum(length(w.content)) > :len").setParameter("len", len).list();
		List<String> strings = new ArrayList<String>();
		for (Sentence sentence : result)	{
			strings.add(sentence.getSentence());
		}

		transaction.commit();
		session.close();
		
		return strings;
	}

	public List<Object[]>  makeOverall()	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		List<Object[]> results = session.createQuery("select p.url, count(s.id) from ProceedUrl p join p.sentences s group by p order by count(s.id)").list();

		transaction.commit();
		session.close();

		return results;
	}

	public List<Object[]> findAppearance(String word)
	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();

		List<Object[]> result = session.createQuery("select count(*), pu.url from Word w join w.sentences s join s.proceedUrl pu where w.content = :word " +
				"group by pu").setParameter("word", word).list();

		transaction.commit();
		session.close();

		return result;
	}

	public List<String> compareTwoUrl(String a, String b)	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();

		List<Word> result1 = getUrlresult(session, a);
		List<Word> result2 = getUrlresult(session, b);

		transaction.commit();
		session.close();

		List<String> list = new ArrayList<String>();

		for (Word word : result1) {
			if (result2.contains(word)) {
				list.add(word.getContent());
			}
		}

		return list;
	}

	private List getUrlresult(Session session, String page) {
		return session.createQuery("select w from ProceedUrl pu join pu.sentences s join s.words w where pu.url = :url").setParameter("url", page).list();
	}
	
}
