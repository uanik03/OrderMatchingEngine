import engine.*;

import static engine.Symbol.*;

public class Main {
    public static void main(String[] args) {
        Exchange exchange = Exchange.getInstance();

        Runnable task1 = () -> {
            for (int i = 0; i < 10; i++) {
                Order order = new Order( Side.BUY, OrderType.LIMIT, 100.0 + i, 10, Symbol.AAPL);
                exchange.routeOrder(order, ActionType.ADD);
            }
        };

        Runnable task2 = () -> {
            for (int i = 0; i < 10; i++) {
                Order order = new Order( Side.SELL, OrderType.LIMIT, 1000.0 + i, 10, Symbol.AAPL);
                exchange.routeOrder(order, ActionType.ADD);
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OrderBook orderBook = exchange.getOrCreateOrderBook(Symbol.AAPL);
        System.out.println(orderBook);
    }
}