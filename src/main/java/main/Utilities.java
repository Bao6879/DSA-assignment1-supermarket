package main;

import main.linkedList.BaoList;
import main.linkedList.BaoNode;
import main.supermarketComponents.Aisle;
import main.supermarketComponents.FloorArea;
import main.supermarketComponents.Shelf;

public class Utilities {
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

    public static BaoNode constructNode(String s)
    {
        if (s.startsWith("Floor Area:"))
        {
            String title=extractElement(s.substring(12));
            int level=Integer.parseInt(extractElement(s.substring(12+title.length()+1)).substring(7).trim());
            return new BaoNode<>(new FloorArea(title.trim(), level));
        }
        else if (s.startsWith("Aisle:"))
        {
            int index=6;
            String name=extractElement(s.substring(index)), length, width, temperature;
            index+=name.length()+9;

            length=extractElement(s.substring(index));
            index+=length.length()+8;

            width=extractElement(s.substring(index));
            index+=width.length()+14;

            temperature=extractElement(s.substring(index));

            return new BaoNode<>(new Aisle(name.trim(), Double.parseDouble(length.trim()), Double.parseDouble(width.trim()), Double.parseDouble(temperature.trim())));
        }
        else if (s.startsWith("Shelf:"))
        {
            return new BaoNode<>(new Shelf(Integer.parseInt(extractElement(s.substring(7)).trim())));
        }
        return null;
    }

    public static BaoList copyList(BaoList list)
    {
        BaoList temp=new BaoList();
        for (Object o : list)
            temp.addNode(constructNode(o.toString()));
        return temp;
    }
}
