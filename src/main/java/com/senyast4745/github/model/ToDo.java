package com.senyast4745.github.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todos")
public class ToDo {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "checked", nullable = false)
    private boolean checked;

    @Column(name = "userName", nullable = false)
    private String userName;

    public ToDo(){
        super();
    }

    public ToDo(long id, String userName, String description, boolean checked) {
        this.id = id;
        this.userName = userName;
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

    public String getUserName() {
        return userName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
