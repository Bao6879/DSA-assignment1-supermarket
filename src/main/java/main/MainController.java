package main;

import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import main.supermarketComponents.Aisle;
import main.supermarketComponents.FloorArea;
import main.supermarketComponents.Goods;
import main.supermarketComponents.Shelf;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainController {

    //Floor Area
    public TableView <FloorArea> floorAreaTable;
    public TableColumn <FloorArea, String> floorAreaTitle, floorAreaLevel;
    public TableColumn <FloorArea, Integer> floorAreaNo;
    public ContextMenu floorAreaContextMenu =new ContextMenu();

    //Aisles
    public TableView <Aisle> aisleTable;
    public TableColumn <Aisle, String> aisleName;
    public TableColumn <Aisle, Integer> aisleNo;
    public TableColumn <Aisle, Double> aisleWidth, aisleLength, aisleTemperature;
    public ContextMenu aisleContextMenu =new ContextMenu();

    //Shelf
    public TableView <Shelf> shelfTable;
    public TableColumn <Shelf, Integer> shelfNumber, shelfNo;
    public ContextMenu shelfContextMenu =new ContextMenu();

    //Goods
    public TableView <Goods> goodsTable;
    public TableColumn <Goods, Integer> goodsQuantity, goodsNo;
    public TableColumn <Goods, Double> goodsPrice, goodsWeight;
    public TableColumn <Goods, String> goodsDescription, goodsName, goodsImage;
    public ContextMenu goodsContextMenu =new ContextMenu();

    //General
    public VBox vBox;
    public TextField rootField;
    public ChoiceBox<String> rootChoice;
    public Button rootButton, backButton;
    public Label label;

    //Back end
    private BaoList <FloorArea> baoList=new BaoList<>();
    private BaoList currentList=new BaoList<>(), currentPath=new BaoList<>();
    private BaoNode pos;

    @FXML
    private void initialize() {
        rootChoice.getItems().addAll("Add", "Remove", "Search", "Smart Add", "Move To", "Add Objects With Absolute Path");
        rootChoice.getSelectionModel().selectFirst();

        floorAreaInitialization();
        aisleInitialization();
        shelfInitialization();
        goodsInitialization();
    }

    private void floorAreaInitialization()
    {
        floorAreaTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        floorAreaLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        floorAreaNo.setCellValueFactory(cellData -> {
            int index= floorAreaTable.getItems().indexOf(cellData.getValue())+1;
            return new SimpleIntegerProperty(index).asObject();
        });
        setUpFloorAreaContextMenu(floorAreaContextMenu);
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
        setUpAisleContextMenu(aisleContextMenu);
    }

    private void shelfInitialization()
    {
        shelfNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        shelfNo.setCellValueFactory(cellData -> {
            int index=shelfTable.getItems().indexOf(cellData.getValue())+1;
            return new SimpleIntegerProperty(index).asObject();
        });
    }

    private void goodsInitialization()
    {
        goodsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        goodsQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        goodsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        goodsWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        goodsDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        goodsImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        goodsNo.setCellValueFactory(cellData -> {
            int index=goodsTable.getItems().indexOf(cellData.getValue())+1;
            return new SimpleIntegerProperty(index).asObject();
        });
    }

    private void setUpFloorAreaContextMenu(ContextMenu contextMenu) {
        contextMenu.getItems().clear();
        MenuItem edit=new MenuItem("Change Attributes to Field Value");
        edit.setOnAction(event -> {
            if (floorAreaTable.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(floorAreaTable.getSelectionModel().getSelectedItem().toString());
            FloorArea replaced=getFloorAreaFromString(Utilities.extractElement(rootField.getText()));
            replaced.setAisles(((FloorArea)tmp.getContent()).getAisles());
            baoList.searchNode(tmp).setContent(replaced);
            floorAreaTable.getItems().set(floorAreaNo.getCellData((FloorArea) tmp.getContent())-1, replaced); //0 indexed
        });
        contextMenu.getItems().add(edit);

        MenuItem delete=new MenuItem("Delete");
        delete.setOnAction(event -> {
            if (floorAreaTable.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(floorAreaTable.getSelectionModel().getSelectedItem().toString());
            baoList.removeNode(tmp);
            floorAreaTable.getItems().remove(tmp.getContent());
        });
        contextMenu.getItems().add(delete);

        MenuItem visit = new MenuItem("Visit");
        visit.setOnAction(event -> {
            if (floorAreaTable.getSelectionModel().getSelectedItem()==null)
                return;
            VBox.setVgrow(floorAreaTable, Priority.NEVER);
            floorAreaTable.setPrefWidth(0);
            floorAreaTable.setPrefHeight(0);

            VBox.setVgrow(aisleTable, Priority.ALWAYS);
            aisleTable.setPrefWidth(100);
            aisleTable.setPrefHeight(100);

            pos=Utilities.constructNode(floorAreaTable.getSelectionModel().getSelectedItem().toString());
            if (pos!=null) {
                for (FloorArea floorArea : baoList) {
                    if (floorArea.equals(pos.getContent())) {
                        currentList=Utilities.copyList(floorArea.getAisles());
                        pos.setContent(floorArea);
                        break;
                    }
                }
                currentPath.addNode(pos);
                moveToFloorArea(((FloorArea)pos.getContent()).getTitle());
            }
        });
        contextMenu.getItems().add(visit);
        contextMenu.getItems().add(new MenuItem("More"));
        floorAreaTable.setContextMenu(contextMenu);
    }

    public void setUpAisleContextMenu(ContextMenu contextMenu) {
        contextMenu.getItems().clear();
        MenuItem edit=new MenuItem("Change Attributes to Field Value");
        edit.setOnAction(event -> {
            if (aisleTable.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(aisleTable.getSelectionModel().getSelectedItem().toString());
            Aisle replaced=getAisleFromString(Utilities.extractElement(rootField.getText()));
            replaced.setShelves(((Aisle)tmp.getContent()).getShelves());
            baoList.searchNode(tmp).setContent(replaced);
            aisleTable.getItems().set(aisleNo.getCellData((Aisle) tmp.getContent())-1, replaced); //0 indexed
        });
        contextMenu.getItems().add(edit);

        MenuItem delete=new MenuItem("Delete");
        delete.setOnAction(event -> {
            if (aisleTable.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(aisleTable.getSelectionModel().getSelectedItem().toString());
            baoList.removeNode(tmp);
            aisleTable.getItems().remove(tmp.getContent());
        });
        contextMenu.getItems().add(delete);

        MenuItem visit = new MenuItem("Visit");
        visit.setOnAction(event -> {
            if (aisleTable.getSelectionModel().getSelectedItem()==null)
                return;
            VBox.setVgrow(aisleTable, Priority.NEVER);
            aisleTable.setPrefWidth(0);
            aisleTable.setPrefHeight(0);

            VBox.setVgrow(shelfTable, Priority.ALWAYS);
            shelfTable.setPrefWidth(100);
            shelfTable.setPrefHeight(100);

            pos=Utilities.constructNode(aisleTable.getSelectionModel().getSelectedItem().toString());
            if (pos!=null) {
                for (FloorArea floorArea : baoList) {
                    if (floorArea.equals(currentPath.getNode(0).getContent())) {
                        for (Aisle aisle: floorArea.getAisles())
                        {
                            if (aisle.equals(pos.getContent())) {
                                currentList=Utilities.copyList(aisle.getShelves());
                                pos.setContent(aisle);
                                break;
                            }
                        }
                    }
                }
                currentPath.addNode(pos);
                moveToAisle(((Aisle)pos.getContent()).getName());
            }
        });
        contextMenu.getItems().add(visit);
        contextMenu.getItems().add(new MenuItem("More"));
        aisleTable.setContextMenu(contextMenu);
    }

    @FXML
    private void rootAction(ActionEvent event) {
        String choice=rootChoice.getSelectionModel().getSelectedItem();
        String input=rootField.getText();
        if (choice.equals("Add")) {
            if (pos==null) {
                int handledLength = 0;
                String floorArea;
                while (handledLength < input.length()) {
                    floorArea = Utilities.extractElement(input.substring(handledLength));
                    handledLength += floorArea.length() + 1;

                    floorArea=floorArea.trim();
                    FloorArea temp=getFloorAreaFromString(floorArea);
                    baoList.addNode(new BaoNode<>(temp));
                    floorAreaTable.getItems().add(temp);
                }
            }
            else if (pos.getContent().getClass()==FloorArea.class) {
                int handledLength = 0;
                String aisle;
                while (handledLength < input.length()) {
                    aisle = Utilities.extractElement(input.substring(handledLength));

                    Aisle temp=getAisleFromString(aisle);
                    ((FloorArea)(baoList.searchNode(pos).getContent())).getAisles().addNode(new BaoNode<>(temp));
                    aisleTable.getItems().add(temp);

                    handledLength += aisle.length() + 1;
                }
            }
            else if (pos.getContent().getClass()==Aisle.class) {
                int handledLength = 0;
                String shelf;
                while (handledLength < input.length()) {
                    shelf = Utilities.extractElement(input.substring(handledLength));

                    Shelf temp=getShelfFromString(shelf);
                    ((Aisle)(((FloorArea)(baoList.searchNode(currentPath.getNode(0)).getContent())).getAisles().searchNode(currentPath.getNode(currentPath.getSize()-1)).getContent())).getShelves().addNode(new BaoNode<>(temp));
                    shelfTable.getItems().add(temp);

                    handledLength += shelf.length() + 1;
                }
            }
        }
    }

    private FloorArea getFloorAreaFromString(String floorArea) {
        String title, level;
        title = Utilities.extractAttribute(floorArea);
        level = Utilities.extractElement(floorArea.substring(title.length() + 1));

        return new FloorArea(title.trim(), Integer.parseInt(level.trim()));
    }

    private Aisle getAisleFromString(String aisle)
    {
        String name, length, width, temperature;
        int currentPosition=0;
        name = Utilities.extractAttribute(aisle);
        currentPosition+=name.length()+1;

        length=Utilities.extractAttribute(aisle.substring(currentPosition));
        currentPosition+=length.length()+1;

        width=Utilities.extractAttribute(aisle.substring(currentPosition));
        currentPosition+=width.length()+1;

        temperature=Utilities.extractElement(aisle.substring(currentPosition));

        return new Aisle(name.trim(), Double.parseDouble(length.trim()), Double.parseDouble(width.trim()), Double.parseDouble(temperature.trim()));
    }

    private Shelf getShelfFromString(String shelf)
    {
        String number;
        number=Utilities.extractAttribute(shelf);

        return new Shelf(Integer.parseInt(number.trim()));
    }

    private void moveToFloorArea(String destination) {
        label.setText("Floor Area: "+destination);
        aisleTable.getItems().clear();
        for (Object node: currentList)
        {
            aisleTable.getItems().add((Aisle)(node));
        }
    }

    private void moveToAisle(String destination) {
        label.setText("Aisle: "+destination);
        shelfTable.getItems().clear();
        for (Object node: currentList)
        {
            shelfTable.getItems().add((Shelf) (node));
        }
    }

    @FXML
    private void backAction(ActionEvent event) {
        if (pos==null)
            return;
        if (pos.getContent().getClass()==FloorArea.class) {
            pos=null;
            label.setText("Bao's Hypermarket");
            floorAreaTable.getItems().clear();
            for (FloorArea floorArea : baoList) {
                floorAreaTable.getItems().add(floorArea);
            }

            VBox.setVgrow(aisleTable, Priority.NEVER);
            aisleTable.setPrefWidth(0);
            aisleTable.setPrefHeight(0);

            VBox.setVgrow(floorAreaTable, Priority.ALWAYS);
            floorAreaTable.setPrefWidth(100);
            floorAreaTable.setPrefHeight(100);
        }
        else if (pos.getContent().getClass()==Aisle.class) {
            pos=currentPath.getNode(0);

            currentList.clear();
            for (Object aisle: ((FloorArea)(baoList.searchNode(pos).getContent())).getAisles())
            {
                currentList.addNode(new BaoNode<>((Aisle)(aisle)));
            }
            moveToFloorArea(((FloorArea)pos.getContent()).getTitle());
            VBox.setVgrow(shelfTable, Priority.NEVER);
            shelfTable.setPrefWidth(0);
            shelfTable.setPrefHeight(0);

            VBox.setVgrow(aisleTable, Priority.ALWAYS);
            aisleTable.setPrefWidth(100);
            aisleTable.setPrefHeight(100);
        }
        currentPath.removeNode(currentPath.getNode(currentPath.getSize()-1));
    }
}
