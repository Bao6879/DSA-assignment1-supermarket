package com.example.dsaassignment1;

import com.example.dsaassignment1.linkedList.BaoList;
import com.example.dsaassignment1.linkedList.BaoNode;
import com.example.dsaassignment1.supermarketComponents.FloorArea;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import kotlin.ULong;

public class MainController {
    @FXML
    public ListView <String> list;
    public TextField rootField;
    public ChoiceBox<String> rootChoice;
    public Button rootButton, backButton;
    public Label label;

    private BaoList <FloorArea> baoList=new BaoList<>();
    private BaoList currentList=new BaoList<>();
    private BaoNode pos;

    @FXML
    private void initialize() {
        rootChoice.getItems().addAll("Select One", "Remove", "Search", "Smart Add", "Move To", "Add Floor Areas", "Add Objects With Absolute Path");
        rootChoice.getSelectionModel().selectFirst();

        if (baoList!=null && baoList.getSize()>0) {
            for (FloorArea floorArea : baoList) {
                String content = "";
                content += floorArea.getTitle();
                content += "\n" + floorArea.getLevel();
                list.getItems().add(content);
            }
        }

        list.setOnMouseClicked(mouseEvent -> {
            if (list.getSelectionModel().getSelectedItem()!=null) {
                Object selectedItem = list.getSelectionModel().getSelectedItem();
                pos=Utilities.constructNode((String) selectedItem);
                if (pos.getContent().getClass()==FloorArea.class) {
                    for (FloorArea floorArea : baoList) {
                        if (floorArea.equals(pos.getContent())) {
                            currentList=((FloorArea)pos.getContent()).getAisles();
                        }
                    }
                    moveToFloorArea(((FloorArea)pos.getContent()).getTitle());
                }
                System.out.println(selectedItem);
            }
        });
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
                floorArea=Utilities.extractElement(input.substring(handledLength));

                title=Utilities.extractAttribute(floorArea);
                newListElement+="Floor Area: "+title+";                                                                      ";
                level=Utilities.extractElement(floorArea.substring(title.length()+1));
                newListElement+="Level: "+level;

                baoList.addNode(new BaoNode<>(new FloorArea(title, Integer.parseInt(level))));
                list.getItems().add(newListElement);
                handledLength+=floorArea.length()+1;
            }
        }
    }

    private void moveToFloorArea(String destination) {
        label.setText("Floor Area: "+destination);
        list.getItems().clear();
        for (Object node: currentList)
        {
            list.getItems().add(node.toString());
        }
    }

    @FXML
    private void backAction(ActionEvent event) {
        if (pos==null)
            return;
        if (pos.getContent().getClass()==FloorArea.class) {
            pos=null;
            label.setText("Bao's Hypermarket");
            list.getItems().clear();
            for (FloorArea floorArea : baoList) {
                list.getItems().add(floorArea.toString());
            }
        }
    }
}
