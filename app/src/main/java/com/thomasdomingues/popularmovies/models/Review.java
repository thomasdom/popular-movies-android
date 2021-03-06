package com.thomasdomingues.popularmovies.models;


public class Review
{
    private String id;
    private String author;
    private String content;
    private String url;

    /**
     * @param content TMDB review content
     * @param id      TMDB review identifier
     * @param author  TDMB review author
     * @param url     TMDB review URL
     */
    public Review(String id, String author, String content, String url)
    {
        super();
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getUrl()
    {
        return url;
    }
}
