package com.thomasdomingues.popularmovies.models;

public class Video
{
    private String id;
    private String iso6391;
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    /**
     * @param site     Video provider (Youtube, Dailymotion...)
     * @param iso6391  Video ISO 6391 compliant language code
     * @param id       TMDB video identifier
     * @param iso31661 Video ISO 31661 compliant language's country code
     * @param name     TMDB video name
     * @param type     Video type
     * @param key      TMDB video key
     * @param size     TMDB video format
     */
    public Video(String id, String iso6391, String iso31661, String key, String name, String site, int size, String type)
    {
        super();
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIso6391()
    {
        return iso6391;
    }

    public void setIso6391(String iso6391)
    {
        this.iso6391 = iso6391;
    }

    public String getIso31661()
    {
        return iso31661;
    }

    public void setIso31661(String iso31661)
    {
        this.iso31661 = iso31661;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}