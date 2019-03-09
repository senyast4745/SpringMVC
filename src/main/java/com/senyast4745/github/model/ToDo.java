package com.senyast4745.github.model;

public class ToDo {

    private final long id;
    private final String description;

    public ToDo(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
