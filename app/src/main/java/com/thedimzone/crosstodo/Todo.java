package com.thedimzone.crosstodo;

/**
 * Created by thedi on 25.03.2018.
 */

public class Todo {
    private Integer id;
    private String text;
    private boolean completed;

    public Todo()
    {

    }

    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getText()
    {
        return this.text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public boolean isCompleted()
    {
        return this.completed;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }
}
