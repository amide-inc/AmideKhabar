package com.amideinc.khabar.amidenews;

public class Article {
    String src_id;
    String src_name;
    String author;
    String title;
    String description;
    String url;
    String urlToImage;
    String publishedAt;

    public Article(String src_id, String src_name, String author, String title , String description, String url, String urlToImage, String publishedAt) {
        this.src_id = src_id;
        this.src_name = src_name;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
    }

}
