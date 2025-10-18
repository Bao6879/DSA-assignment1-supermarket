package main;

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

public class ModelViewController {

    //Back end
    public BaoList<FloorArea> baoList=new BaoList<>();
    private BaoList currentPath=new BaoList();
    private BaoNode considering;
    private MainApplication mainApp;
    private boolean inSearch=false;

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
    public TextField smartAddName, smartAddDescription, smartAddWeight, smartAddPrice, smartAddQuantity, smartAddTemperature, smartAddImage;
    public Button smartAddButton;

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
    }

    public void populateTree(){
        TreeItem<String> root = new TreeItem<>("Hypermarket");
        root.setExpanded(true);
        for (FloorArea area : baoList){
            TreeItem<String> floorItem = new TreeItem<>(area.getName());
            if (currentPath.getSize()>=1 && area.equals(currentPath.getNode(0).getContent()))
                floorItem.setExpanded(true);
            for (Aisle aisle: area.getInnerList())
            {
                TreeItem<String> aisleItem = new TreeItem<>(aisle.getName());
                if (currentPath.getSize()>=2 && aisle.equals(currentPath.getNode(1).getContent()))
                    aisleItem.setExpanded(true);
                for (Shelf shelf: aisle.getInnerList())
                {
                    TreeItem<String> shelfItem = new TreeItem<>(shelf.getNumber()+"");
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

    ///Action
    public void visit()
    {
        if (currentPath.getSize()==0)
        {
            currentPath.addNode(considering);
            showAisles();
        }
        else if (currentPath.getSize()==1)
        {
            currentPath.addNode(considering);
            showShelves();
        }
        else if (currentPath.getSize()==2)
        {
            currentPath.addNode(considering);
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
                if (((Goods) temp.getContent()).getQuantity() <Integer.parseInt(count)) {
                    ((Components) findNodeByPath(currentPath, currentPath.getNode(2)).getContent()).getInnerList().removeNode(considering);
                    comment.setText("Deletion Successful!");
                }
                else {
                    ((Goods) temp.getContent()).setQuantity(Integer.parseInt(count) + ((Goods) temp.getContent()).getQuantity());
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
        if (!inSearch)
            currentPath.removeNode(currentPath.getNode(currentPath.getSize()-1));
        else {
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

    public void smartAdd()
    {

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
    public void draw()
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        switch (currentPath.getSize())
        {
            case 0 -> drawFloorAreas();
            case 1 -> drawAisles();
            case 2 -> drawShelves();
            case 3 -> drawGoods();
        }
    }

    private void drawFloorAreas()
    {
        int xPos=20, yPos=20;
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
                xPos=20;
                yPos+=floorAreaSize+20;
            }
            //TODO: compute multi line names
        }
    }

    private void drawAisles()
    {
        int xPos=20, yPos=20;
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
                xPos=20;
                yPos+=aisleBoxWidth+20;
            }
            //TODO: compute multi line names
        }
    }

    private void drawShelves()
    {
        int xPos=20, yPos=20;
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
                xPos=20;
                yPos+=shelfWidth+20;
            }
            //TODO: compute multi line names
        }
    }

    private void drawGoods()
    {
        int xPos=20, yPos=20;
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
                xPos=20;
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
}
