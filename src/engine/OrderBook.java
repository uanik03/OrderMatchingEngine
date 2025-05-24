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
                if (order.getOrderType() == OrderType.LIMIT) {
                    if (order.getPrice() <= 0) {
                        throw new Exception("Price should be greater than 0");
                    }

                    if (order.getSide() == Side.BUY) {
                        int qty = order.getQuantity();
                        while (qty > 0) {
                            Order tmpOrder = sellOrders.peek();
                            if (tmpOrder == null) {
                                buyOrders.add(order);
                                break;
                            } else if (tmpOrder.getQuantity() > qty && tmpOrder.getPrice() <= order.getPrice()) {
                                tmpOrder.setQuantity(tmpOrder.getQuantity() - qty);
                                qty = 0;
                            } else if (tmpOrder.getQuantity() == qty && tmpOrder.getPrice() <= order.getPrice()) {
                                sellOrders.remove();
                                qty = 0;
                            } else if (tmpOrder.getQuantity() < qty && tmpOrder.getPrice() <= order.getPrice()) {
                                qty = qty - tmpOrder.getQuantity();
                                sellOrders.remove();

                            } else {
                                if( qty != order.getQuantity()){
                                    order.setQuantity(qty);
                                }
                                    buyOrders.add(order);


                                break;
                            }
                        }
                    } else {
                        int qty = order.getQuantity();
                        while (qty > 0) {
                            Order tmpOrder = buyOrders.peek();
                            if (tmpOrder == null) {
                                sellOrders.add(order);
                                break;
                            } else if (tmpOrder.getQuantity() > qty && tmpOrder.getPrice() >= order.getPrice()) {
                                tmpOrder.setQuantity(tmpOrder.getQuantity() - qty);
                                qty = 0;
                            } else if (tmpOrder.getQuantity() == qty && tmpOrder.getPrice() >= order.getPrice()) {
                                buyOrders.remove();
                                qty = 0;
                            } else if (tmpOrder.getQuantity() < qty && tmpOrder.getPrice() >= order.getPrice()) {
                                qty = qty - tmpOrder.getQuantity();
                                buyOrders.remove();

                            } else {
                                if( qty != order.getQuantity()){
                                    order.setQuantity(qty);
                                }
                                sellOrders.add(order);
                                break;
                            }
                        }
                    }


                }else{
                    //Market order
                    if (order.getSide() == Side.BUY ) {
                        int qty = order.getQuantity();
                        while (qty > 0) {
                            Order tmpOrder = sellOrders.peek();
                            if (tmpOrder == null) {
                                if( qty != order.getQuantity()){
                                    order.setQuantity(qty);
                                }
                                buyOrders.add(order);
                                break;
                            } else if (tmpOrder.getQuantity() > qty ) {
                                tmpOrder.setQuantity(tmpOrder.getQuantity() - qty);
                                qty = 0;
                            }else if(tmpOrder.getQuantity() == qty ){
                                qty=0;
                                sellOrders.remove();
                            }else {
                                qty=qty- tmpOrder.getQuantity();
                                sellOrders.remove();
                            }
                        }
                    }else{
                        int qty = order.getQuantity();
                        while (qty > 0) {
                            Order tmpOrder = buyOrders.peek();
                            if (tmpOrder == null) {
                                if( qty != order.getQuantity()){
                                    order.setQuantity(qty);
                                }
                                sellOrders.add(order);
                                break;
                            } else if (tmpOrder.getQuantity() > qty ) {
                                tmpOrder.setQuantity(tmpOrder.getQuantity() - qty);
                                qty = 0;
                            }else if(tmpOrder.getQuantity() == qty ){
                                qty=0;
                                buyOrders.remove();
                            }else {
                                qty=qty- tmpOrder.getQuantity();
                                buyOrders.remove();
                            }
                        }
                    }


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
