package com.webcrawler.crawler;

import com.webcrawler.index.IndexDocument;
import com.webcrawler.index.InvertedIndex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class WebCrawler {

    private Set<String> visited = new HashSet<>();
    private Queue<String> queue_waiting = new LinkedList<>();
    private Map<String, String> page_texts = new HashMap<>();
    private static final int max_pages = 10;

    // Function crawling
    public List<Document> start_crawling(String[] seeds) {

        // Add URLs to queue
        queue_waiting.addAll(Arrays.asList(seeds));

        while (!queue_waiting.isEmpty() && visited.size() < max_pages) {
            // Get next URL from queue
            String current_url = normalizeUrl(queue_waiting.poll());

            // To avoid duplicates & ensure it's a Wikipedia link
            if (current_url == null || visited.contains(current_url)) continue;
            if (!current_url.startsWith("https://en.wikipedia.org/wiki/")) continue;

            try {
                // Fetch the HTML document
                Document document = Jsoup.connect(current_url).get();
                visited.add(current_url);

                // Save text content for tokenization
                page_texts.put(current_url, document.body().text());

                System.out.println("✅ VISITED: " + current_url);

                // Get all links on the current page
                Elements links_page = document.select("a[href]");
                for (Element link : links_page) {
                    String Url = normalizeUrl(link.attr("abs:href"));

                    if (Url != null && !visited.contains(Url) && Url.startsWith("https://en.wikipedia.org/wiki/")) {
                        queue_waiting.add(Url);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Convert map to List of documents to return in main
        List<Document> documents = new ArrayList<>();
        for (Map.Entry<String, String> entry : page_texts.entrySet()) {
            documents.add(new Document(entry.getKey(), entry.getValue()));
        }

        System.out.println("\nDone! Total visited: " + visited.size());
        return documents;
    }

    // Normalize URL to avoid duplicate visits
    private String normalizeUrl(String url) {
        if (url == null) return null;

        // Remove fragment (e.g., #section)
        int hashIndex = url.indexOf('#');
        if (hashIndex != -1) {
            url = url.substring(0, hashIndex);
        }

        // Remove trailing slash
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    // Getter for the page texts to tokenize them
    public Map<String, String> getPageTexts() {
        return this.page_texts;
    }

    // Tokenize page texts into a list of IndexDocuments
    public static List<IndexDocument> tokenization(Map<String, String> pageTexts) {
        List<IndexDocument> tokenedUrls = new ArrayList<>();

        for (Map.Entry<String, String> entry : pageTexts.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            List<String> TokensList = Tokenize(value);
            tokenedUrls.add(new IndexDocument(key, TokensList));
        }

        return tokenedUrls;
    }

    // Tokenizer helper method
    public static List<String> Tokenize(String text) {
        List<String> tokens = new ArrayList<>();

        if (text == null || text.isEmpty()) return tokens;

        String noPunctuation = text.replaceAll("\\p{Punct}", " ");
        String[] words = noPunctuation.split("\\s+");

        for (String word : words) {
            String trimmed = word.trim().toLowerCase();
            if (!trimmed.isEmpty()) {
                tokens.add(InvertedIndex.stemWord( trimmed));
            }
        }

        return tokens;
    }

    // Print tokenized content for testing
    public static void printTokens(Map<String, List<String>> tokenizedUrls) {
        for (Map.Entry<String, List<String>> entry : tokenizedUrls.entrySet()) {
            String key = entry.getKey();
            List<String> value = entry.getValue();

            System.out.print(key + " Tokens:");
            for (String token : value) {
                System.out.print(token + " ");
            }
            System.out.println();
        }
    }
}
