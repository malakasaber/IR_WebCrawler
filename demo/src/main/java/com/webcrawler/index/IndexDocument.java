package com.webcrawler.index;

import java.util.List;
import java.util.Map;

public class IndexDocument {
    private final String url;
    private final List<String> tokens;
    private Map<String, Integer> termFrequencies;
    public IndexDocument() {
        this.url = "";
        this.tokens = null;
    }
    public IndexDocument(String url, List<String> tokens) {
        this.url = url;
        this.tokens = tokens;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTermFrequencies(Map<String, Integer> freqs) {
        this.termFrequencies = freqs;
    }

    public Map<String, Integer> getTermFrequencies() {
        return termFrequencies;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return url;
    }

}
