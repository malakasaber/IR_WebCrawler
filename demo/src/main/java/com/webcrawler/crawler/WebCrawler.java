package com.webcrawlerimport org.jsoup.Jsoup;
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
    public void start_crawling(String[] seeds){

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

        System.out.println("\n Done! Total visited: " + visited.size());
    }

}

