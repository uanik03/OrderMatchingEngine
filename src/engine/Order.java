package engine;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

public class Order {
    private static final AtomicInteger idGenerator = new AtomicInteger(1);
    private final int orderId;
    private final Symbol symbol;
    private final Side side;
    private final OrderType orderType;
    private double price;
    private int quantity;
    private final long timestamp;


    public Order(Side side, OrderType orderType, double price, int quantity, Symbol symbol) {
        this.side = side;
        this.orderType = orderType;
        this.price = price;
        this.timestamp = System.currentTimeMillis();
        this.quantity=quantity;
        this.orderId=idGenerator.getAndIncrement();
        this.symbol = symbol;
    }

    public Order(int orderId,  Side side, OrderType orderType,  double price, int quantity, Symbol symbol) {
        this.orderType = orderType;
        this.side = side;
        this.orderId = orderId;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = System.currentTimeMillis();
        this.symbol = symbol;
    }

    public Symbol getSymbol() {
        return symbol;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", side=" + side +
                ", orderType=" + orderType +
                ", price=" + price +
                ", quantity=" + quantity +
                ", timestamp=" + timestamp +
                '}';
    }
}
