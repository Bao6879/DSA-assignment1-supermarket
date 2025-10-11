package main;

import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import main.supermarketComponents.*;
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
        setUpShelfContextMenu(shelfContextMenu);
    }

    private void goodsInitialization()
    {
        goodsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        goodsQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        goodsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        goodsWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        goodsDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        goodsImage.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        goodsNo.setCellValueFactory(cellData -> {
            int index=goodsTable.getItems().indexOf(cellData.getValue())+1;
            return new SimpleIntegerProperty(index).asObject();
        });
        setUpGoodsContextMenu(goodsContextMenu);
    }

    private void setUpFloorAreaContextMenu(ContextMenu contextMenu) {
        contextMenu.getItems().clear();
        contextMenu.getItems().add(createEditMenuItem(floorAreaTable, floorAreaNo));
        contextMenu.getItems().add(createDeleteMenuItem(floorAreaTable));
        contextMenu.getItems().add(createVisitMenuItem(floorAreaTable, aisleTable));
        contextMenu.getItems().add(new MenuItem("More"));
        floorAreaTable.setContextMenu(contextMenu);
    }

    public void setUpAisleContextMenu(ContextMenu contextMenu) {
        contextMenu.getItems().clear();
        contextMenu.getItems().add(createEditMenuItem(aisleTable, aisleNo));
        contextMenu.getItems().add(createDeleteMenuItem(aisleTable));
        contextMenu.getItems().add(createVisitMenuItem(aisleTable, shelfTable));
        contextMenu.getItems().add(new MenuItem("More"));
        aisleTable.setContextMenu(contextMenu);
    }

    public void setUpShelfContextMenu(ContextMenu contextMenu)
    {
        contextMenu.getItems().clear();
        contextMenu.getItems().add(createEditMenuItem(shelfTable, shelfNo));
        contextMenu.getItems().add(createDeleteMenuItem(shelfTable));
        contextMenu.getItems().add(createVisitMenuItem(shelfTable, goodsTable));
        contextMenu.getItems().add(new MenuItem("More"));
        shelfTable.setContextMenu(contextMenu);
    }

    public void setUpGoodsContextMenu(ContextMenu contextMenu)
    {
        contextMenu.getItems().clear();
        contextMenu.getItems().add(createEditMenuItem(goodsTable, goodsNo));
        contextMenu.getItems().add(createDeleteMenuItem(goodsTable));
        contextMenu.getItems().add(new MenuItem("More"));
        goodsTable.setContextMenu(contextMenu);
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
                    FloorArea temp=getFloorAreaFromString(floorArea.trim());
                    baoList.addNode(new BaoNode<>(temp));
                    floorAreaTable.getItems().add(temp);
                    handledLength += floorArea.length() + 1;
                }
            }
            else if (pos.getContent() instanceof FloorArea) {
                int handledLength = 0;
                String aisle;
                while (handledLength < input.length()) {
                    aisle = Utilities.extractElement(input.substring(handledLength));
                    Aisle temp=getAisleFromString(aisle.trim());
                    ((Components)(findNodeByPath(currentPath, pos).getContent())).getInnerList().addNode(new BaoNode<>(temp));
                    aisleTable.getItems().add(temp);
                    handledLength += aisle.length() + 1;
                }
            }
            else if (pos.getContent() instanceof Aisle) {
                int handledLength = 0;
                String shelf;
                while (handledLength < input.length()) {
                    shelf = Utilities.extractElement(input.substring(handledLength));
                    Shelf temp=getShelfFromString(shelf);
                    ((Components)(findNodeByPath(currentPath, pos).getContent())).getInnerList().addNode(new BaoNode<>(temp));
                    shelfTable.getItems().add(temp);
                    handledLength += shelf.length() + 1;
                }
            }
            else
            {
                int handledLength = 0;
                String goods;
                while (handledLength < input.length()) {
                    goods = Utilities.extractElement(input.substring(handledLength));
                    Goods temp=getGoodFromString(goods);
                    ((Components)(findNodeByPath(currentPath, pos).getContent())).getInnerList().addNode(new BaoNode<>(temp));
                    goodsTable.getItems().add(temp);
                    handledLength += goods.length() + 1;
                }
            }
        }
    }

    ///MENU ITEMS
    private MenuItem createEditMenuItem(TableView <?> tableView, TableColumn <?, ?> noColumn)
    {
        MenuItem edit=new MenuItem("Change Attributes to Field Value");
        edit.setOnAction(event -> {
            if (tableView.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(tableView.getSelectionModel().getSelectedItem().toString());
            Object replaced;
            if (tmp.getContent() instanceof FloorArea)
                replaced = getFloorAreaFromString(Utilities.extractElement(rootField.getText()));
            else if (tmp.getContent() instanceof Aisle)
                replaced = getAisleFromString(Utilities.extractElement(rootField.getText()));
            else if (tmp.getContent() instanceof Shelf)
                replaced = getShelfFromString(Utilities.extractElement(rootField.getText()));
            else
                replaced = getGoodFromString(Utilities.extractElement(rootField.getText()));
            if (currentPath.getSize()>=1) {
                ((Components) replaced).setInnerList(((Components)(((Components) findNodeByPath(currentPath, currentPath.getNode(currentPath.getSize()-1)).getContent()).getInnerList().searchNode(tmp).getContent())).getInnerList());
                ((BaoNode<Components>) (findNodeByPath(currentPath, currentPath.getNode(currentPath.getSize()-1)))).getContent().getInnerList().searchNode(tmp).setContent(replaced);
            }
            else {
                ((Components) replaced).setInnerList(((Components) baoList.searchNode(tmp).getContent()).getInnerList());
                baoList.searchNode(tmp).setContent(replaced);
            }
            ((TableView<Object>)(tableView)).getItems().set(((TableColumn <Components, Integer>)(noColumn)).getCellData((Components) tmp.getContent())-1, replaced); //0 indexed
        });
        return edit;
    }

    private MenuItem createDeleteMenuItem(TableView <?> tableView)
    {
        MenuItem delete=new MenuItem("Delete");
        delete.setOnAction(event -> {
            if (tableView.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(tableView.getSelectionModel().getSelectedItem().toString());
            if (tmp.getContent() instanceof FloorArea)
                baoList.removeNode(tmp);
            else if (tmp.getContent() instanceof Aisle)
                ((FloorArea)(findNodeByPath(currentPath, currentPath.getNode(0)).getContent())).getInnerList().removeNode(tmp);
            else if (tmp.getContent() instanceof Shelf)
                ((Aisle)(findNodeByPath(currentPath, currentPath.getNode(1)).getContent())).getInnerList().removeNode(tmp);
            else
                ((Shelf)(findNodeByPath(currentPath, currentPath.getNode(2)).getContent())).getInnerList().removeNode(tmp);
            tableView.getItems().remove(tmp.getContent());
        });
        return delete;
    }

    private MenuItem createVisitMenuItem(TableView <?> oldTableView, TableView <?> newTableView)
    {
        MenuItem visit=new MenuItem("Visit");
        visit.setOnAction(event -> {
            if (oldTableView.getSelectionModel().getSelectedItem()==null)
                return;
            VBox.setVgrow(oldTableView, Priority.NEVER);
            oldTableView.setPrefWidth(0);
            oldTableView.setPrefHeight(0);

            VBox.setVgrow(newTableView, Priority.ALWAYS);
            newTableView.setPrefWidth(100);
            newTableView.setPrefHeight(100);

            pos=Utilities.constructNode(oldTableView.getSelectionModel().getSelectedItem().toString());
            if (pos!=null) {
                currentPath.addNode(pos);
                BaoNode tmp=findNodeByPath(currentPath, pos);
                currentList=Utilities.copyList(((Components)(tmp.getContent())).getInnerList());

                if (pos.getContent() instanceof FloorArea)
                    moveToFloorArea(((FloorArea)pos.getContent()).getTitle());
                else if (pos.getContent() instanceof Aisle)
                    moveToAisle(((Aisle)pos.getContent()).getName());
                else if (pos.getContent() instanceof Shelf)
                    moveToShelf(String.valueOf(((Shelf)pos.getContent()).getNumber()));
            }
        });
        return visit;
    }

    ///INFO EXTRACTION FROM STRINGS
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

    private Goods getGoodFromString(String goods)
    {
        String name, description, weight, price, quantity, temperature, image;
        int currentPosition=0;
        name = Utilities.extractAttribute(goods);
        currentPosition+=name.length()+1;

        description=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=description.length()+1;

        weight=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=weight.length()+1;

        price=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=price.length()+1;

        quantity=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=quantity.length()+1;

        temperature=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=temperature.length()+1;

        image=Utilities.extractElement(goods.substring(currentPosition));

        return new Goods(name.trim(), description.trim(), Double.parseDouble(weight.trim()), Double.parseDouble(price.trim()), Integer.parseInt(quantity.trim()), Double.parseDouble(temperature.trim()), image.trim());
    }

    ///MOVEMENT TO COMPONENTS
    private void moveToFloorArea(String destination) {
        label.setText("Floor Area: "+destination);
        aisleTable.getItems().clear();
        for (Object node: currentList)
            aisleTable.getItems().add((Aisle)(node));
    }

    private void moveToAisle(String destination) {
        label.setText("Aisle: "+destination);
        shelfTable.getItems().clear();
        for (Object node: currentList)
            shelfTable.getItems().add((Shelf) (node));
    }

    private void moveToShelf(String destination) {
        label.setText("Shelf: "+destination);
        goodsTable.getItems().clear();
        for (Object node: currentList)
            goodsTable.getItems().add((Goods) (node));
    }

    @FXML
    private void backAction(ActionEvent event) {
        if (pos==null)
            return;
        if (pos.getContent() instanceof FloorArea) {
            pos=null;
            label.setText("Bao's Hypermarket");
            floorAreaTable.getItems().clear();
            for (FloorArea floorArea : baoList)
                floorAreaTable.getItems().add(floorArea);

            VBox.setVgrow(aisleTable, Priority.NEVER);
            aisleTable.setPrefWidth(0);
            aisleTable.setPrefHeight(0);

            VBox.setVgrow(floorAreaTable, Priority.ALWAYS);
            floorAreaTable.setPrefWidth(100);
            floorAreaTable.setPrefHeight(100);
        }
        else if (pos.getContent() instanceof Aisle) {
            pos=new BaoNode(currentPath.getNode(0).getContent());

            currentList.clear();
            for (Object aisle: ((Components)(findNodeByPath(currentPath.subList(0), currentPath.getNode(0)).getContent())).getInnerList())
                currentList.addNode(new BaoNode<>((Aisle)(aisle)));
            moveToFloorArea(((FloorArea)pos.getContent()).getTitle());

            VBox.setVgrow(shelfTable, Priority.NEVER);
            shelfTable.setPrefWidth(0);
            shelfTable.setPrefHeight(0);

            VBox.setVgrow(aisleTable, Priority.ALWAYS);
            aisleTable.setPrefWidth(100);
            aisleTable.setPrefHeight(100);
        }
        else if (pos.getContent() instanceof Shelf)
        {
            pos=new BaoNode(currentPath.getNode(1).getContent());

            currentList.clear();
            for (Object shelf: ((Components)(findNodeByPath(currentPath.subList(1), currentPath.getNode(1)).getContent())).getInnerList())
                currentList.addNode(new BaoNode<>((Shelf)(shelf)));
            moveToAisle(((Aisle)pos.getContent()).getName());

            VBox.setVgrow(goodsTable, Priority.NEVER);
            goodsTable.setPrefWidth(0);
            goodsTable.setPrefHeight(0);

            VBox.setVgrow(shelfTable, Priority.ALWAYS);
            shelfTable.setPrefWidth(100);
            shelfTable.setPrefHeight(100);
        }
        currentPath.removeNode(currentPath.getNode(currentPath.getSize()-1));
    }

    ///UTILITIES
    private BaoNode <?> findNodeByPath(BaoList <?> path, BaoNode <?> node)
    {
        if (path==null || node==null)
            return null;
        switch (path.getSize())
        {
            case 1: return baoList.searchNode((BaoNode<FloorArea>) node);
            case 2: return baoList.searchNode((BaoNode<FloorArea>) path.getNode(0)).getContent().getInnerList().searchNode((BaoNode<Aisle>) node);
            case 3: return baoList.searchNode((BaoNode<FloorArea>) path.getNode(0)).getContent().getInnerList().searchNode((BaoNode<Aisle>) path.getNode(1)).getContent().getInnerList().searchNode((BaoNode<Shelf>) node);
            default: return null;
        }
    }

    private BaoNode <?> fullSearch(BaoNode <?> node)
    {
        if (node.getDepth()==0)
            return baoList.searchNode((BaoNode<FloorArea>) node);
        for (FloorArea floorArea: baoList)
        {
            if (node.getDepth()==1)
            {
                BaoNode <Aisle> tmp=floorArea.getInnerList().searchNode((BaoNode<Aisle>) node);
                if (tmp!=null)
                    return tmp;
                continue;
            }
            for (Aisle aisle: floorArea.getInnerList())
            {
                if (node.getDepth()==2)
                {
                    BaoNode <Shelf> tmp=aisle.getInnerList().searchNode((BaoNode<Shelf>) node);
                    if (tmp!=null)
                        return tmp;
                    continue;
                }
                for (Shelf shelf: aisle.getInnerList())
                {
                    BaoNode <Goods> tmp=shelf.getInnerList().searchNode((BaoNode<Goods>) node);
                    if (tmp!=null)
                        return tmp;
                }
            }
        }
        return null;
    }
}
