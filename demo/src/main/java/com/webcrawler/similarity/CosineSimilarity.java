package com.webcrawler.similarity;

import java.util.Map;

public class CosineSimilarity {
    public double calculate(Map<String, Double> vector1, Map<String, Double> vector2) {
        // Calculate dot product
        double dotProduct = 0.0;
        for (String term : vector1.keySet()) {
            if (vector2.containsKey(term)) {
                dotProduct += vector1.get(term) * vector2.get(term);
            }
        }
        
        // Calculate magnitudes
        double magnitude1 = calculateMagnitude(vector1);
        double magnitude2 = calculateMagnitude(vector2);
        
        // Avoid division by zero
        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0.0;
        }
        
        return dotProduct / (magnitude1 * magnitude2);
    }
    
    private double calculateMagnitude(Map<String, Double> vector) {
        double sum = 0.0;
        for (double value : vector.values()) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }
}