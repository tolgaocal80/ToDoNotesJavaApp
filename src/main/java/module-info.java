module com.tolgaocal80.todolist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tolgaocal80.todolist to javafx.fxml;
    exports com.tolgaocal80.todolist;
}