package com.example.android.movieapp.modules;

public class Review {
    /*
    *
    {
   "id":1544,
   "page":1,
   "results":[
      {
         "author":"Nubyan",
         "content":"That was a great movie.\r\n\r\nI read the subject matter in the preview and thought...this sounds interesting. I can't believe this came out in 2005. Twelve years isn't that long ago, however...it fits right in with 2017.\r\n\r\nIt was easy to fall for the main characters. I found myself being torn between wanting Rachel to remain with Heck and wanting her to be with Luce.\r\n\r\nIt's a story of love. Just when you think you've found true love, life throws you a curve ball and complicates everything.\r\n\r\nIn the end, everyone seems to end up where they should be.",
         "id":"59c74eacc3a3681454050457",
         "url":"https://www.themoviedb.org/review/59c74eacc3a3681454050457"
      }
   ],
   "total_pages":1,
   "total_results":1
}
    *
    * */
private String author,id,content,url;

    public Review( String id , String author, String content, String url) {
        this.author = author;
        this.id = id;
        this.content = content;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
