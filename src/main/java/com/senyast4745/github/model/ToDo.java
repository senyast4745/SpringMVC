package com.senyast4745.github.model;

public class ToDo {

    private final long id;
    private String description;
    private boolean checked;

    public ToDo(long id, String description, boolean checked) {
        this.id = id;
        this.description = description;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
