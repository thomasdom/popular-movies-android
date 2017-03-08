package com.thomasdomingues.popularmovies.models;


public class Genre
{
    private Integer id;
    private String name;

    /**
     * @param id   TMDB genre identifier
     * @param name TMDB genre name
     */
    public Genre(Integer id, String name)
    {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
