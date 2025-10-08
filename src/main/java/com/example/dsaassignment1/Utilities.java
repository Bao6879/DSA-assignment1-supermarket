package com.example.dsaassignment1;

import com.example.dsaassignment1.linkedList.BaoNode;
import com.example.dsaassignment1.supermarketComponents.FloorArea;

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
        return temp.trim();
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
        return temp.trim();
    }

    public static BaoNode constructNode(String s)
    {
        if (s.startsWith("Floor Area:"))
        {
            BaoNode <FloorArea> baoNode;
            String title=extractElement(s.substring(12));
            int level=Integer.parseInt(extractElement(s.substring(12+title.length()+1)).substring(7));
            baoNode=new BaoNode<>(new FloorArea(title,level));
            return baoNode;
        }
        return null;
    }
}
