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
    public static BaoList <String> skippedWords=new BaoList<>();
    public static void createSkippedWords(){
        // Articles
        skippedWords.addNode("a");
        skippedWords.addNode("an");
        skippedWords.addNode("the");

        // Prepositions
        skippedWords.addNode("in");
        skippedWords.addNode("on");
        skippedWords.addNode("at");
        skippedWords.addNode("to");
        skippedWords.addNode("for");
        skippedWords.addNode("of");
        skippedWords.addNode("with");
        skippedWords.addNode("from");
        skippedWords.addNode("by");
        skippedWords.addNode("about");
        skippedWords.addNode("as");
        skippedWords.addNode("into");
        skippedWords.addNode("through");
        skippedWords.addNode("during");
        skippedWords.addNode("before");
        skippedWords.addNode("after");
        skippedWords.addNode("above");
        skippedWords.addNode("below");
        skippedWords.addNode("between");
        skippedWords.addNode("under");
        skippedWords.addNode("over");
        skippedWords.addNode("along");
        skippedWords.addNode("around");
        skippedWords.addNode("behind");
        skippedWords.addNode("beside");
        skippedWords.addNode("beyond");
        skippedWords.addNode("near");
        skippedWords.addNode("off");
        skippedWords.addNode("onto");
        skippedWords.addNode("out");
        skippedWords.addNode("outside");
        skippedWords.addNode("within");
        skippedWords.addNode("without");

        // Conjunctions
        skippedWords.addNode("and");
        skippedWords.addNode("or");
        skippedWords.addNode("but");
        skippedWords.addNode("nor");
        skippedWords.addNode("so");
        skippedWords.addNode("yet");
        skippedWords.addNode("if");
        skippedWords.addNode("because");
        skippedWords.addNode("while");
        skippedWords.addNode("although");
        skippedWords.addNode("though");
        skippedWords.addNode("unless");
        skippedWords.addNode("until");
        skippedWords.addNode("since");
        skippedWords.addNode("whereas");

        // Pronouns
        skippedWords.addNode("i");
        skippedWords.addNode("you");
        skippedWords.addNode("he");
        skippedWords.addNode("she");
        skippedWords.addNode("it");
        skippedWords.addNode("we");
        skippedWords.addNode("they");
        skippedWords.addNode("me");
        skippedWords.addNode("him");
        skippedWords.addNode("her");
        skippedWords.addNode("us");
        skippedWords.addNode("them");
        skippedWords.addNode("my");
        skippedWords.addNode("your");
        skippedWords.addNode("his");
        skippedWords.addNode("its");
        skippedWords.addNode("our");
        skippedWords.addNode("their");
        skippedWords.addNode("mine");
        skippedWords.addNode("yours");
        skippedWords.addNode("hers");
        skippedWords.addNode("ours");
        skippedWords.addNode("theirs");
        skippedWords.addNode("this");
        skippedWords.addNode("that");
        skippedWords.addNode("these");
        skippedWords.addNode("those");
        skippedWords.addNode("who");
        skippedWords.addNode("whom");
        skippedWords.addNode("whose");
        skippedWords.addNode("which");
        skippedWords.addNode("what");

        //Auxiliary/Modal verbs
        skippedWords.addNode("is");
        skippedWords.addNode("am");
        skippedWords.addNode("are");
        skippedWords.addNode("was");
        skippedWords.addNode("were");
        skippedWords.addNode("be");
        skippedWords.addNode("been");
        skippedWords.addNode("being");
        skippedWords.addNode("have");
        skippedWords.addNode("has");
        skippedWords.addNode("had");
        skippedWords.addNode("do");
        skippedWords.addNode("does");
        skippedWords.addNode("did");
        skippedWords.addNode("will");
        skippedWords.addNode("would");
        skippedWords.addNode("shall");
        skippedWords.addNode("should");
        skippedWords.addNode("may");
        skippedWords.addNode("might");
        skippedWords.addNode("must");
        skippedWords.addNode("can");
        skippedWords.addNode("could");

        //Common adverbs
        skippedWords.addNode("not");
        skippedWords.addNode("no");
        skippedWords.addNode("yes");
        skippedWords.addNode("very");
        skippedWords.addNode("too");
        skippedWords.addNode("also");
        skippedWords.addNode("just");
        skippedWords.addNode("only");
        skippedWords.addNode("even");
        skippedWords.addNode("now");
        skippedWords.addNode("then");
        skippedWords.addNode("here");
        skippedWords.addNode("there");
        skippedWords.addNode("where");
        skippedWords.addNode("when");
        skippedWords.addNode("why");
        skippedWords.addNode("how");
        skippedWords.addNode("all");
        skippedWords.addNode("both");
        skippedWords.addNode("each");
        skippedWords.addNode("few");
        skippedWords.addNode("more");
        skippedWords.addNode("most");
        skippedWords.addNode("other");
        skippedWords.addNode("some");
        skippedWords.addNode("such");

        //Food-related words
        skippedWords.addNode("fresh");
        skippedWords.addNode("frozen");
        skippedWords.addNode("organic");
        skippedWords.addNode("natural");
        skippedWords.addNode("free");
        skippedWords.addNode("range");
        skippedWords.addNode("pack");
        skippedWords.addNode("packet");
        skippedWords.addNode("box");
        skippedWords.addNode("tin");
        skippedWords.addNode("can");
        skippedWords.addNode("bottle");
        skippedWords.addNode("jar");
        skippedWords.addNode("bag");
        skippedWords.addNode("tube");
    }
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
