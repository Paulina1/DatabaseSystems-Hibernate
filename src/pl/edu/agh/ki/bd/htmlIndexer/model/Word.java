package pl.edu.agh.ki.bd.htmlIndexer.model;

import java.util.HashSet;
import java.util.Set;

public class Word {
    private String content;
    private Set<Sentence> sentences = new HashSet<Sentence>(
            0);

    public Word() {}

    public Word(String content) { this.content = content; }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(Set<Sentence> sentences) {
        this.sentences = sentences;
    }

    public void addSentence(Sentence sentence) { this.sentences.add(sentence); }
}
