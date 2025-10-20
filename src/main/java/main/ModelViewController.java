package main;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import main.supermarketComponents.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

public class ModelViewController {

    //Back end
    public BaoList<FloorArea> baoList=new BaoList<>();
    private BaoList currentPath=new BaoList(), newPath=new BaoList();
    private BaoNode considering;
    private MainApplication mainApp;
    private boolean inSearch=false, animating=false;
    private AnimationTimer currentAnimation=null;

    //General
    public TreeView <String> treeView=new TreeView<>();
    public Canvas canvas;
    public GraphicsContext gc;
    public Label comment;
    public ScrollPane canvasPane;
    private final int floorAreaSize=100, aisleBoxWidth=70, aisleBoxLength=150, shelfWidth=150, shelfLength=70, goodsSize=50;

    //Action
    public AnchorPane action, deletePane;
    public Button visit, delete, edit, info;
    public Label actionLabel;
    public TextField deleteCount;

    //Search
    public AnchorPane search, searchResult;
    public TextField searchField;
    public Button searchButton;
    public ListView <String> listView;

    //Smart Add
    public AnchorPane smartAdd;
    public TextField smartAddName, smartAddDescription, smartAddWeight, smartAddPrice, smartAddQuantity, smartAddImage;
    public ChoiceBox <String> smartAddTemperature;

    //Floor Area
    public AnchorPane floorAreaAdd;
    public TextField floorAreaName, floorAreaLevel;

    //Aisle
    public AnchorPane aisleAdd;
    public TextField aisleName, aisleLength, aisleWidth;
    public ChoiceBox <String> aisleTemperature;

    //Shelf
    public AnchorPane shelfAdd;
    public TextField shelfNumber;

    //Goods
    public AnchorPane goodsAdd;
    public TextField goodsName, goodsDescription, goodsWeight, goodsPrice, goodsQuantity, goodsImage;
    public ChoiceBox <String> goodsTemperature;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize(){
        gc=canvas.getGraphicsContext2D();
        aisleTemperature.getItems().addAll("Unrefrigerated", "Refrigerated", "Frozen");
        aisleTemperature.getSelectionModel().selectFirst();

        goodsTemperature.getItems().addAll("Unrefrigerated", "Refrigerated", "Frozen");
        goodsTemperature.getSelectionModel().selectFirst();

        smartAddTemperature.getItems().addAll("Unrefrigerated", "Refrigerated", "Frozen");
        smartAddTemperature.getSelectionModel().selectFirst();

        treeView.setOnMouseClicked(event -> {
            BaoList <String> clicked=new BaoList();
            TreeItem <String> selected=treeView.getSelectionModel().getSelectedItem();
            if (selected==null)
                return;
            TreeItem <String> current=selected;
            while (current!=null) {
                String value=current.getValue();
                if (current.getValue().startsWith("Floor area: "))
                    value=value.substring(12);
                else if (current.getValue().startsWith("Aisle: "))
                    value=value.substring(7);
                else if (current.getValue().startsWith("Shelf: "))
                    value=value.substring(7);
                clicked.addNode(value);
                current=current.getParent();
            }
            String source="";
            switch (currentPath.getSize())
            {
                case 0 -> source="floorArea";
                case 1 -> source="aisle";
                case 2 -> source="shelf";
                case 3 -> source="goods";
            }
            newPath.clear();
            if (clicked.getSize()==1)
            {
                drawTransition(source, "floorArea", "change");
            }
            else if (clicked.getSize()==2)
            {
                newPath.addNode(baoList.searchNode(new FloorArea(clicked.getNode(0).getContent(), 1)));
                drawTransition(source, "aisle", "change");
            }
            else if (clicked.getSize()==3)
            {
                FloorArea tmp=baoList.searchNode(new FloorArea(clicked.getNode(1).getContent(), 1)).getContent();
                newPath.addNode(tmp);
                newPath.addNode(tmp.getInnerList().searchNode(new Aisle(clicked.getNode(0).getContent(), 1, 1, "")));
                drawTransition(source, "shelf", "change");
            }
            else
            {
                FloorArea tmp=baoList.searchNode(new FloorArea(clicked.getNode(clicked.getSize()-2).getContent(), 1)).getContent();
                newPath.addNode(tmp);
                Aisle temp=tmp.getInnerList().searchNode(new Aisle(clicked.getNode(clicked.getSize()-3).getContent(), 1, 1, "")).getContent();
                newPath.addNode(temp);
                newPath.addNode(temp.getInnerList().searchNode(new Shelf(Integer.parseInt(clicked.getNode(clicked.getSize()-4).getContent()))).getContent());
                drawTransition(source, "shelf", "change");
            }
        });
    }

    public void populateTree(){
        TreeItem<String> root = new TreeItem<>("Hypermarket");
        root.setExpanded(true);
        for (FloorArea area : baoList){
            TreeItem<String> floorItem = new TreeItem<>("Floor area: "+area.getName());
            if (currentPath.getSize()>=1 && area.equals(currentPath.getNode(0).getContent()))
                floorItem.setExpanded(true);
            for (Aisle aisle: area.getInnerList())
            {
                TreeItem<String> aisleItem = new TreeItem<>("Aisle: "+aisle.getName());
                if (currentPath.getSize()>=2 && aisle.equals(currentPath.getNode(1).getContent()))
                    aisleItem.setExpanded(true);
                for (Shelf shelf: aisle.getInnerList())
                {
                    TreeItem<String> shelfItem = new TreeItem<>("Shelf: "+shelf.getNumber());
                    if (currentPath.getSize()>=3 && shelf.equals(currentPath.getNode(2).getContent()))
                        shelfItem.setExpanded(true);
                    for (Goods goods: shelf.getInnerList())
                    {
                        TreeItem<String> goodsItem = new TreeItem<>(goods.getName());
                        shelfItem.getChildren().add(goodsItem);
                    }
                    aisleItem.getChildren().add(shelfItem);
                }
                floorItem.getChildren().add(aisleItem);
            }
            root.getChildren().add(floorItem);
        }
        treeView.setRoot(root);
        draw();
    }

    ///Adding
    public void addFloorArea()
    {
        FloorArea tmp=getFloorAreaFromInput();
        if (tmp!=null)
        {
            if (baoList.searchNode(new BaoNode<>(tmp))!=null)
            {
                comment.setText("This floor area already exists!");
                return;
            }
            baoList.addNode(new BaoNode<>(tmp));
            comment.setText("Floor area added successfully!");
            populateTree();
        }
    }
    public void addAisle()
    {
        Aisle tmp=getAisleFromInput();
        if (tmp!=null)
        {
            if (fullSearch(new BaoNode<>(tmp))!=null)
            {
                comment.setText("This aisle already exists!");
                return;
            }
            ((Components)findNodeByPath(currentPath, currentPath.getNode(0)).getContent()).getInnerList().addNode(new BaoNode<>(tmp));
            comment.setText("Aisle added successfully!");
            populateTree();
        }
    }
    public void addShelf()
    {
        Shelf tmp=getShelfFromInput();
        if (tmp!=null)
        {
            BaoNode temp=currentPath.getNode(1);
            if (((Components)temp.getContent()).getInnerList().searchNode(new BaoNode<>(tmp))!=null)
            {
                comment.setText("This shelf already exists!");
                return;
            }
            ((Components) temp.getContent()).getInnerList().addNode(new BaoNode<>(tmp));
            comment.setText("Shelf added successfully!");
            populateTree();
        }
    }
    public void addGoods()
    {
        Goods tmp=getGoodsFromInput();
        if (tmp!=null)
        {
            BaoNode temp=currentPath.getNode(2);
            if (((Components)temp.getContent()).getInnerList().searchNode(new BaoNode<>(tmp))!=null)
            {
                ((Goods)((Components)temp.getContent()).getInnerList().searchNode(new BaoNode<>(tmp)).getContent()).setQuantity(tmp.getQuantity()+((Goods)((Components)temp.getContent()).getInnerList().searchNode(new BaoNode<>(tmp)).getContent()).getQuantity());
                comment.setText("Added to existing goods!");
                populateTree();
                return;
            }
            ((Components) temp.getContent()).getInnerList().addNode(new BaoNode<>(tmp));
            comment.setText("Goods added successfully!");
            populateTree();
        }
    }
    ///Menu
    public void viewAll()
    {
        mainApp.viewAllController.baoList=baoList;
        mainApp.viewAllController.viewAll();
        mainApp.switchScene("view");
    }
    public void cli()
    {
        mainApp.cliController.baoList=baoList;
        mainApp.switchScene("cli");
    }

    ///Action
    public void visit()
    {
        if (currentPath.getSize()==0)
        {
            currentPath.addNode(considering);
            drawTransition("floorArea", "aisle", "");
            showAisles();
        }
        else if (currentPath.getSize()==1)
        {
            currentPath.addNode(considering);
            drawTransition("aisle", "shelf", "");
            showShelves();
        }
        else if (currentPath.getSize()==2)
        {
            currentPath.addNode(considering);
            drawTransition("shelf", "goods", "");
            showGoods();
        }
        else if (currentPath.getSize()==3)
        {
            comment.setText("You can't visit a good item, unfortunately.");
        }
        populateTree();
    }

    public void delete()
    {
        if (currentPath.getSize()==0)
            baoList.removeNode(considering);
        else if (currentPath.getSize()!=3)
            ((Components)findNodeByPath(currentPath, currentPath.getNode(currentPath.getSize()-1)).getContent()).getInnerList().removeNode(considering);
        else
        {
            BaoNode temp=((Components)findNodeByPath(currentPath, currentPath.getNode(2)).getContent()).getInnerList().searchNode(considering);
            String count=deleteCount.getText().trim();
            if (!Utilities.checkStringInvalidInteger(count, comment)) {
                if (((Goods) temp.getContent()).getQuantity()<=Integer.parseInt(count)) {
                    ((Components) findNodeByPath(currentPath, currentPath.getNode(2)).getContent()).getInnerList().removeNode(considering);
                    comment.setText("Deletion Successful!");
                }
                else {
                    ((Goods) temp.getContent()).setQuantity(Integer.parseInt(count) - ((Goods) temp.getContent()).getQuantity());
                    comment.setText("Removed "+count+" items!");
                }
            }
        }
        if (currentPath.getSize()!=3)
            comment.setText("Deletion Successful!");
        populateTree();
    }

    public void edit()
    {
        switch (currentPath.getSize())
        {
            case 0 -> baoList.searchNode(considering).setContent(getFloorAreaFromInput());
            case 1 -> ((Components)findNodeByPath(currentPath, currentPath.getNode(0)).getContent()).getInnerList().searchNode(new BaoNode<>(considering)).setContent(getAisleFromInput());
            case 2 -> ((Components)findNodeByPath(currentPath, currentPath.getNode(1)).getContent()).getInnerList().searchNode(new BaoNode<>(considering)).setContent(getShelfFromInput());
            case 3 -> ((Components)findNodeByPath(currentPath, currentPath.getNode(2)).getContent()).getInnerList().searchNode(new BaoNode<>(considering)).setContent(getGoodsFromInput());
        }
        comment.setText("Edit successful! If nothing changed, you need to change the content from the add tab!");
        populateTree();
    }

    public void info()
    {
        //TODO DO I NEED TO DO THIS
    }

    public void back()
    {
        if (currentPath.getSize()==0) {
            comment.setText("There is no higher place to go to.");
            return;
        }
        if (!inSearch) {
            switch (currentPath.getSize()) {
                case 1 -> drawTransition("aisle", "floorArea", "delete");
                case 2 -> drawTransition("shelf", "aisle", "delete");
                case 3 -> drawTransition("goods", "aisle", "delete");
            }
        }
    }

    public void addView()
    {
        hideAllPanes();
        if (inSearch) {
            inSearch = false;
            searchResult.setDisable(true);
            searchResult.setVisible(false);
            canvasPane.setDisable(false);
            canvasPane.setVisible(true);
        }
        switch (currentPath.getSize())
        {
            case 0 -> showFloorAreas();
            case 1 -> showAisles();
            case 2 -> showShelves();
            case 3 -> showGoods();
        }
        populateTree();
    }

    public void searchView()
    {
        hideAllPanes();
        search.setVisible(true);
    }

    public void smartAddView()
    {
        hideAllPanes();
        smartAdd.setVisible(true);
    }

    public void searchGood()
    {
        String name=searchField.getText().strip();
        int count=1;
        BaoList <String> list=new BaoList<>();
        for (FloorArea floorArea: baoList) {
            for (Aisle aisle : floorArea.getInnerList()) {
                for (Shelf shelf : aisle.getInnerList()) {
                    for (Goods goods : shelf.getInnerList()) {
                        if (goods.getName().equals(name)) {
                            list.addNode(new BaoNode<>("Found occurrence " + count + " at:\nFloor Area " + floorArea.getName() + ", Aisle " + aisle.getName()
                                    + ", Shelf " + shelf.getName() + "!"));
                            count++;
                        }
                    }
                }
            }
        }
        canvasPane.setVisible(false);
        canvasPane.setDisable(true);
        searchResult.setDisable(false);
        searchResult.setVisible(true);
        listView.setVisible(true);
        listView.setDisable(false);
        listView.getItems().clear();
        for (String s: list)
            listView.getItems().add(s);
        comment.setText("Found "+(count-1)+" occurrences! Click add or back to go back!");
        inSearch=true;
    }

    public void saveData()
    {
        try {
            save();
            comment.setText("Saved to baoList.xml!");
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadData()
    {
        try {
            load();
            currentPath.clear();
            hideAllPanes();
            showFloorAreas();
            populateTree();
            comment.setText("Loaded from baoList.xml!");
        }
        catch (Exception e) {
            System.err.println("Error reading from file!");
        }
    }

    public void resetData()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Reset");
        alert.setHeaderText("Reset All Data");
        alert.setContentText("Are you sure you want to reset all data? This action cannot be undone.");

        Optional<ButtonType> result=alert.showAndWait();

        if (result.isPresent() && result.get()==ButtonType.OK) {
            baoList.clear();
            currentPath.clear();
            hideAllPanes();
            showFloorAreas();
            populateTree();
            comment.setText("System reset successfully. All data cleared.");
        } else {
            comment.setText("Reset cancelled.");
        }
    }

    public void smartAdd()
    {
        String name=smartAddName.getText().trim(), description=smartAddDescription.getText().trim(), weight=smartAddWeight.getText().trim(), price=smartAddPrice.getText().trim(), quantity=smartAddQuantity.getText().trim(), image=smartAddImage.getText().trim();
        String temperature=smartAddTemperature.getSelectionModel().getSelectedItem();
        if (!Utilities.checkStringInvalidDouble(weight, comment) && !Utilities.checkStringInvalidDouble(price, comment) && !Utilities.checkStringInvalidInteger(quantity, comment))
        {
            Goods item=new Goods(name, description, Double.parseDouble(weight), Double.parseDouble(price), Integer.parseInt(quantity), temperature, image);
            if (fullSearch(new BaoNode<>(item))!=null)
            {
                comment.setText("Added to existing item!");
                ((Goods)fullSearch(new BaoNode<>(item)).getContent()).setQuantity(Integer.parseInt(quantity)+((Goods)fullSearch(new BaoNode<>(item)).getContent()).getQuantity());
                return;
            }
            BaoList defaultPlacement=new BaoList<>();
            for (FloorArea floorArea: baoList) {
                for (Aisle aisle : floorArea.getInnerList()) {
                    for (Shelf shelf : aisle.getInnerList()) {
                        defaultPlacement.addNode(new BaoNode<>(floorArea));
                        defaultPlacement.addNode(new BaoNode<>(aisle));
                        defaultPlacement.addNode(new BaoNode<>(shelf));
                        break;
                    }
                    if (defaultPlacement.getSize()!=0)
                        break;
                }
                if (defaultPlacement.getSize()!=0)
                    break;
            }
            if (defaultPlacement.getSize()==0)
            {
                comment.setText("No place to put item! Add floor areas, aisles, and shelves first!");
                return;
            }
            BaoList optimalPlacement=new BaoList<>();
            int maxNameMatches=-1, maxDescriptionMatches=-1;
            double minPriceDifference=Double.MAX_VALUE, minWeightDifference=Double.MAX_VALUE;
            Utilities.createSkippedWords();
            for (FloorArea area: baoList) {
                for (Aisle aisle : area.getInnerList()) {
                    if (aisle.getTemperature().equals(temperature)) {
                         for (Shelf shelf : aisle.getInnerList()) {
                             for (Goods goods : shelf.getInnerList()) {
                                 String currentName=goods.getName(), currentDescription=goods.getDescription();
                                 BaoList <String> itemNameTokenized=tokenizeString(name), itemDescriptionTokenized=tokenizeString(description);
                                 BaoList <String> currentNameTokenized=tokenizeString(currentName), currentDescriptionTokenized=tokenizeString(currentDescription);
                                 int nameMatches=0, descriptionMatches=0;
                                 if (itemNameTokenized!=null && currentNameTokenized!=null) {
                                     for (String token : itemNameTokenized)
                                         if (currentNameTokenized.contains(token))
                                             nameMatches++;
                                     if (nameMatches > maxNameMatches)
                                     {
                                         maxNameMatches = nameMatches;
                                         optimalPlacement.clear();
                                         optimalPlacement.addNode(new BaoNode<>(area));
                                         optimalPlacement.addNode(new BaoNode<>(aisle));
                                         optimalPlacement.addNode(new BaoNode<>(shelf));
                                         break;
                                     }
                                     else if (nameMatches == maxNameMatches)
                                     {
                                         if (itemDescriptionTokenized!=null && currentDescriptionTokenized!=null) {
                                             for (String token: itemDescriptionTokenized)
                                                 if (currentDescriptionTokenized.contains(token))
                                                     descriptionMatches++;
                                         }
                                         if (descriptionMatches > maxDescriptionMatches)
                                         {
                                             maxDescriptionMatches = descriptionMatches;
                                             optimalPlacement.clear();
                                             optimalPlacement.addNode(new BaoNode<>(area));
                                             optimalPlacement.addNode(new BaoNode<>(aisle));
                                             optimalPlacement.addNode(new BaoNode<>(shelf));
                                             break;
                                         }
                                         else if (descriptionMatches == maxDescriptionMatches)
                                         {
                                             if (Math.abs(item.getPrice()-goods.getPrice())<minPriceDifference)
                                             {
                                                 minPriceDifference = Math.abs(item.getPrice()-goods.getPrice());
                                                 optimalPlacement.clear();
                                                 optimalPlacement.addNode(new BaoNode<>(area));
                                                 optimalPlacement.addNode(new BaoNode<>(aisle));
                                                 optimalPlacement.addNode(new BaoNode<>(shelf));
                                                 break;
                                             }
                                             else if (Math.abs(item.getPrice()-goods.getPrice())==minPriceDifference)
                                             {
                                                 if (Math.abs(item.getWeight()-goods.getWeight())<minWeightDifference)
                                                 {
                                                     minWeightDifference = Math.abs(item.getWeight()-goods.getWeight());
                                                     optimalPlacement.clear();
                                                     optimalPlacement.addNode(new BaoNode<>(area));
                                                     optimalPlacement.addNode(new BaoNode<>(aisle));
                                                     optimalPlacement.addNode(new BaoNode<>(shelf));
                                                     break;
                                                 }
                                             }
                                         }
                                     }
                                 }
                             }
                         }
                    }
                }
            }
            if (optimalPlacement.getSize()!=0)
            {
                ((Components)findNodeByPath(optimalPlacement, optimalPlacement.getNode(2)).getContent()).getInnerList().addNode(new BaoNode(item));
                comment.setText("Item has been added to shelf "+((Components)optimalPlacement.getNode(2).getContent()).getName()+" of aisle "+((Components)optimalPlacement.getNode(1).getContent()).getName()+" of floor area "+((Components)optimalPlacement.getNode(0).getContent()).getName()+"!");
            }
            else
            {
                ((Components)findNodeByPath(defaultPlacement, defaultPlacement.getNode(2)).getContent()).getInnerList().addNode(new BaoNode(item));
                comment.setText("Didn't find a suitable place, so item has been added to shelf "+((Components)defaultPlacement.getNode(2).getContent()).getName()+" of aisle "+((Components)defaultPlacement.getNode(1).getContent()).getName()+" of floor area "+((Components)defaultPlacement.getNode(0).getContent()).getName()+"!");
            }
            populateTree();
        }
    }

    private BaoList <String> tokenizeString(String s)
    {
        BaoList <String> tokens=new BaoList();
        String tmp;
        for (int i=0; i<s.length(); i++)
        {
            int j=i;
            tmp="";
            while (j<s.length() && s.charAt(j)!=' ')
            {
                tmp+=s.charAt(j);
                j++;
            }
            i=j;
            tmp=tmp.toLowerCase();
            if (!Utilities.skippedWords.contains(tmp) && !tokens.contains(tmp))
            {
                tokens.addNode(tmp);
            }
        }
        return tokens;
    }

    public void canvasPressed(MouseEvent event)
    {
        int mouseX=(int) event.getX(), mouseY=(int) event.getY();
        if (currentPath.getSize()==0)
        {
            int position=getPositionOfItem(mouseX, mouseY, floorAreaSize, floorAreaSize);
            if (position>=0 && baoList.getSize()>position)
            {
                considering=baoList.getNode(position);
                actionLabel.setText("What do you want to do with Floor Area "+((FloorArea)(considering.getContent())).getName()+"?");
                showActions();
            }
        }
        else if (currentPath.getSize()==1)
        {
            int position=getPositionOfItem(mouseX, mouseY, aisleBoxLength, aisleBoxWidth);
            if (position>=0  && ((Components)currentPath.getNode(0).getContent()).getInnerList().getSize()>position)
            {
                considering=((Components)currentPath.getNode(0).getContent()).getInnerList().getNode(position);
                actionLabel.setText("What do you want to do with Aisle "+((Aisle)(considering.getContent())).getName()+"?");
                showActions();
            }
        }
        else if (currentPath.getSize()==2)
        {
            int position=getPositionOfItem(mouseX, mouseY, shelfLength, shelfWidth);
            if (position>=0  && ((Components)currentPath.getNode(1).getContent()).getInnerList().getSize()>position)
            {
                considering=((Components)currentPath.getNode(1).getContent()).getInnerList().getNode(position);
                actionLabel.setText("What do you want to do with Shelf "+((Shelf)(considering.getContent())).getName()+"?");
                showActions();
            }
        }
        else
        {
            int position=getPositionOfItem(mouseX, mouseY, goodsSize, goodsSize);
            if (position>=0   && ((Components)currentPath.getNode(2).getContent()).getInnerList().getSize()>position)
            {
                considering=((Components)currentPath.getNode(2).getContent()).getInnerList().getNode(position);
                actionLabel.setText("What do you want to do with "+((Goods)(considering.getContent())).getName()+"?");
                showActions();
            }
        }
    }

    private int getPositionOfItem(int mouseX, int mouseY, int length, int width)
    {
        mouseX-=20;
        mouseY-=20;
        if (mouseX>=0 && mouseY>=0) {
            int remX = mouseX % (length + 20), remY = mouseY % (width + 20);
            int divX = mouseX / (length + 20), divY = mouseY / (width + 20);
            if (remX == 0)
                divX--;
            if (remY == 0)
                divY--;
            int xValue = mouseX - (divX * (length + 20)), yValue = mouseY - (divY * (width + 20));
            if (xValue <= length && yValue <= width) {
                int position=(int) (divY * (canvas.getWidth() - 20) / (length + 20) + divX);
                if (divY>=1)
                    position++;
                 return position;
            }
        }
        return -1;
    }

    ///Showing
    public void showActions()
    {
        hideAllPanes();
        action.setVisible(true);
    }
    public void showDelete()
    {
        hideAllPanes();
        deletePane.setVisible(true);
    }
    public void showFloorAreas()
    {
        comment.setText("You are in the Hypermarket!");
        hideAllPanes();
        floorAreaAdd.setVisible(true);
    }
    public void showAisles()
    {
        comment.setText("You are in Floor Area "+((Components)currentPath.getNode(0).getContent()).getName()+"!");
        hideAllPanes();
        aisleAdd.setVisible(true);
    }
    public void showShelves()
    {
        comment.setText("You are in Aisle "+((Components)currentPath.getNode(1).getContent()).getName()+"!");
        hideAllPanes();
        shelfAdd.setVisible(true);
    }
    public void showGoods()
    {
        comment.setText("You are in Shelf "+((Components)currentPath.getNode(2).getContent()).getName()+"!");
        hideAllPanes();
        goodsAdd.setVisible(true);
    }

    ///Drawing
    private void stopAnimation()
    {
        if (currentAnimation!=null)
            currentAnimation.stop();
        currentAnimation=null;
    }

    public void drawTransition(String source, String dest, String after)
    {
        stopAnimation();
        animating=true;
        if (after.equals("change"))
            if (newPath.getSize()>=currentPath.getSize())
                currentPath=Utilities.copyList(newPath);
        currentAnimation=new AnimationTimer() {
            private long startTime=0;

            @Override
            public void handle(long now) {
                if (startTime==0)
                    startTime=now;
                double elapsedSeconds = (now-startTime)/1e9;
                int sta= (int) ((canvas.getWidth()+20)*elapsedSeconds/0.25);
                boolean finishedSource = false;
                if (elapsedSeconds>=0.25)
                    finishedSource = true;
                if (elapsedSeconds>0.5) {
                    animating=false;
                    stop();
                    if (after.equals("delete"))
                    {
                        if (!inSearch)
                            currentPath.removeNode(currentPath.getNode(currentPath.getSize() - 1));
                        else {
                            inSearch = false;
                            searchResult.setDisable(true);
                            searchResult.setVisible(false);
                            canvasPane.setDisable(false);
                            canvasPane.setVisible(true);
                        }
                        switch (currentPath.getSize()) {
                            case 0 -> showFloorAreas();
                            case 1 -> showAisles();
                            case 2 -> showShelves();
                            case 3 -> showGoods();
                        }
                        populateTree();
                    }
                    else if (after.equals("change"))
                    {
                        currentPath=Utilities.copyList(newPath);
                        switch (currentPath.getSize())
                        {
                            case 0 -> showFloorAreas();
                            case 1 -> showAisles();
                            case 2 -> showShelves();
                            case 3 -> showGoods();
                        }
                        populateTree();
                    }
                    return;
                }
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                if (!finishedSource)
                    switch (source) {
                        case "floorArea" -> drawFloorAreas(20 - sta);
                        case "aisle" -> drawAisles(20 - sta);
                        case "shelf" -> drawShelves(20 - sta);
                        case "goods" -> drawGoods(20 - sta);
                    }
                else
                    switch (dest) {
                        case "floorArea" -> drawFloorAreas((int) (sta-2*canvas.getWidth()-20));
                        case "aisle" -> drawAisles((int) (sta-2*canvas.getWidth()-20));
                        case "shelf" -> drawShelves((int) (sta-2*canvas.getWidth()-20));
                        case "goods" -> drawGoods((int) (sta-2*canvas.getWidth()-20));
                    }
            }
        };
        currentAnimation.start();
    }

    public void draw()
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        switch (currentPath.getSize())
        {
            case 0 -> drawFloorAreas(20);
            case 1 -> drawAisles(20);
            case 2 -> drawShelves(20);
            case 3 -> drawGoods(20);
        }
    }

    private void drawFloorAreas(int xStart)
    {
        int xPos=xStart, yPos=20;
        gc.setTextAlign(TextAlignment.CENTER);
        for (FloorArea area : baoList)
        {
            gc.setFill(Color.BLACK);
            gc.fillRect(xPos, yPos, floorAreaSize, floorAreaSize);

            gc.setFill(Color.WHITE);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(area.getName(), xPos+ floorAreaSize/2, yPos+floorAreaSize/2);
            xPos+=floorAreaSize+20;
            if (xPos>=canvas.getWidth())
            {
                xPos=xStart;
                yPos+=floorAreaSize+20;
            }
            //TODO: compute multi line names
        }
    }

    private void drawAisles(int xStart)
    {
        int xPos=xStart, yPos=20;
        gc.setTextAlign(TextAlignment.CENTER);
        for (Object aisle: ((Components)currentPath.getNode(0).getContent()).getInnerList())
        {
            gc.setFill(Color.RED);
            gc.fillRect(xPos, yPos, aisleBoxLength, aisleBoxWidth);

            gc.setFill(Color.WHITE);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(((Aisle)aisle).getName(), xPos+aisleBoxLength/2, yPos+aisleBoxWidth/2);
            xPos+=aisleBoxLength+20;
            if (xPos>=canvas.getWidth())
            {
                xPos=xStart;
                yPos+=aisleBoxWidth+20;
            }
            //TODO: compute multi line names
        }
    }

    private void drawShelves(int xStart)
    {
        int xPos=xStart, yPos=20;
        gc.setTextAlign(TextAlignment.CENTER);
        for (Object shelf: ((Components)currentPath.getNode(1).getContent()).getInnerList())
        {
            gc.setFill(Color.BLUE);
            gc.fillRect(xPos, yPos, shelfLength, shelfWidth);

            gc.setFill(Color.WHITE);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(((Shelf)shelf).getName(), xPos+shelfLength/2, yPos+shelfWidth/2);
            xPos+=shelfLength+20;
            if (xPos>=canvas.getWidth())
            {
                xPos=xStart;
                yPos+=shelfWidth+20;
            }
            //TODO: compute multi line names
        }
    }

    private void drawGoods(int xStart)
    {
        int xPos=xStart, yPos=20;
        gc.setTextAlign(TextAlignment.CENTER);
        for (Object goods: ((Components)currentPath.getNode(2).getContent()).getInnerList())
        {
            gc.setFill(Color.GREEN);
            gc.fillRect(xPos, yPos, goodsSize, goodsSize);

            gc.setFill(Color.WHITE);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(((Goods)goods).getName(), xPos+goodsSize/2, yPos+goodsSize/2);
            xPos+=goodsSize+20;
            if (xPos>=canvas.getWidth())
            {
                xPos=xStart;
                yPos+=goodsSize+20;
            }
            //TODO: compute multi line names
        }
    }

    ///Utilities
    private void hideAllPanes()
    {
        floorAreaAdd.setVisible(false);
        aisleAdd.setVisible(false);
        shelfAdd.setVisible(false);
        goodsAdd.setVisible(false);
        smartAdd.setVisible(false);
        search.setVisible(false);
        action.setVisible(false);
        deletePane.setVisible(false);
    }
    public void checkKeyPressed(KeyEvent e)
    {
        if (e.getCode()==KeyCode.ENTER) {
            switch (currentPath.getSize())
            {
                case 0 -> addFloorArea();
                case 1 -> addAisle();
                case 2 -> addShelf();
                case 3 -> addGoods();
            }
        }
    }
    private FloorArea getFloorAreaFromInput()
    {
        String name=floorAreaName.getText().trim();
        String level=floorAreaLevel.getText().trim();
        if (Utilities.checkStringInvalidInteger(level, comment))
        {
            return null;
        }
        return new FloorArea(name, Integer.parseInt(level));
    }
    private Aisle getAisleFromInput()
    {
        String name=aisleName.getText().trim(), length=aisleLength.getText().trim(), width=aisleWidth.getText().trim();
        String temperature=aisleTemperature.getSelectionModel().getSelectedItem();
        if (!Utilities.checkStringInvalidDouble(length, comment) && !Utilities.checkStringInvalidDouble(width, comment))
        {
            return new Aisle(name, Double.parseDouble(length), Double.parseDouble(width), temperature);
        }
        return null;
    }
    private Shelf getShelfFromInput()
    {
        String number=shelfNumber.getText().trim();
        if (!Utilities.checkStringInvalidInteger(number, comment))
        {
            return new Shelf(Integer.parseInt(number));
        }
        return null;
    }
    private Goods getGoodsFromInput()
    {
        String name=goodsName.getText().trim(), description=goodsDescription.getText().trim(), weight=goodsWeight.getText().trim(), price=goodsPrice.getText().trim(), quantity=goodsQuantity.getText().trim(), image=goodsImage.getText().trim();
        String temperature=goodsTemperature.getSelectionModel().getSelectedItem();
        if (!Utilities.checkStringInvalidDouble(weight, comment) && !Utilities.checkStringInvalidDouble(price, comment) && !Utilities.checkStringInvalidInteger(quantity, comment))
        {
            return new Goods(name, description, Double.parseDouble(weight), Double.parseDouble(price), Integer.parseInt(quantity), temperature, image);
        }
        return null;
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
}
