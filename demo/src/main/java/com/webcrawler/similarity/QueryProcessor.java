package com.webcrawler.similarity;

import com.webcrawler.index.InvertedIndex;
import com.webcrawler.index.IndexDocument;
import com.webcrawler.crawler.WebCrawler;

import java.util.*;
import java.util.stream.Collectors;

public class QueryProcessor {
    private final InvertedIndex index;
    private final TFIDFCalculator tfidfCalculator;
    private final CosineSimilarity cosineSimilarity;

    public QueryProcessor(InvertedIndex index) {
        this.index = index;
        this.tfidfCalculator = new TFIDFCalculator(index);
        this.cosineSimilarity = new CosineSimilarity();
    }

    public Map<IndexDocument, Double> processQuery(String query) {
        // Tokenize
        List<String> queryTerms = WebCrawler.Tokenize(query);

        // Convert to array for TF-IDF calculation
        String[] termsArray = queryTerms.toArray(new String[0]);
        Map<String, Double> queryVector = tfidfCalculator.calculateQueryVector(termsArray);

        // For document scores
        Map<IndexDocument, Double> documentScores = new HashMap<>();

        // For every doc in crawled docs, calculate cosine similarity and store
        for (IndexDocument doc : index.getAllDocuments()) {
            Map<String, Double> docVector = tfidfCalculator.calculateDocumentVector(doc);
            double score = cosineSimilarity.calculate(queryVector, docVector);
            documentScores.put(doc, score);
        }

        // Sort by score descending
        return documentScores.entrySet()
                .stream()
                .sorted(Map.Entry.<IndexDocument, Double>comparingByValue(Comparator.reverseOrder()))
                .collect(
                        LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        LinkedHashMap::putAll
                );
    }

}