package vo;

import lombok.Data;

@Data
public class Order {
    private int id;
    private String userId;
    private int totalPrice;

    public Order(int id, String userId, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }
}