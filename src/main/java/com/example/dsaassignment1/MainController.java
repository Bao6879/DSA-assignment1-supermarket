package com.example.dsaassignment1;

import com.example.dsaassignment1.linkedList.BaoList;
import com.example.dsaassignment1.linkedList.BaoNode;
import com.example.dsaassignment1.supermarketComponents.FloorArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import kotlin.ULong;

public class MainController {
    @FXML
    public ListView <String> list;
    public TextField rootField;
    public ChoiceBox<String> rootChoice;
    public Button rootButton;

    private BaoList baoList;

    @FXML
    private void initialize() {
        rootChoice.getItems().addAll("Select One", "Remove", "Search", "Smart Add", "Move To", "Add Floor Areas", "Add Objects With Absolute Path");
        rootChoice.getSelectionModel().selectFirst();

        if (baoList!=null) {
            BaoNode newNode = baoList.getHead();
            while (newNode != null) {
                String content = "";
                content += ((FloorArea) newNode.getContent()).getTitle();
                content += "\n" + ((FloorArea) newNode.getContent()).getLevel();
                list.getItems().add(content);
                newNode = newNode.getNext();
            }
        }
        else
        {
            baoList=new BaoList();
        }
    }

    @FXML
    private void rootAction(ActionEvent event) {
        String choice=rootChoice.getSelectionModel().getSelectedItem();
        String input=rootField.getText();
        if (choice.equals("Add Floor Areas")) {
            int handledLength=0;
            String floorArea, title, level, newListElement;
            while (handledLength<input.length())
            {
                newListElement="";
                floorArea=Utilities.extractElement(input.substring(handledLength, input.length()));
                title=Utilities.extractAttribute(floorArea);

                newListElement+=title+"\n";
                level=Utilities.extractElement(floorArea.substring(title.length(), title.length()));
                newListElement+=level;

                baoList.addNode(new BaoNode(new FloorArea(title, Integer.parseInt(title))));
                list.getItems().add(newListElement);
                handledLength+=floorArea.length()+1;
            }
        }
    }
}
