package com.tolgaocal80.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {

    private static ToDoData instance = new ToDoData();
    private static String filename = "TodoListItems.txt";
    private ObservableList<ToDoItem> toDoItems;
    private DateTimeFormatter formatter;

    private ToDoData(){
        formatter = DateTimeFormatter.ofPattern("dd-MM-yy, k:mm \"B\"");
    }

    public static ToDoData getInstance() {
        if (instance == null){
            instance = new ToDoData();
        }
        return instance;
    }

    public void setToDoItems(ObservableList<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }

    public void addTodoItem(ToDoItem toDoItem) {
        toDoItems.add(toDoItem);
    }
    public ObservableList<ToDoItem> getToDoItems() {return toDoItems;}

    public void loadToDoItems() throws IOException {
        toDoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try{
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");
                String shortDescription = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];
                LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
                ToDoItem toDoItem = new ToDoItem(shortDescription, details, dateTime);
                toDoItems.add(toDoItem);
            }
        }finally {
            if (br != null){br.close();}
        }
    }

    public void storeToDoItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
        try{
            Iterator<ToDoItem> iterator = toDoItems.iterator();
            while (iterator.hasNext()){
                ToDoItem item = iterator.next();
                bufferedWriter.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadLine().format(formatter)));
                bufferedWriter.newLine();
            }

        }finally {
            if ((bufferedWriter != null)){
                bufferedWriter.close();
            }
        }
    }

    public void deleteTodoItem(ToDoItem item) {
        if (this.toDoItems.contains(item)){
            toDoItems.remove(item);
        }
    }
}
