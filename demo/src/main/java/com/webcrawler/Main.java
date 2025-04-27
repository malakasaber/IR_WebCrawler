package com.webcrawler;

import com.webcrawler.crawler.WebCrawler;
import com.webcrawler.index.IndexDocument;
import com.webcrawler.index.InvertedIndex;
import com.webcrawler.similarity.QueryProcessor;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();

        String[] seeds = {
                "https://en.wikipedia.org/wiki/List_of_pharaohs",
                "https://en.wikipedia.org/wiki/Pharaoh"
        };

        // Step 1: Start crawling and retrieve documents
        List<org.jsoup.nodes.Document> documents = crawler.start_crawling(seeds);
        List<IndexDocument> documents2 = WebCrawler.tokenization(crawler.getPageTexts());

        // Step 2: Build the inverted index (add all documents to it)
        InvertedIndex index = new InvertedIndex();
        for (IndexDocument doc : documents2) {
            index.addDocument(doc);
        }
        // Step 3: Create a query processor
        QueryProcessor processor = new QueryProcessor(index);

        // Step 4: Read user query
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your query: ");
        String userQuery = scanner.nextLine();

        // Step 5: Process query and get ranked results
        Map<IndexDocument , Double> topResults = processor.processQuery(userQuery);

        // Step 6: Display top documents
        System.out.println("\nTop matching documents:");
        if (topResults.isEmpty()) {
            System.out.println("No results found.");
        } else {
            int rank = 1;
            topResults.forEach((indexDocument, score) -> {
                System.out.println("Document: " + indexDocument.getUrl() + ", Score: " + score);
            });

        }
    }
}