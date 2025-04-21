package com.webcrawler.crawler;
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

    //function crawing
    public List<Document> start_crawling(String[] seeds){

        //add URLS in queue
        queue_waiting.addAll(Arrays.asList(seeds));

        while (!queue_waiting.isEmpty() && visited.size() < max_pages){
            //get next url from queue
            String current_url = queue_waiting.poll();

            //to avoid duplicated & ensure it wikipedia link
            if (visited.contains(current_url)) continue;
            if(!current_url.startsWith("https://en.wikipedia.org/wiki/")) continue;

            try {
                //fetch the html document
                Document document = Jsoup.connect(current_url).get();
                visited.add(current_url);

                //save texts content for task 2
                page_texts.put(current_url, document.body().text());

                System.out.println("✅ VISITED: " + current_url);


                //get all links on the current page
                Elements links_page = document.select("a[href]");
                for(Element link : links_page) {
                    //get url for each link to add it in queue
                    String Url = link.attr("abs:href");

                    if (!visited.contains(Url) && Url.startsWith("https://en.wikipedia.org/wiki/")) {
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

        System.out.println("\n Done! Total visited: " + visited.size());
        return documents;
    }

    //getter to get the pageTexts to tokenize it
    public Map<String, String> getPageTexts() {
        return this.page_texts;
    }

    //this function is supposed to take the pageTexts to tokenize it
    public static Map<String, List<String>> tokenization(Map<String, String> pageTexts) {
        //declaring the output variable
        Map<String, List<String>> tokenedUrls = new HashMap<>();

        //looping over each url and its texts
        for (Map.Entry<String, String> entry : pageTexts.entrySet()) {
            //getting the url name and the text in variables
            String key = entry.getKey();
            String value = entry.getValue();
            //storing the tokenized text in a list after using the tokenization function on the text
            List<String> TokensList = Tokenize(value);
            //adding the (url and tokenized-list) to the output variable
            tokenedUrls.put(key, TokensList);
        }
        return tokenedUrls;
    }

    public static List<String> Tokenize(String text) {
        //declaring a list of tokens for the output
        List<String> tokens = new ArrayList<>();

        //if the list is empty return an empty list
        if (text == null || text.isEmpty()) return tokens;

        //removing all punctuation from the text and replacing them with space
        String noPunctiuation = text.replaceAll("\\p{Punct}", " ");

        //adding each word in a list splited by spaces
        String[] words = noPunctiuation.split("\\s+");

        //looping over the list of words to change each char to lowercase
        for (String word : words) {
            String trimmed = word.trim().toLowerCase();
            if (!trimmed.isEmpty()) {

                //adding the word after tokenization to the output variable
                tokens.add(trimmed);
            }
        }
        return tokens;
    }

    //function to test the tokenization output (for testing)
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