package pl.edu.agh.ki.bd.htmlIndexer.model;




public class Sentence {
	
	private long id;
	private String content;
	private String URL;
	private ProceedUrl proceedUrl;
	
	public Sentence() 
	{
		
	}
	
	public Sentence(String content, String URL)
	{
		this.setContent(content);
		this.setURL(URL);
	}

	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}

	public ProceedUrl getProceedUrl() {
		return proceedUrl;
	}

	public void setProceedUrl(ProceedUrl proceedUrl) {
		this.proceedUrl = proceedUrl;
	}
}
