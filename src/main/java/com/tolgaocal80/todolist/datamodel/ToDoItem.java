package com.tolgaocal80.todolist.datamodel;

import java.time.LocalDateTime;

public class ToDoItem {

    private String shortDescription;
    private String details;
    private LocalDateTime deadLine;

    public ToDoItem(String shortDescription, String details, LocalDateTime deadLine) {
        this.shortDescription = shortDescription;
        this.details = details;
        this.deadLine = deadLine;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public String getShortDescription() {
        return shortDescription;
    }
}
