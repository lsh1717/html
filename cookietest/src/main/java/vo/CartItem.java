package vo;

import lombok.Data;

@Data
public class CartItem {
    private Book book;
    private int quantity;

    public CartItem() {}
    
    public CartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }
}