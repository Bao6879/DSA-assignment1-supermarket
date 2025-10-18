package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import main.supermarketComponents.Aisle;
import main.supermarketComponents.FloorArea;
import main.supermarketComponents.Goods;
import main.supermarketComponents.Shelf;

import java.awt.event.ActionEvent;

public class ModelViewController {

    //Back end
    public BaoList<FloorArea> baoList=new BaoList<>();
    private BaoList currentPath=new BaoList();
    private BaoNode considering;
    private MainApplication mainApp;

    //General
    public TreeView <String> treeView=new TreeView<>();
    public Canvas canvas;
    public GraphicsContext gc;
    public Label comment;
    private final int floorAreaSize=100, aisleBoxWidth=70, aisleBoxLength=100, shelfWidth=100, shelfLength=70, goodsSize=50;

    //Action
    public AnchorPane action;
    public Button visit, delete, edit, info;
    public Label actionLabel;

    //Search
    public AnchorPane search;
    public TextField searchField;
    public Button searchButton;

    //Smart Add
    public AnchorPane smartAdd;
    public TextField smartAddName, smartAddDescription, smartAddWeight, smartAddPrice, smartAddQuantity, smartAddTemperature, smartAddImage;
    public Button smartAddButton;

    //Floor Area
    public AnchorPane floorAreaAdd;
    public TextField floorAreaName, floorAreaLevel;
    public Button floorAreaButton;

    //Aisle
    public AnchorPane aisleAdd;
    public TextField aisleName, aisleLength, aisleWidth;
    public ChoiceBox <String> aisleTemperature;
    public Button aisleButton;

    //Shelf
    public AnchorPane shelfAdd;
    public TextField shelfNumber;
    public Button shelfButton;

    //Goods
    public AnchorPane goodsAdd;
    public TextField goodsName, goodsDescription, goodsWeight, goodsPrice, goodsQuantity, goodsTemperature, goodsImage;
    public Button goodsButton;

    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize(){
        gc=canvas.getGraphicsContext2D();
    }

    public void populateTree(){
        TreeItem<String> root = new TreeItem<>("Hypermarket");
        root.setExpanded(true);
        for (FloorArea area : baoList){
            TreeItem<String> floorItem = new TreeItem<>(area.getName());
            for (Aisle aisle: area.getInnerList())
            {
                TreeItem<String> aisleItem = new TreeItem<>(aisle.getName());
                for (Shelf shelf: aisle.getInnerList())
                {
                    TreeItem<String> shelfItem = new TreeItem<>(shelf.getNumber()+"");
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
        String name=floorAreaName.getText().trim();
        String level=floorAreaLevel.getText().trim();
        if (!Utilities.checkStringInvalidInteger(level, comment))
        {
            FloorArea tmp=new FloorArea(name, Integer.parseInt(level));
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

    }

    public void addShelf()
    {

    }

    public void addGoods()
    {

    }

    ///Action
    public void visit()
    {
        if (currentPath.getSize()==0)
        {
            currentPath.addNode(considering);

        }
    }

    public void searchGood()
    {

    }

    public void smartAdd()
    {

    }

    public void canvasPressed(MouseEvent event)
    {
        int mouseX=(int) event.getX(), mouseY= (int) event.getY();
        if (currentPath.getSize()==0)
        {
            mouseX-=20;
            mouseY-=20;
            if (mouseX>=0 && mouseY>=0)
            {
                int remX=mouseX%(floorAreaSize+20), remY=mouseY%(floorAreaSize+20);
                int divX=mouseX/(floorAreaSize+20), divY=mouseY/(floorAreaSize+20);
                if (remX==0)
                    divX--;
                if (remY==0)
                    divY--;
                int xValue=mouseX-(divX*(floorAreaSize+20)), yValue=mouseY-(divY*(floorAreaSize+20));
                if (xValue<=floorAreaSize && yValue<=floorAreaSize)
                {
                    int position=(int) (divY*(canvas.getWidth()-20)/(floorAreaSize+20)+divX);
                    if (divY>=1)
                        position++;
                    considering=baoList.getNode(position);
                    actionLabel.setText("What do you want to do with Floor Area "+((FloorArea)(considering.getContent())).getName()+"?");
                    showActions();
                }
            }
        }
        else if (currentPath.getSize()==1)
        {

        }
        else if (currentPath.getSize()==2)
        {

        }
        else
        {

        }
    }

    public void showActions()
    {
        hideAllPanes();
        action.setVisible(true);
    }

    public void draw()
    {
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
            gc.fillText(area.getName(), xPos+ floorAreaSize /2, yPos+floorAreaSize/2);
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

    }

    private void drawShelves()
    {

    }

    private void drawGoods()
    {

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
    }
    public void checkKeyPressed(KeyEvent e)
    {
        if (e.getCode()==KeyCode.ENTER) {
            if (currentPath.getSize()==0)
                addFloorArea();
        }
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
}
