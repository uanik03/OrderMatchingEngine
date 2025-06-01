package engine;

public class Trade {
    private static int id=1;
    private int tradeId;
    private int buyOrderId;
    private double price;
    private int quantity;
    private int sellOrderId;
    private long timestamp;

    public Trade(int buyOrderId, int sellOrderId, double price, int quantity) {
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
        this.price=price;
        this.quantity=quantity;
        this.tradeId=id++;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId=" + tradeId +
                ", buyOrderId=" + buyOrderId +
                ", price=" + price +
                ", quantity=" + quantity +
                ", sellOrderId=" + sellOrderId +
                ", timestamp=" + timestamp +
                '}';
    }
}
