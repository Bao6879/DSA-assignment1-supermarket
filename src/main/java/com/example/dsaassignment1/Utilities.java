package com.example.dsaassignment1;

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

}
