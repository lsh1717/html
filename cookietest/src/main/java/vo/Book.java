package vo;

import lombok.Data;

@Data
public class Book {
    private int id;
    private String name;
    private int price;

    public Book() {}
    
    public Book(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
