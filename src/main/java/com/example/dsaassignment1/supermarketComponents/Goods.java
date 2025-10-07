package com.example.dsaassignment1.supermarketComponents;

public class Goods {
    private String description;
    private double price, weight;
    private int quantity;
    private String imageUrl;
    public Goods(String description, double price, double weight, int quantity, String imageUrl) {
        setDescription(description);
        setPrice(price);
        setWeight(weight);
        setQuantity(quantity);
        setImageUrl(imageUrl);
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
