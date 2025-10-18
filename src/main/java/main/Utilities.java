package main;

import javafx.scene.control.Label;
import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import main.supermarketComponents.*;

import java.awt.*;

public class Utilities {
    public static String defaultString="";
    public static Integer defaultInteger=0;
    public static Double defaultDouble=0.0;
    public static String extractElement(String s)
    {
        String temp="";
        int i=0;
        while (i<s.length())
        {
            if (s.charAt(i)==';')
                break;
            temp+=s.charAt(i);
            i++;
        }
        return temp;
    }

    public static String extractAttribute(String s)
    {
        String temp="";
        int i=0;
        while (i<s.length())
        {
            if (s.charAt(i)==',')
                break;
            temp+=s.charAt(i);
            i++;
        }
        return temp;
    }

    public static int countCommas(String s)
    {
        int count=0;
        for (int i=0;i<s.length();i++)
            if (s.charAt(i)==',')
                count++;
        return count;
    }

    public static boolean checkStringNotFitType(String s, String type)
    {
        int count=countCommas(s);
        switch (type)
        {
            case "floorArea": return count != 1;
            case "aisle": return count != 3;
            case "shelf": return count != 0;
            default: return count != 6;
        }
    }

    public static boolean checkStringInvalidInteger(String s, Label comment)
    {
        s=s.trim();
        if (s.isEmpty()) {
            if (comment!=null)
                comment.setText("Invalid number formatting.");
            return true;
        }
        int start=0;
        if (s.startsWith("-") || s.startsWith("+"))
            start=1;
        for (int i=start;i<s.length();i++) {
            if (!Character.isDigit(s.charAt(i))) {
                if (comment!=null)
                    comment.setText("Invalid number formatting.");
                return true;
            }
        }
        return false;
    }

    public static boolean checkStringInvalidDouble(String s, Label comment)
    {
        s=s.trim();
        if (s.isEmpty()) {
            if (comment!=null)
                comment.setText("Invalid number formatting.");
            return true;
        }
        int start=0, dotCount=0;
        if (s.startsWith("-") || s.startsWith("+"))
            start=1;
        for (int i=start;i<s.length();i++)
        {
            if (s.charAt(i)=='.')
                dotCount++;
            else if (!Character.isDigit(s.charAt(i))) {
                if (comment!=null)
                    comment.setText("Invalid number formatting.");
                return true;
            }
        }
        if (dotCount>1)
        {
            if (comment!=null)
                comment.setText("Invalid number formatting.");
            return true;
        }
        return false;
    }

    public static BaoNode <Components> constructNode(String s)
    {
        if (!checkStringNotFitType(s, "floorArea"))
            return new BaoNode<>(getFloorAreaFromString(s, null));
        else if (!checkStringNotFitType(s, "aisle"))
            return new BaoNode<>(getAisleFromString(s, null));
        else if (!checkStringNotFitType(s, "shelf"))
            return new BaoNode<>(getShelfFromString(s, null));
        else
            return new BaoNode<>(getGoodFromString(s, null));
    }

    public static FloorArea getFloorAreaFromString(String floorArea, Label comment) {
        String title, level;
        title = Utilities.extractAttribute(floorArea);
        level = Utilities.extractElement(floorArea.substring(title.length() + 1));
        if (Utilities.checkStringInvalidInteger(level, comment))
            return null;

        return new FloorArea(title.trim(), Integer.parseInt(level.trim()));
    }

    public static Aisle getAisleFromString(String aisle, Label comment)
    {
        String name, length, width, temperature;
        int currentPosition=0;
        name = Utilities.extractAttribute(aisle);
        currentPosition+=name.length()+1;

        length=Utilities.extractAttribute(aisle.substring(currentPosition));
        if (Utilities.checkStringInvalidDouble(length, comment))
            return null;
        currentPosition+=length.length()+1;

        width=Utilities.extractAttribute(aisle.substring(currentPosition));
        if (Utilities.checkStringInvalidDouble(width, comment))
            return null;
        currentPosition+=width.length()+1;

        temperature=Utilities.extractElement(aisle.substring(currentPosition));

        return new Aisle(name.trim(), Double.parseDouble(length.trim()), Double.parseDouble(width.trim()), temperature.trim());
    }

    public static Shelf getShelfFromString(String shelf, Label comment)
    {
        String number;
        number=Utilities.extractAttribute(shelf);
        if (Utilities.checkStringInvalidInteger(number, comment))
            return null;

        return new Shelf(Integer.parseInt(number.trim()));
    }

    public static Goods getGoodFromString(String goods, Label comment)
    {
        String name, description, weight, price, quantity, temperature, image;
        int currentPosition=0;
        name = Utilities.extractAttribute(goods);
        currentPosition+=name.length()+1;

        description=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=description.length()+1;

        weight=Utilities.extractAttribute(goods.substring(currentPosition));
        if (Utilities.checkStringInvalidDouble(weight, comment))
            return null;
        currentPosition+=weight.length()+1;

        price=Utilities.extractAttribute(goods.substring(currentPosition));
        if (Utilities.checkStringInvalidDouble(price, comment))
            return null;
        currentPosition+=price.length()+1;

        quantity=Utilities.extractAttribute(goods.substring(currentPosition));
        if (Utilities.checkStringInvalidInteger(quantity, comment))
            return null;
        currentPosition+=quantity.length()+1;

        temperature=Utilities.extractAttribute(goods.substring(currentPosition));
        currentPosition+=temperature.length()+1;

        image=Utilities.extractElement(goods.substring(currentPosition));

        return new Goods(name.trim(), description.trim(), Double.parseDouble(weight.trim()), Double.parseDouble(price.trim()), Integer.parseInt(quantity.trim()), temperature.trim(), image.trim());
    }

    public static BaoList copyList(BaoList list)
    {
        BaoList temp=new BaoList();
        for (Object o : list)
            temp.addNode(new BaoNode(o));
        return temp;
    }
}
