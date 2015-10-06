package com.cs616.trysqlite;

/**
 * Created by ian on 15-09-02.
 */
public class Note {
    private long id;
    private long categoryId;
    private String title;
    private String body;

    public Note() {

    }

    public Note(long id, long categoryId, String title, String body) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
