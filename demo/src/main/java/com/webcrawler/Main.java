package com.webcrawler;

public class Main {
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();

        String[] seeds = {
                "https://en.wikipedia.org/wiki/List_of_pharaohs",
                "https://en.wikipedia.org/wiki/Pharaoh"
        };

        crawler.start_crawling(seeds);
    }
}
