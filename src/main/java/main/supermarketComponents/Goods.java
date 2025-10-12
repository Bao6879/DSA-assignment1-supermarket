package main.supermarketComponents;

import main.Utilities;
import main.linkedList.BaoList;
import main.linkedList.BaoNode;

import java.util.Objects;

public class Goods extends Components {
    private String description, name;
    private double price, weight, temperature;
    private int quantity;
    private String imageUrl;
    public Goods(String name, String description, double weight, double price, int quantity, double temperature, String imageUrl) {
        setName(name);
        setDescription(description);
        setPrice(price);
        setWeight(weight);
        setQuantity(quantity);
        setTemperature(temperature);
        setAttributes();
        setImageUrl(imageUrl);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return Double.compare(price, goods.price) == 0 && Double.compare(weight, goods.weight) == 0 && Double.compare(temperature, goods.temperature) == 0 && Objects.equals(description, goods.description) && Objects.equals(name, goods.name) && Objects.equals(imageUrl, goods.imageUrl);
    }

    @Override
    public int getTotalSize() {
        return 1;
    }

    @Override
    public String toString() {
        return "Goods: "+name+"; Description: "+description+"; Weight: "+weight+"; Price: "+price+"; Quantity: "+quantity+"; Temperature: "+temperature+"; Image: "+imageUrl;
    }

    @Override
    public String toRawString() {
        return name+", "+description+", "+weight+", "+price+", "+quantity+", "+temperature+", "+imageUrl;
    }

    @Override
    public void setAttributes() {
        attributes=new BaoList();
        attributes.addNode(new BaoNode(Utilities.defaultString));
        attributes.addNode(new BaoNode(Utilities.defaultString));
        attributes.addNode(new BaoNode(Utilities.defaultDouble));
        attributes.addNode(new BaoNode(Utilities.defaultDouble));
        attributes.addNode(new BaoNode(Utilities.defaultInteger));
        attributes.addNode(new BaoNode(Utilities.defaultDouble));
        attributes.addNode(new BaoNode(Utilities.defaultString));
    }

    @Override
    public BaoList getAttributes() {
        return attributes;
    }

    @Override
    public BaoList getInnerList() {
        return null;
    }

    @Override
    public void setInnerList(BaoList<?> innerList) {

    }

    @Override
    public double similarScore(Components other) {
        if (other==null)
            return Double.MAX_VALUE-1;
        Goods otherGoods = (Goods) other;
        double difference=Math.abs(otherGoods.getWeight()-getWeight())+Math.abs(otherGoods.getPrice()-getPrice())+Math.abs(otherGoods.getTemperature()-getTemperature());
        difference+=Math.abs(otherGoods.getName().length()-getName().length());
        for (int i=0; i<Math.min(getName().length(), otherGoods.getName().length()); i++)
            if (getName().charAt(i)!=otherGoods.getName().charAt(i))
                difference++;
        return difference;
    }
}
