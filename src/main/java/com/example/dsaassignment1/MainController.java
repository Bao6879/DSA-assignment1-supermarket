package com.example.dsaassignment1;

import com.example.dsaassignment1.linkedList.BaoList;
import com.example.dsaassignment1.linkedList.BaoNode;
import com.example.dsaassignment1.supermarketComponents.Aisle;
import com.example.dsaassignment1.supermarketComponents.FloorArea;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import kotlin.ULong;

public class MainController {
    @FXML
    public VBox vBox;
    public ContextMenu contextMenu=new ContextMenu();

    //Floor Area
    public TableView <FloorArea> table;
    public TableColumn <FloorArea, String> floorAreaTitle, floorAreaLevel;
    public TableColumn <FloorArea, Integer> floorAreaNo;

    //Aisles
    public TableView <Aisle> aisleTable;
    public TableColumn <Aisle, String> aisleName;
    public TableColumn <Aisle, Integer> aisleNo;
    public TableColumn <Aisle, Double> aisleWidth, aisleLength, aisleTemperature;

    //Shelf


    //Goods

    //General
    public TextField rootField;
    public ChoiceBox<String> rootChoice;
    public Button rootButton, backButton;
    public Label label;

    //Back end
    private BaoList <FloorArea> baoList=new BaoList<>();
    private BaoList currentList=new BaoList<>();
    private BaoNode pos;

    @FXML
    private void initialize() {
        rootChoice.getItems().addAll("Add", "Remove", "Search", "Smart Add", "Move To", "Add Objects With Absolute Path");
        rootChoice.getSelectionModel().selectFirst();

        floorAreaInitialization();
        aisleInitialization();
    }

    private void floorAreaInitialization()
    {
        floorAreaTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        floorAreaLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        floorAreaNo.setCellValueFactory(cellData -> {
            int index=table.getItems().indexOf(cellData.getValue())+1;
            return new SimpleIntegerProperty(index).asObject();
        });

        setUpContextMenu(contextMenu);
    }

    private void aisleInitialization()
    {
        aisleName.setCellValueFactory(new PropertyValueFactory<>("name"));
        aisleLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        aisleWidth.setCellValueFactory(new PropertyValueFactory<>("width"));
        aisleTemperature.setCellValueFactory(new PropertyValueFactory<>("temperature"));
        aisleNo.setCellValueFactory(cellData -> {
            int index=aisleTable.getItems().indexOf(cellData.getValue())+1;
            return new SimpleIntegerProperty(index).asObject();
        });
    }
    private void setUpContextMenu(ContextMenu contextMenu) {
        contextMenu.getItems().clear();
        contextMenu.getItems().add(new MenuItem("Edit"));
        contextMenu.getItems().add(new MenuItem("Delete"));
        MenuItem visit = new MenuItem("Visit");
        visit.setOnAction(event -> {
            VBox.setVgrow(table, Priority.NEVER);
            table.setPrefWidth(0);
            table.setPrefHeight(0);

            VBox.setVgrow(aisleTable, Priority.ALWAYS);
            aisleTable.setPrefWidth(100);
            aisleTable.setPrefHeight(100);

            pos=Utilities.constructNode(table.getSelectionModel().getSelectedItem().toString());
            if (pos!=null && pos.getContent().getClass()==FloorArea.class) {
                aisleTable.getItems().clear();
                currentList.clear();
                for (FloorArea floorArea : baoList) {
                    if (floorArea.equals(pos.getContent())) {
                        currentList=floorArea.getAisles();
                        break;
                    }
                }
                moveToFloorArea(((FloorArea)pos.getContent()).getTitle());
            }
        });
        contextMenu.getItems().add(visit);
        contextMenu.getItems().add(new MenuItem("More"));
        table.setContextMenu(contextMenu);
    }

    @FXML
    private void rootAction(ActionEvent event) {
        String choice=rootChoice.getSelectionModel().getSelectedItem();
        String input=rootField.getText();
        if (choice.equals("Add")) {
            if (pos==null) {
                int handledLength = 0;
                String floorArea, title, level;
                while (handledLength < input.length()) {
                    floorArea = Utilities.extractElement(input.substring(handledLength));

                    title = Utilities.extractAttribute(floorArea);
                    level = Utilities.extractElement(floorArea.substring(title.length() + 1));

                    FloorArea temp = new FloorArea(title.trim(), Integer.parseInt(level.trim()));
                    baoList.addNode(new BaoNode<>(temp));
                    table.getItems().add(temp);

                    handledLength += floorArea.length() + 1;
                }
            }
            else if (pos.getContent().getClass()==FloorArea.class) {
                int handledLength = 0;
                String aisle, name, length, width, temperature;
                while (handledLength < input.length()) {
                    aisle = Utilities.extractElement(input.substring(handledLength));

                    int currentPosition=0;
                    name = Utilities.extractAttribute(aisle);
                    currentPosition+=name.length()+1;

                    length=Utilities.extractAttribute(aisle.substring(currentPosition));
                    currentPosition+=length.length()+1;

                    width=Utilities.extractAttribute(aisle.substring(currentPosition));
                    currentPosition+=width.length()+1;

                    temperature=Utilities.extractElement(aisle.substring(currentPosition));

                    Aisle temp = new Aisle(name.trim(), Double.parseDouble(length.trim()), Double.parseDouble(width.trim()), Double.parseDouble(temperature.trim()));
                    ((FloorArea)(baoList.searchNode(pos).getContent())).getAisles().addNode(new BaoNode<>(temp));
                    aisleTable.getItems().add(temp);

                    handledLength += aisle.length() + 1;
                }
            }
        }
    }

    private void moveToFloorArea(String destination) {
        label.setText("Floor Area: "+destination);
        table.getItems().clear();
        for (Object node: currentList)
        {
            aisleTable.getItems().add((Aisle)(node));
        }
    }

    @FXML
    private void backAction(ActionEvent event) {
        if (pos==null)
            return;
        if (pos.getContent().getClass()==FloorArea.class) {
            pos=null;
            label.setText("Bao's Hypermarket");
            table.getItems().clear();
            for (FloorArea floorArea : baoList) {
                table.getItems().add(floorArea);
            }

            VBox.setVgrow(aisleTable, Priority.NEVER);
            aisleTable.setPrefWidth(0);
            aisleTable.setPrefHeight(0);

            VBox.setVgrow(table, Priority.ALWAYS);
            table.setPrefWidth(100);
            table.setPrefHeight(100);
        }
    }
}
