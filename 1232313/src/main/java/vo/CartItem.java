package vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItem {
    private Long bookId;
    private int quantity;
    private Book book;

    public CartItem() {}

    public CartItem(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

}