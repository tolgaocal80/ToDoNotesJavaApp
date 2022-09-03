package com.tolgaocal80.todolist;

import com.tolgaocal80.todolist.datamodel.ToDoData;
import com.tolgaocal80.todolist.datamodel.ToDoItem;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class Controller {

    private ObservableList<ToDoItem> toDoItems;
    @FXML
    private ListView<ToDoItem> todoListView;
    @FXML
    private TextArea itemDetailsTextArea;
    @FXML
    private Label deadLineLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private Button addNewItemButton;
    @FXML
    private HBox toolBarHBox;
    @FXML
    private ChoiceBox<String> filterTodoItemsBox;
    @FXML
    private MenuItem exitButton;

    public void deleteItem(ToDoItem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Item");
        alert.setHeaderText("Delete: "+item.getShortDescription());
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            ToDoData.getInstance().deleteTodoItem(item);
        }
    }

    public void initialize(){

        /*
        ToDoItem item1 = new ToDoItem("Mail Birthday card", "Buy a birthday card",
                LocalDateTime.of(LocalDate.of(2022,9,2), LocalTime.of(13,30)));
        ToDoItem item2 = new ToDoItem("Doctor appointment", "Go to doctor",
                LocalDateTime.of(LocalDate.of(2022,9,2), LocalTime.of(8,30)));
        ToDoItem item3 = new ToDoItem("Finish design proposal for client", "Work",
                LocalDateTime.of(LocalDate.of(2022,9,2), LocalTime.of(21,30)));
        ToDoItem item4 = new ToDoItem("Go to the vet for dog", "Go to vet",
                LocalDateTime.of(LocalDate.of(2022,9,2), LocalTime.of(22,30)));
        ToDoItem item5 = new ToDoItem("Pickup Doug at train", "Go to train station",
                LocalDateTime.of(LocalDate.of(2022,9,2), LocalTime.of(15,30)));
        toDoItems = FXCollections.observableArrayList();
        toDoItems.add(item1);
        toDoItems.add(item2);
        toDoItems.add(item3);
        toDoItems.add(item4);
        toDoItems.add(item5);
        ToDoData.getInstance().setToDoItems(toDoItems);

         */

        exitButton.setOnAction(event -> {
            Platform.exit();
        });

        Tooltip tooltip = new Tooltip("Add a new note");
        tooltip.setShowDelay(Duration.millis(500));
        addNewItemButton.setTooltip(tooltip);

        toolBarHBox.setSpacing(10);

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
        });

        listContextMenu.getItems().addAll(deleteMenuItem);

        todoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
                itemDetailsTextArea.setText(item.getDetails());
                deadLineLabel.setText(item.getDeadLine().format(DateTimeFormatter.ofPattern("dd/MMMM/yy, k:mm B")));
            }
        });

        ObservableList<ToDoItem> sortedList = ToDoData.getInstance().getToDoItems().sorted(Comparator.comparing(ToDoItem::getDeadLine));

        Tooltip filterTooltip = new Tooltip("Filter notes");
        filterTooltip.setShowDelay(Duration.millis(500));
        filterTodoItemsBox.setTooltip(filterTooltip);
        filterTodoItemsBox.getItems().add("All notes");
        filterTodoItemsBox.getItems().add("Future notes");
        filterTodoItemsBox.getItems().add("Past due notes");


        filterTodoItemsBox.setOnAction(event -> {
            Object selectedItem = filterTodoItemsBox.getSelectionModel().getSelectedItem();
            if (selectedItem.equals("All notes")){
                todoListView.setItems(sortedList.filtered(null));
            }else if (selectedItem.equals("Future notes")){
                todoListView.setItems(sortedList.filtered(item -> item.getDeadLine().isAfter(LocalDateTime.now())));
            } else if (selectedItem.equals("Past due notes")) {
                todoListView.setItems(sortedList.filtered(item -> item.getDeadLine().isBefore(LocalDateTime.now())));
            }
            todoListView.getSelectionModel().selectFirst();
        });

        filterTodoItemsBox.getSelectionModel().selectFirst();

        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
   //     todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                ListCell cell = new ListCell<ToDoItem>() {
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadLine().isBefore(LocalDateTime.now())) {
                                setTextFill(Color.RED);
                            } else if (item.getDeadLine().isAfter(LocalDateTime.now().plusDays(2))) {
                                setTextFill(Color.GREEN);
                            } else {
                                setTextFill(Color.DARKCYAN);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener((observable, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty){
                        cell.setContextMenu(null);
                    }else {
                        cell.setContextMenu(listContextMenu);
                    }
                });
                return cell;
            }
        });
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent){
        ToDoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null){
            if (keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new Todo item");
        dialog.setHeaderText("Use this dialog to create new todo item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("toDoItemDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get()==ButtonType.OK){
            DialogController controller = fxmlLoader.getController();
            ToDoItem toDoItem = controller.processResults();
         //   todoListView.getItems().removeAll();
         //   todoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
      //      todoListView.setItems(ToDoData.getInstance().getToDoItems());
            todoListView.getSelectionModel().select(toDoItem);
        }
    }


}