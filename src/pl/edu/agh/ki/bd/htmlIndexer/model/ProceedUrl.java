package pl.edu.agh.ki.bd.htmlIndexer.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProceedUrl {

	private Integer id;
//	private List<Sentence> sentences;
	private Set<Sentence> sentences = new HashSet<Sentence>(
			0);
	private String date;
	private String url;
	
	public ProceedUrl() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(Set<Sentence> sentences) {
		this.sentences = sentences;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
