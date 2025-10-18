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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CliController {

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
    public Label label, comment, instruction;
    public ListView <String> searchView;

    //Back end
    public BaoList <FloorArea> baoList=new BaoList<>();
    private MainApplication mainApp;
    private BaoList currentList=new BaoList<>(), currentPath=new BaoList<>();
    private BaoNode pos;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        rootChoice.getItems().addAll("Add", "Search", "Smart Add", "Reset", "Save", "Load");
        rootChoice.getSelectionModel().selectFirst();
        rootChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setInstructions();
        });
        comment.setText("Follow the instructions below, this label will report errors/mistakes!");
        setInstructions();

        floorAreaInitialization();
        aisleInitialization();
        shelfInitialization();
        goodsInitialization();
    }

    @FXML
    private void setInstructions() {
        switch (rootChoice.getSelectionModel().getSelectedItem())
        {
            case "Add" -> setAddInstructions();
            case "Search" -> instruction.setText("This option searches for goods with a certain name in the entire hypermarket.");
            case "Smart Add" -> instruction.setText("This option will add goods where they 'fit' most. Do this if you know how to format goods.");
            case "Reset" -> instruction.setText("This option will wipe everything. Type 'I am sure' (without ' ) in the field to confirm.");
            case "Save" -> instruction.setText("This option will save the current list in 'baoList.xml'.");
            case "Load" -> instruction.setText("This option will load the list from 'baoList.xml'.");
        }
    }

    private void setAddInstructions()
    {
        switch (currentPath.getSize())
        {
            case 0 -> instruction.setText("Floor Area Format: STRING (title), INT (level); REPEAT");
            case 1 -> instruction.setText("Aisle Format: STRING (name), DOUBLE (length), DOUBLE (width), DOUBLE (temperature); REPEAT");
            case 2 -> instruction.setText("Shelf Format: INT (number); REPEAT");
            case 3 -> instruction.setText("Goods Format: STRING (name), STRING (description), DOUBLE (weight), DOUBLE (price), INT (quantity), DOUBLE (temperature); REPEAT");
        }
    }

    private void floorAreaInitialization()
    {
        floorAreaTitle.setCellValueFactory(new PropertyValueFactory<>("name"));
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
        int handledLength = 0;
        if (choice.equals("Add")) {
            if (pos==null) {
                String floorArea;
                while (handledLength < input.length()) {
                    floorArea = Utilities.extractElement(input.substring(handledLength));
                    if (Utilities.checkStringNotFitType(floorArea, "floorArea"))
                    {
                        comment.setText("Your formatting is off. Check your ',' and ';' count?");
                        return;
                    }
                    FloorArea temp=Utilities.getFloorAreaFromString(floorArea.trim(), comment);
                    if (temp==null)
                        return;
                    if (baoList.searchNode(new BaoNode<>(temp))!=null)
                    {
                        comment.setText("This floor area already exists!");
                        return;
                    }
                    baoList.addNode(new BaoNode<>(temp));
                    floorAreaTable.getItems().add(temp);
                    handledLength += floorArea.length() + 1;
                }
            }
            else if (pos.getContent() instanceof Goods) {
                comment.setText("You're in the search view! Go back to the table view to edit it!");
                return;
            }
            else if (pos.getContent() instanceof FloorArea) {
                String aisle;
                while (handledLength < input.length()) {
                    aisle = Utilities.extractElement(input.substring(handledLength));
                    if (Utilities.checkStringNotFitType(aisle, "aisle"))
                    {
                        comment.setText("Your formatting is off. Check your ',' and ';' count?");
                        return;
                    }
                    Aisle temp=Utilities.getAisleFromString(aisle.trim(), comment);
                    if (temp==null)
                        return;
                    BaoList tmpList=((Components)(findNodeByPath(currentPath, pos).getContent())).getInnerList();
                    if (fullSearch(new BaoNode<>(temp))!=null)
                    {
                        comment.setText("This aisle already exists!");
                        return;
                    }
                    tmpList.addNode(new BaoNode<>(temp));
                    aisleTable.getItems().add(temp);
                    handledLength += aisle.length() + 1;
                }
            }
            else if (pos.getContent() instanceof Aisle) {
                String shelf;
                while (handledLength < input.length()) {
                    shelf = Utilities.extractElement(input.substring(handledLength));
                    if (Utilities.checkStringNotFitType(shelf, "shelf"))
                    {
                        comment.setText("Your formatting is off. Check your ',' and ';' count?");
                        return;
                    }
                    Shelf temp=Utilities.getShelfFromString(shelf, comment);
                    if (temp==null)
                        return;
                    BaoList tmpList=((Components)(findNodeByPath(currentPath, pos).getContent())).getInnerList();
                    if (tmpList.searchNode(new BaoNode<>(temp))!=null)
                    {
                        comment.setText("This shelf already exists!");
                        return;
                    }
                    tmpList.addNode(new BaoNode<>(temp));
                    shelfTable.getItems().add(temp);
                    handledLength += shelf.length() + 1;
                }
            }
            else if (pos.getContent() instanceof Shelf)
            {
                String goods;
                while (handledLength < input.length()) {
                    goods = Utilities.extractElement(input.substring(handledLength));
                    if (Utilities.checkStringNotFitType(goods, "goods"))
                    {
                        comment.setText("Your formatting is off. Check your ',' and ';' count?");
                        return;
                    }
                    Goods temp=Utilities.getGoodFromString(goods, comment);
                    if (temp==null)
                        return;
                    BaoList tmpList=((Components)(findNodeByPath(currentPath, pos).getContent())).getInnerList();
                    BaoNode tmp=tmpList.searchNode(new BaoNode<>(temp));
                    if (tmp!=null) {
                        ((Goods) tmp.getContent()).setQuantity(((Goods) tmp.getContent()).getQuantity() + temp.getQuantity());
                        goodsTable.getItems().set(goodsNo.getCellData((Goods) tmp.getContent())-1, (Goods) tmp.getContent()); //0 indexed
                    }
                    else {
                        tmpList.addNode(new BaoNode<>(temp));
                        goodsTable.getItems().add(temp);
                    }
                    handledLength += goods.length() + 1;
                }
            }
            comment.setText("No errors here.");
            if (rootField.getText().isEmpty())
                comment.setText("There's literally no input.");
        }
        else if (choice.equals("Smart Add"))
        {
            if (pos.getContent() instanceof Goods) {
                comment.setText("You're in the search view! Go back to the table view to edit it!");
                return;
            }
            String goods, location="Added to: ";
            while (handledLength < input.length()) {
                goods = Utilities.extractElement(input.substring(handledLength));
                if (Utilities.checkStringNotFitType(goods, "goods"))
                {
                    comment.setText("Your formatting is off. Check your ',' and ';' count?");
                    return;
                }
                Goods temp=Utilities.getGoodFromString(goods, comment);
                if (temp==null)
                    return;
                BaoNode place=fullSearch(new BaoNode<>(temp));
                if (place!=null)
                {
                    ((Goods)(place.getContent())).setQuantity(((Goods) place.getContent()).getQuantity() + temp.getQuantity());
                    goodsTable.getItems().set(goodsNo.getCellData((Goods) place.getContent())-1, (Goods) place.getContent()); //0 indexed
                    location+=((Components)place.getContent()).getName()+"; ";
                }
                else
                {
                    Components similar=similarSearch(new BaoNode<>(temp));
                    if (similar==null)
                    {
                        return;
                    }
                    else if (similar instanceof Goods)
                    {
                        comment.setText("There's no place to put this yet. Add floor areas, aisles, and shelves first!");
                        return;
                    }
                    else {
                        similar.getInnerList().addNode(new BaoNode<>(temp));
                        goodsTable.getItems().add(temp); //0 indexed
                        location+=similar.getName()+"; ";
                    }
                }
                handledLength += goods.length() + 1;
            }
            comment.setText(location.substring(0, location.length()-2));
            if (rootField.getText().isEmpty())
                comment.setText("There's literally no input.");
        }
        else if (choice.equals("Search"))
        {
            String search=rootField.getText();
            int count=1;
            BaoList <String> list=new BaoList<>();
            for (FloorArea floorArea: baoList)
            {
                for (Aisle aisle: floorArea.getInnerList())
                {
                    for (Shelf shelf: aisle.getInnerList())
                    {
                        for (Goods goods: shelf.getInnerList())
                        {
                            if (goods.getName().equals(search))
                            {
                                list.addNode(new BaoNode<>("Found occurrence "+count+" at:\nFloor Area "+floorArea.getName()+", Aisle "+aisle.getName()
                                +", Shelf "+shelf.getName()+"!"));
                                count++;
                            }
                        }
                    }
                }
            }
            moveToTable(floorAreaTable, floorAreaTable);
            moveToTable(aisleTable, aisleTable);
            moveToTable(shelfTable, shelfTable);
            moveToTable(goodsTable, goodsTable);
            searchView.getItems().clear();
            for (String s: list)
                searchView.getItems().add(s);
            VBox.setVgrow(searchView, Priority.ALWAYS);
            searchView.setPrefHeight(100);
            searchView.setPrefWidth(100);
            comment.setText("Found "+(count-1)+" occurrences!");
            pos=new BaoNode<>(new Goods("", "", 0, 0, 0, 0, ""));
        }
        else if (choice.equals("Reset")) {
            if (rootField.getText().equals("I am sure")) {
                baoList.clear();
                floorAreaTable.getItems().clear();
                aisleTable.getItems().clear();
                shelfTable.getItems().clear();
                goodsTable.getItems().clear();
                pos = null;
                currentPath.clear();
                moveToTable(aisleTable, floorAreaTable);
                moveToTable(shelfTable, floorAreaTable);
                moveToTable(goodsTable, floorAreaTable);
                label.setText("Bao's Hypermarket");
                comment.setText("Reset complete. Hope you know what you're doing.");
            } else
                comment.setText("This is a dangerous option! Type 'I am sure' to confirm.");
        }
        else if (choice.equals("Save"))
        {
            try {
                save();
                comment.setText("Saved to baoList.xml!");
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        else if (choice.equals("Load"))
        {
            try {
                load();
                floorAreaTable.getItems().clear();
                pos=null;
                currentPath.clear();
                moveToTable(aisleTable, floorAreaTable);
                moveToTable(shelfTable, floorAreaTable);
                moveToTable(goodsTable, floorAreaTable);
                for (FloorArea floorArea: baoList)
                    floorAreaTable.getItems().add(floorArea);
                label.setText("Bao's Hypermarket");
                comment.setText("Loaded from baoList.xml!");
            }
            catch (Exception e) {
                System.err.println("Error writing to file!");
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
            BaoNode tmp=Utilities.constructNode(((Components) tableView.getSelectionModel().getSelectedItem()).toRawString());
            Object replaced;
            if (tmp.getContent() instanceof FloorArea) {
                if (Utilities.checkStringNotFitType(Utilities.extractElement(rootField.getText()), "floorArea"))
                {
                    comment.setText("Your formatting is off. Check your ',' and ';' count?");
                    return;
                }
                replaced=Utilities.getFloorAreaFromString(Utilities.extractElement(rootField.getText()), comment);
            }
            else if (tmp.getContent() instanceof Aisle) {
                if (Utilities.checkStringNotFitType(Utilities.extractElement(rootField.getText()), "aisle"))
                {
                    comment.setText("Your formatting is off. Check your ',' and ';' count?");
                    return;
                }
                replaced = Utilities.getAisleFromString(Utilities.extractElement(rootField.getText()), comment);
            }
            else if (tmp.getContent() instanceof Shelf) {
                if (Utilities.checkStringNotFitType(Utilities.extractElement(rootField.getText()), "shelf")) {
                    comment.setText("Your formatting is off. Check your ',' and ';' count?");
                    return;
                }
                replaced = Utilities.getShelfFromString(Utilities.extractElement(rootField.getText()), comment);
            }
            else {
                if (Utilities.checkStringNotFitType(Utilities.extractElement(rootField.getText()), "goods"))
                {
                    comment.setText("Your formatting is off. Check your ',' and ';' count?");
                    return;
                }
                replaced = Utilities.getGoodFromString(Utilities.extractElement(rootField.getText()), comment);
            }
            if (replaced==null)
                return;
            if (currentPath.getSize()>=1) {
                ((Components) replaced).setInnerList(((Components)(((Components) findNodeByPath(currentPath, currentPath.getNode(currentPath.getSize()-1)).getContent()).getInnerList().searchNode(tmp).getContent())).getInnerList());
                ((BaoNode<Components>) (findNodeByPath(currentPath, currentPath.getNode(currentPath.getSize()-1)))).getContent().getInnerList().searchNode(tmp).setContent(replaced);
            }
            else {
                ((Components) replaced).setInnerList(((Components) baoList.searchNode(tmp).getContent()).getInnerList());
                baoList.searchNode(tmp).setContent(replaced);
            }
            ((TableView<Object>)(tableView)).getItems().set(((TableColumn <Components, Integer>)(noColumn)).getCellData((Components) tmp.getContent())-1, replaced); //0 indexed
            if (rootField.getText().isEmpty())
                comment.setText("There's literally no input");
            else
                comment.setText("No errors here. Edit only takes the first input in the field.");
        });
        return edit;
    }

    private MenuItem createDeleteMenuItem(TableView <?> tableView)
    {
        MenuItem delete=new MenuItem("Delete");
        delete.setOnAction(event -> {
            if (tableView.getSelectionModel().getSelectedItem()==null)
                return;
            BaoNode tmp=Utilities.constructNode(((Components) tableView.getSelectionModel().getSelectedItem()).toRawString());
            if (tmp.getContent() instanceof FloorArea)
                baoList.removeNode(tmp);
            else if (tmp.getContent() instanceof Aisle)
                ((FloorArea)(findNodeByPath(currentPath, currentPath.getNode(0)).getContent())).getInnerList().removeNode(tmp);
            else if (tmp.getContent() instanceof Shelf)
                ((Aisle)(findNodeByPath(currentPath, currentPath.getNode(1)).getContent())).getInnerList().removeNode(tmp);
            else {
                BaoNode <Goods> node=((Shelf) (findNodeByPath(currentPath, currentPath.getNode(2)).getContent())).getInnerList().searchNode(tmp);
                if (Utilities.checkStringInvalidInteger(rootField.getText(), comment))
                    ((Shelf) (findNodeByPath(currentPath, currentPath.getNode(2)).getContent())).getInnerList().removeNode(tmp);
                else
                {
                    int amount=Integer.parseInt(rootField.getText().trim());
                    if (amount>=node.getContent().getQuantity())
                        ((Shelf) (findNodeByPath(currentPath, currentPath.getNode(2)).getContent())).getInnerList().removeNode(tmp);
                    else {
                        node.getContent().setQuantity(node.getContent().getQuantity() - amount);
                        Goods replaced=node.getContent();
                        ((TableView<Object>)(tableView)).getItems().set((goodsNo).getCellData(replaced)-1, replaced); //0 indexed
                    }
                }
            }
            if (!(tmp.getContent() instanceof Goods))
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
            moveToTable(oldTableView, newTableView);

            pos=Utilities.constructNode(((Components) oldTableView.getSelectionModel().getSelectedItem()).toRawString());
            if (pos!=null) {
                currentPath.addNode(pos);
                BaoNode tmp=findNodeByPath(currentPath, pos);
                currentList=Utilities.copyList(((Components)(tmp.getContent())).getInnerList());

                if (pos.getContent() instanceof FloorArea)
                    moveToFloorArea(((FloorArea)pos.getContent()).getName());
                else if (pos.getContent() instanceof Aisle)
                    moveToAisle(((Aisle)pos.getContent()).getName());
                else if (pos.getContent() instanceof Shelf)
                    moveToShelf(String.valueOf(((Shelf)pos.getContent()).getNumber()));
                setInstructions();
            }
        });
        return visit;
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

    private void goingBack(TableView <?> oldTableView, TableView <?> newTableView, int position)
    {
        currentList.clear();
        for (Object object: ((Components)(findNodeByPath(currentPath.subList(position), currentPath.getNode(position)).getContent())).getInnerList())
            currentList.addNode(new BaoNode<>(object));
        moveToTable(oldTableView, newTableView);
    }

    @FXML
    private void backAction(ActionEvent event) {
        if (pos==null) {
            mainApp.switchScene();
            return;
        }
        boolean isSearch=false;
        if (pos.getContent() instanceof FloorArea) {
            pos=null;
            label.setText("Bao's Hypermarket");
            floorAreaTable.getItems().clear();
            for (FloorArea floorArea : baoList)
                floorAreaTable.getItems().add(floorArea);
            moveToTable(aisleTable, floorAreaTable);
        }
        else if (pos.getContent() instanceof Aisle) {
            pos=new BaoNode(currentPath.getNode(0).getContent());

            goingBack(shelfTable, aisleTable, 0);
            moveToFloorArea(((FloorArea)pos.getContent()).getName());
        }
        else if (pos.getContent() instanceof Shelf)
        {
            pos=new BaoNode(currentPath.getNode(1).getContent());

            goingBack(goodsTable, shelfTable, 1);
            moveToAisle(((Aisle)pos.getContent()).getName());
        }
        else if (pos.getContent() instanceof Goods)
        {
            goBackFromSearch();
            isSearch=true;
        }
        if (!isSearch)
            currentPath.removeNode(currentPath.getNode(currentPath.getSize()-1));
    }

    private void goBackFromSearch()
    {
        VBox.setVgrow(searchView, Priority.NEVER);
        searchView.setPrefHeight(0);
        searchView.setPrefWidth(0);
        if (currentPath.getSize()==0) {
            pos=null;
            label.setText("Bao's Hypermarket");
            floorAreaTable.getItems().clear();
            for (FloorArea floorArea : baoList)
                floorAreaTable.getItems().add(floorArea);
            moveToTable(aisleTable, floorAreaTable);
        }
        else {
            pos=new BaoNode(currentPath.getNode(currentPath.getSize() - 1).getContent());
            if (pos.getContent() instanceof FloorArea) {
                pos=new BaoNode(currentPath.getNode(0).getContent());

                goingBack(shelfTable, aisleTable, 0);
                moveToFloorArea(((FloorArea)pos.getContent()).getName());
            }
            else if (pos.getContent() instanceof Aisle) {
                pos=new BaoNode(currentPath.getNode(1).getContent());

                goingBack(goodsTable, shelfTable, 1);
                moveToAisle(((Aisle)pos.getContent()).getName());
            }
            else if (pos.getContent() instanceof Shelf) {
                pos=new BaoNode(currentPath.getNode(2).getContent());

                goingBack(floorAreaTable, goodsTable, 2); //Any table aside from the current one
                moveToShelf(((Shelf)pos.getContent()).getName());
            }
        }
    }

    ///UTILITIES
    public void save() throws Exception {
        XStream xstream=new XStream(new DomDriver());
        ObjectOutputStream out=xstream.createObjectOutputStream(new FileWriter("baoList.xml"));
        out.writeObject(baoList);
        out.close();
    }
    public void load() throws Exception {
        Class<?>[] classes = new Class[] { BaoNode.class, BaoList.class, Components.class, Aisle.class, Shelf.class, FloorArea.class, Goods.class};

        //setting up the xstream object with default security and the above classes
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        //doing the actual serialisation to an XML file
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader("baoList.xml"));
        baoList = (BaoList<FloorArea>) in.readObject();
        in.close();
    }
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

    private void moveToTable(TableView oldTable, TableView newTable)
    {
        VBox.setVgrow(newTable, Priority.ALWAYS);
        newTable.setPrefWidth(100);
        newTable.setPrefHeight(100);

        VBox.setVgrow(oldTable, Priority.NEVER);
        oldTable.setPrefWidth(0);
        oldTable.setPrefHeight(0);
    }

    private Components similarSearch(BaoNode <?> node)
    {
        if (node.getDepth()==0)
            return null;
        BaoNode cmpNode = new BaoNode(null);
        Components retNode=new Goods("", "", 0, 0, 0, 0, "");
        double similarScore=Double.MAX_VALUE;
        for (FloorArea floorArea: baoList)
        {
            if (node.getDepth()==1)
            {
                BaoNode <Aisle> tmp=floorArea.getInnerList().similarNode((BaoNode<Aisle>) node);
                if (tmp==null) {
                    retNode=floorArea;
                    continue;
                }
                if (tmp.getContent().similarScore((Components) cmpNode.getContent())<similarScore)
                {
                    similarScore=tmp.getContent().similarScore((Components) cmpNode.getContent());
                    cmpNode=tmp;
                    retNode=floorArea;
                }
                continue;
            }
            for (Aisle aisle: floorArea.getInnerList())
            {
                if (node.getDepth()==2)
                {
                    BaoNode <Shelf> tmp=aisle.getInnerList().similarNode((BaoNode<Shelf>) node);
                    if (tmp==null) {
                        retNode=aisle;
                        continue;
                    }
                    if (tmp.getContent().similarScore((Components) cmpNode.getContent())<similarScore)
                    {
                        similarScore=tmp.getContent().similarScore((Components) cmpNode.getContent());
                        cmpNode=tmp;
                        retNode=aisle;
                    }
                    continue;
                }
                for (Shelf shelf: aisle.getInnerList())
                {
                    BaoNode <Goods> tmp=shelf.getInnerList().searchNode((BaoNode<Goods>) node);
                    if (tmp==null)
                    {
                        retNode=shelf;
                        continue;
                    }
                    if (tmp.getContent().similarScore((Components) cmpNode.getContent())<similarScore)
                    {
                        similarScore=tmp.getContent().similarScore((Components) cmpNode.getContent());
                        cmpNode=tmp;
                        retNode=shelf;
                    }
                }
            }
        }
        return retNode;
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
