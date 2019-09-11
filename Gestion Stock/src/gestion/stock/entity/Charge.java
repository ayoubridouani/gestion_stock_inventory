package gestion.stock.entity;

public class Charge{
    private int id;
    private String name;
    private Double price;
    private String description;
    private String date;

    public Charge() {
        
    }

    public Charge(int id, String name, Double price, String date, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
