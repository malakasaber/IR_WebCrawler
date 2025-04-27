package com.webcrawler.index;

import com.webcrawler.crawler.WebCrawler;

import java.util.*;

public class InvertedIndex {
    private final Map<String, Posting> index = new HashMap<>();
    private final Map<Integer, IndexDocument> documentMap = new HashMap<>();
    private static int docIdCounter = 0;
    private static final Set<String> STOP_WORDS = Set.of(
            "the", "to", "be", "for", "from", "in",
            "a", "into", "by", "or", "and", "that"
    );
    public void addDocument(IndexDocument document) {
        int docId = docIdCounter++;
        documentMap.put(docId, document);

        Map<String, Integer> termFrequencies = new HashMap<>();
        for (String token : document.getTokens()) {
            String stemmed = stemWord(token);
            if (!isStopWord(stemmed)) {
                termFrequencies.put(stemmed, termFrequencies.getOrDefault(stemmed, 0) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : termFrequencies.entrySet()) {
            String term = entry.getKey();
            int frequency = entry.getValue();

            Posting posting = new Posting(docId, frequency);
            posting.next = index.get(term);
            index.put(term, posting);
        }

        document.setTermFrequencies(termFrequencies);
    }

    public List<IndexDocument> getDocuments(String term) {
        List<IndexDocument> documents = new ArrayList<>();
        Posting current = index.get(term);

        while (current != null) {
            IndexDocument document = documentMap.get(current.docId);
            if (document != null) {
                documents.add(document);
            }
            current = current.next;
        }

        return documents;
    }

    public int getDocumentFrequency(String term) {
        List<String> tokenized = WebCrawler.Tokenize(term);
        if (tokenized.isEmpty()) return 0;

        int count = 0;
        Posting current = index.get(tokenized.get(0));
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public int getTotalDocuments() {
        return documentMap.size();
    }

    public List<IndexDocument> getAllDocuments() {
        return new ArrayList<>(documentMap.values());
    }

    private static boolean isStopWord(String word) {
        return word.length() < 2 || STOP_WORDS.contains(word);
    }

    public static String stemWord(String word) {
        Stemmer stemmer = new Stemmer();
        stemmer.addString(word);
        stemmer.stem();
        return stemmer.toString();
    }


}
