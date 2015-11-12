package pl.edu.agh.ki.bd.htmlIndexer;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.edu.agh.ki.bd.htmlIndexer.model.Sentence;
import pl.edu.agh.ki.bd.htmlIndexer.persistence.HibernateUtils;

public class Index 
{		
	public void indexWebPage(String url) throws IOException
	{
		
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.body().select("*");
		
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		
		for (Element element : elements) 
		{
			if (element.ownText().trim().length() > 1)
			{
				for (String sentenceContent : element.ownText().split("\\. "))
				{
					

					Sentence sentence = new Sentence(sentenceContent, url);
					session.persist(sentence);

				}
			}
		}	
		
		transaction.commit();
		session.close();
		
	}	
	
	public List<Sentence> findSentencesByWords(String words)
	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		
		String query = "%" + words.replace(" ", "%") + "%";
		List<Sentence> result = session.createQuery("select s from Sentence s where s.content like :query").setParameter("query", query).list();
		
		transaction.commit();
		session.close();
		
		return result;
	}
	
	public List<String> findLongerSentences(int len)
	{
		Session session = HibernateUtils.getSession();
		Transaction transaction = session.beginTransaction();
		
		//String query = "%" + words.replace(" ", "%") + "%";
		List<String> result = session.createQuery("select s.content from Sentence s where length(s.content) > :len").setParameter("len", len).list();
		
		transaction.commit();
		session.close();
		
		return result;
	}
	
	
}
