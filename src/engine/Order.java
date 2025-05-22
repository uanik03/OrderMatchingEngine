package engine;

import java.sql.Timestamp;

public class Order {
    private static int id=1;
    private int orderId;
    private Side side;
    private OrderType orderType;
    private double price;
    private int quantity;
    private long timestamp;


    public Order(Side side, OrderType orderType, double price, int quantity) {
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
        this.quantity=quantity;
        this.orderId=id++;
    }

    public Side getSide() {
        return side;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity(){
        return quantity;
    }
}
