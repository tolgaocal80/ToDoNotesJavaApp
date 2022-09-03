package com.tolgaocal80.todolist;

import com.tolgaocal80.todolist.datamodel.ToDoData;
import com.tolgaocal80.todolist.datamodel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class DialogController {

    @FXML
    private DatePicker deadLinePicker;
    @FXML
    private TextField shortDescriptionText;
    @FXML
    private TextArea detailsText;

    public ToDoItem processResults(){
        String shortDescription = shortDescriptionText.getText().trim();
        String details = detailsText.getText().trim();
        LocalDateTime deadLineValue = deadLinePicker.getValue().atTime(12,40);
        ToDoItem newItem = new ToDoItem(shortDescription,details,deadLineValue);
        ToDoData.getInstance().addTodoItem(newItem);
        return newItem;
    }



}
