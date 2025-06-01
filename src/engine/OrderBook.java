package engine;

import java.util.*;

public class OrderBook {
    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;
    private Map<Integer, Order> activeOrders;
    List<Trade> tradeHistory;

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

        this.activeOrders = new HashMap<>();
        this.tradeHistory = new ArrayList<>();
    }

    private synchronized void matchOrder(PriorityQueue<Order> queue, PriorityQueue<Order> oppQueue, Order incomingOrder, boolean isBuy, boolean isMarket) {

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
            int buyId;
            int sellId;
            if (oppositeOrders.getSide() == Side.BUY) {
                buyId = oppositeOrders.getOrderId();
                sellId = incomingOrder.getOrderId();
            } else {
                sellId = oppositeOrders.getOrderId();
                buyId = incomingOrder.getOrderId();
            }
            Trade tmpTrade = new Trade(buyId, sellId, oppositeOrders.getPrice(), reqQty);
            tradeHistory.add(tmpTrade);

            if (bookQty > reqQty) {
                oppositeOrders.setQuantity(bookQty - reqQty);

            } else {
                oppQueue.remove();
            }

        }

        if (qty > 0) {
            if (isMarket) {
                System.err.println("Unfilled market order discarded: " + incomingOrder);
                return;
            }

            incomingOrder.setQuantity(qty);
            queue.add(incomingOrder);
            activeOrders.put(incomingOrder.getOrderId(), incomingOrder);
            System.out.println("Partially filled order added to book: " + incomingOrder);
        }

    }


    public void addOrder(Order order) {
        try {
            if (order.getQuantity() > 0) {
                if (order.getOrderType()==OrderType.LIMIT && order.getPrice() <= 0) {
                    throw new Exception("Price should be greater than 0");
                }



                boolean isBuyOrder = order.getSide() == Side.BUY;

                boolean isMarketType = order.getOrderType() == OrderType.MARKET;

                if (isBuyOrder) {
                    matchOrder(buyOrders, sellOrders, order, true, isMarketType);
                } else {
                    matchOrder(sellOrders, buyOrders, order, false, isMarketType);
                }

            } else {
                throw new Exception("Quantity should be greater than 0");
            }

        } catch (Exception e) {
            System.err.println("Failed to add order: " + e.getMessage());

        }
    }

    public boolean cancelOrder(int orderId) {
        try {
            Order order = activeOrders.get(orderId);
            if (order == null) {
                System.out.println("Order not found: " + orderId);
                throw new Exception("Order not found");
            }
            if (order.getSide() == Side.BUY) {
                buyOrders.remove(order);

            } else {
                sellOrders.remove(order);
            }
            activeOrders.remove(orderId);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to cancel order: " + e.getMessage());
            return false;
        }
    }

    public boolean modifyOrder(int orderId, double newPrice, int newQuantity) {
        try {
            Order order = activeOrders.get(orderId);
            if (order == null) {
                System.out.println("Order not found: " + orderId);
                throw new Exception("Order not found");
            }
            if (newPrice <= 0) {
                throw new Exception("Price should be greater than 0");
            }
            if (newQuantity <= 0) {
                throw new Exception("Quantity should be greater than 0");
            }


            if (order.getPrice() == newPrice && order.getQuantity() == newQuantity) {
                return true;
            }

            boolean cancelledOrder = cancelOrder(orderId);
            addOrder(new Order(order.getOrderId(), order.getSide(), order.getOrderType(), newPrice, newQuantity, order.getSymbol()));
            return true;
        } catch (Exception e) {
            System.err.println("Failed to modify order: " + e.getMessage());
            return false;
        }
    }


    @Override
    public String toString() {
        return "OrderBook{" +
                "buyOrders=" + buyOrders +
                ", sellOrders=" + sellOrders +
                '}';
    }
}
