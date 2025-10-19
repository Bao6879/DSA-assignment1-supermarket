package main;

import javafx.scene.control.TextArea;
import main.linkedList.BaoList;
import main.supermarketComponents.*;


public class viewAllController {
    public BaoList <FloorArea> baoList;
    private MainApplication mainApp;
    public TextArea textArea;
    public void setMainApp(MainApplication mainApp) {
        this.mainApp = mainApp;
    }
    public void viewAll(){
        String text ="";
        text+="Hypermarket has "+baoList.getSize()+" Floor Areas, total value: €"+getValue(baoList)+".\n";
        for (FloorArea area: baoList)
        {
            text += createSpaces(4);
            text+="Floor area "+area.getName()+" has "+area.getInnerList().getSize()+" aisles, total value: €"+getValue(area.getInnerList())+".\n";
            for (Aisle aisle: area.getInnerList())
            {
                text+=createSpaces(16);
                text+="Aisle "+aisle.getName()+" has "+aisle.getInnerList().getSize()+" shelves, total value: €"+getValue(aisle.getInnerList())+".\n";
                for (Shelf shelf: aisle.getInnerList())
                {
                    text+=createSpaces(28);
                    text+="Shelf "+shelf.getName()+" has "+getCount(shelf.getInnerList())+" goods, total value: €"+getValue(shelf.getInnerList())+".\n";
                    for (Goods goods: shelf.getInnerList())
                    {
                        text+=createSpaces(40);
                        text+=goods.getName()+"("+goods.getWeight()+"g), "+goods.getQuantity()+" items, at €"+goods.getPrice()+" for a total value of: €"+goods.getPrice()*goods.getQuantity()+".\n";
                    }
                }
            }
        }
        textArea.setText(text);
    }
    public void switchToModelView()
    {
        mainApp.switchScene("model");
    }
    public void switchToCliView()
    {
        mainApp.switchScene("cli");
    }
    public double getValue(BaoList list)
    {
        double value=0;
        for (Object o: list)
            value+=((Components)o).getValue();
        return value;
    }
    public int getCount(BaoList list)
    {
        int count=0;
        for (Object o: list)
            count+=((Goods)o).getQuantity();
        return count;
    }
    public String createSpaces(int count)
    {
        String spaces="";
        for (int i=0; i<count; i++)
            spaces+=" ";
        return spaces;
    }
}
