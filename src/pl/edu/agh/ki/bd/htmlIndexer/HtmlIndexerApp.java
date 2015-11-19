package pl.edu.agh.ki.bd.htmlIndexer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import pl.edu.agh.ki.bd.htmlIndexer.model.Sentence;
import pl.edu.agh.ki.bd.htmlIndexer.persistence.HibernateUtils;

public class HtmlIndexerApp 
{

	public static void main(String[] args) throws IOException
	{
		HibernateUtils.getSession().close();

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		Index indexer = new Index(); 
		
		while (true)
		{
			System.out.println("\nHtmlIndexer [? for help] > : ");
			String command = bufferedReader.readLine();
	        long startAt = new Date().getTime();

			if (command.startsWith("?"))
			{
				System.out.println("'?'      	- print this help");
				System.out.println("'x'      	- exit HtmlIndexer");
				System.out.println("'i URLs'  	- index URLs, space separated");
				System.out.println("'f WORDS'	- find sentences containing all WORDs, space separated");
				System.out.println("'l WORDS'	- find longer sentences");
				System.out.println("'c WORD'	- count appearance of WORD");
				System.out.println("'d'	- make overall");
				System.out.println("'k URL URL'	- compare words");
			}
			else if (command.startsWith("x"))
			{
				System.out.println("HtmlIndexer terminated.");
				HibernateUtils.shutdown();
				break;				
			}
			else if (command.startsWith("i "))
			{
				for (String url : command.substring(2).split(" "))
				{
					try {
						indexer.indexWebPage(url);
						System.out.println("Indexed: " + url);
					} catch (Exception e) {
						System.out.println("Error indexing: " + e.getMessage());
						e.printStackTrace();
					}
				}
			}
			else if (command.startsWith("f "))
			{
				for (Object[] sentence : indexer.findSentencesByWords(command.substring(2)))
				{
					System.out.println("Found in sentence: " + ((Sentence) sentence[0]).getSentence() +
							"url: " + ((Sentence) sentence[0]).getProceedUrl().getUrl() + " appearance: " + sentence[1]);
				}
			}
			
			else if (command.startsWith("l "))
			{
				for (String sentence : indexer.findLongerSentences(Integer.parseInt(command.substring(2))))
				{
					System.out.println("Found in sentence: " + sentence);
				}
			}

			else if (command.startsWith("d"))
			{
				for (Object[] row : indexer.makeOverall())
				{
					String url = (String) row[0];
					System.out.println(url + ": " + row[1]);
				}
			}
			else if (command.startsWith("c "))
			{
				for (Object[] row : indexer.findAppearance(command.substring(2)))
				{
					System.out.println("Number in: " + row[1] + " = " + row[0]);
				}

			}
			else if (command.startsWith("k "))
			{
				System.out.println("Same words:");
				String [] pages = command.substring(2).split(" ");

				List<String> res = indexer.compareTwoUrl(pages[0], pages[1]);

				for (String row : res)
				{
					System.out.println(row);
				}

				System.out.println("Number of same: " + res.size());

			}
			
			System.out.println("took "+ (new Date().getTime() - startAt)+ " ms");		

		}

	}

}
