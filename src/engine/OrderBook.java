package engine;

import java.util.Comparator;
import java.util.PriorityQueue;

public class OrderBook {
    private PriorityQueue<Order> buyOrders;


    private PriorityQueue<Order> sellOrders;


    public OrderBook() {
        this.sellOrders = new PriorityQueue<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if (Double.compare(o2.getPrice(), o1.getPrice()) != 0) {
                    return Double.compare(o1.getPrice(), o2.getPrice());
                } else {
                    return Long.compare(o1.getTimestamp(), o2.getTimestamp());
                }
            }

        });


        this.buyOrders = new PriorityQueue<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if (Double.compare(o2.getPrice(), o1.getPrice()) != 0) {
                    return Double.compare(o2.getPrice(), o1.getPrice());
                } else {
                    return Long.compare(o1.getTimestamp(), o2.getTimestamp());
                }
            }

        });
    }


    public boolean addOrder(Order order) {
        try {
            if (order.getQuantity() > 0) {
                if(order.getOrderType() == OrderType.LIMIT && order.getPrice() <=0){
                    throw new Exception("Price should be greater than 0");
                }
                if (order.getSide() == Side.BUY) {
                    buyOrders.add(order);
                } else {
                    sellOrders.add(order);
                }

                return true;
            }else{
                throw new Exception("Quantity should be greater than 0");
            }

        }catch(Exception e) {
            System.err.println("Failed to add order: " + e.getMessage());

            return false;
        }
}
}
