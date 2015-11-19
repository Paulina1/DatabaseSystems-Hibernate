package pl.edu.agh.ki.bd.htmlIndexer.model;


import java.util.HashSet;
import java.util.Set;

public class Sentence {
	
	private long id;
	private ProceedUrl proceedUrl;
	private Set<Word> words;

	public Sentence(ProceedUrl proceedUrl, Set<Word> words)
	{
		this.proceedUrl = proceedUrl;
		this.words = words;
	}
	
	public Sentence(ProceedUrl proceedUrl)
	{
		this.proceedUrl = proceedUrl;
		this.words = new HashSet<Word>(0);
	}

	public Sentence() {
		this.words = new HashSet<Word>(0);
	}

	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public ProceedUrl getProceedUrl() {
		return proceedUrl;
	}

	public void setProceedUrl(ProceedUrl proceedUrl) {
		this.proceedUrl = proceedUrl;
	}

	public Set<Word> getWords() {
		return words;
	}

	public void setWords(Set<Word> words) {
		this.words = words;
	}

	public String getSentence()	{
		String sentence = "";
		for (Word word : this.words)	{
			sentence += word.getContent() + ' ';
		}
		return sentence;
	}
}
