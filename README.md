# IR_WebCrawler

# Wikipedia Web Crawler with TF-IDF and Cosine Similarity

A Java application that crawls Wikipedia pages, builds an inverted index, and ranks documents based on their cosine similarity to user queries using TF-IDF weighting.

## Features

- Web crawler that starts from two Wikipedia seed URLs
- Inverted index implementation with term frequencies
- TF-IDF weighting calculation
- Cosine similarity computation for document ranking
- Top 10 document retrieval for user queries

## Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher

## <span style="font-size:28px">Project Structure</span>
src/main/java/com/webcrawler/
├── crawler/          # Web crawling components
│   ├── WebCrawler.java
│   └── CrawlerConfig.java
├── index/            # Inverted index implementation
│   ├── InvertedIndex.java
│   ├── Posting.java
│   └── Document.java
├── similarity/       # TF-IDF and similarity calculations
│   ├── TFIDFCalculator.java
│   ├── CosineSimilarity.java
│   └── QueryProcessor.java
└── Main.java         # Application entry point
