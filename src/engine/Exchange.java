package engine;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Implemented singleton so that only uno object of this class is created
public class Exchange implements Serializable {

    private static volatile    Exchange instance;

    private Map<Symbol, OrderBook> symbolOrderBookMap;

    private Exchange(){
        if(instance!=null){
            throw new RuntimeException("Use getInstance() method");
        }
        symbolOrderBookMap = new ConcurrentHashMap<>();

    }

    public static Exchange getInstance(){
        if(instance==null){
            synchronized (Exchange.class){
                if (instance == null) {
                    instance = new Exchange();
                }
            }
        }

        return instance;
    }

    @Serial
    private Object readResolve() {
        return getInstance();
    }

    public OrderBook getOrCreateOrderBook(Symbol symbol) {
        return symbolOrderBookMap.computeIfAbsent(symbol, k -> new OrderBook());
    }


    public boolean routeOrder(Order order, ActionType actionType){
        try{
            OrderBook orderBook = getOrCreateOrderBook(order.getSymbol());
            if(actionType== ActionType.ADD){
                orderBook.addOrder(order);

            }else if(actionType==ActionType.CANCEL){
                orderBook.cancelOrder(order.getOrderId());

            }else if(actionType==ActionType.UPDATE){
                orderBook.modifyOrder(order.getOrderId(), order.getPrice(), order.getQuantity());
            }

            return true;
        } catch (Exception e) {
            System.out.println("error in create order : "+e.getMessage());
            e.printStackTrace(); // Or use a logging framework

            return false;
        }
    }



}
