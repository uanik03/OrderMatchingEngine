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

    private void matchOrder(PriorityQueue<Order> queue, PriorityQueue<Order> oppQueue, Order incomingOrder, boolean isBuy, boolean isMarket) {

        // no of shares required
        int qty = incomingOrder.getQuantity();

        while (qty > 0) {
            // check the orderbook to see if any deals exists
            Order oppositeOrders = oppQueue.peek();
            if (oppositeOrders == null) break;

            //check if deal is valid
            boolean matchOccured = isMarket || (isBuy && oppositeOrders.getPrice() <= incomingOrder.getPrice()) || (!isBuy && oppositeOrders.getPrice() >= incomingOrder.getPrice());

            if (!matchOccured) break;

            //no of shares in the book
            int bookQty = oppositeOrders.getQuantity();

            //no of share a guy need
            int reqQty = Math.min(bookQty, incomingOrder.getQuantity());

//            reduce from the qty
            qty -= reqQty;
            System.out.println("Trade: " + reqQty + " units at price " + oppositeOrders.getPrice());

            if (bookQty > reqQty) {
                oppositeOrders.setQuantity(bookQty - reqQty);

            } else {
                oppQueue.remove();
            }

        }

        if (qty > 0) {
            incomingOrder.setQuantity(qty);
            queue.add(incomingOrder);
        }

    }


    public boolean addOrder(Order order) {
        try {
            if (order.getQuantity() > 0) {
                if(order.getPrice()<=0){
                    throw new Exception("Price should be greater than 0");
                }

                boolean isBuyOrder = order.getSide() == Side.BUY;

                boolean isMarketType = order.getOrderType() == OrderType.MARKET;

                if(isBuyOrder){
                    matchOrder(buyOrders, sellOrders, order, true, isMarketType);
                }else{
                    matchOrder(sellOrders, buyOrders, order, false, isMarketType);
                }

                return true;
            } else {
                throw new Exception("Quantity should be greater than 0");
            }

        } catch (Exception e) {
            System.err.println("Failed to add order: " + e.getMessage());

            return false;
        }
    }
}
