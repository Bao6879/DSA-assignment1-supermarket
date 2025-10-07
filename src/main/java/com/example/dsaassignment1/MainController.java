package com.example.dsaassignment1;

import com.example.dsaassignment1.linkedList.BaoList;
import com.example.dsaassignment1.linkedList.BaoNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {
    @FXML
    public ListView <String> list;
    public TextField field;
    public ChoiceBox<String> rootChoice;
    public Button button;

    private BaoList baoList;

    @FXML
    private void initialize() {
        rootChoice.getItems().addAll("Select One", "Remove", "Search", "Smart Add", "Move To", "Add Floor Area");
        rootChoice.getSelectionModel().selectFirst();

        BaoNode newNode = baoList.getHead();
        while (newNode.getNext()!= null) {
            newNode = newNode.getNext();
        }
    }


}
