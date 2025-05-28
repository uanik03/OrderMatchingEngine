import engine.Order;
import engine.OrderBook;
import engine.OrderType;
import engine.Side;

import static engine.Symbol.EUR_USD;

public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();

//
//        Order buyOrder1 = new Order( Side.BUY, OrderType.LIMIT, 510.0, 70, EUR_USD);
//        orderBook.addOrder(buyOrder1);
//
//
//        // Add a SELL limit order: 100 units @ Rs. 500
//        Order sellOrder1 = new Order( Side.SELL, OrderType.LIMIT, 500.0, 100,EUR_USD);
//        orderBook.addOrder(sellOrder1);
//
//        // Add another BUY limit order that partially matches the rest
//        Order buyOrder2 = new Order( Side.BUY, OrderType.LIMIT, 500.0, 50,EUR_USD);
//        orderBook.addOrder(buyOrder2);
//
//        // Add a SELL market order (should match existing BUY order)
//        Order sellMarketOrder = new Order( Side.SELL, OrderType.MARKET, 10.0, 30, EUR_USD);
//        orderBook.addOrder(sellMarketOrder);
//
//        // Add a SELL limit order that stays in the book (no match)
//        Order sellOrder2 = new Order( Side.SELL, OrderType.LIMIT, 520.0, 100, EUR_USD);
//        orderBook.addOrder(sellOrder2);
//
//        // Add a BUY market order (should match above sell order)
//        Order buyMarketOrder = new Order( Side.BUY, OrderType.MARKET, 10.0, 1000, EUR_USD );
//        orderBook.addOrder(buyMarketOrder);

        System.out.println(orderBook);
    }
}