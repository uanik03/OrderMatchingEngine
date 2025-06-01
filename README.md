# 🏛️ Order Matching Engine

A multithreaded, symbol-aware, in-memory order matching engine built in Java.  
This engine supports basic trading operations such as **placing orders**, **modifying**, **cancelling**, and **matching buy/sell orders** in real-time across multiple instruments (symbols).

---

## 📦 Features

- ✅ **Multi-symbol support** via an `Exchange` singleton and symbol-specific `OrderBook`s
- ✅ **Limit & Market orders** with correct matching logic
- ✅ **PriorityQueue-based price-time matching**
- ✅ **Modify and cancel orders**
- ✅ **Trade history tracking**
- ✅ **Thread-safe routing and symbol-level isolation**
- ✅ **Auto-generated Order IDs**

---

## 🧠 Design Overview

### 🔁 Order Matching Logic
- **Limit orders** are matched based on price-time priority.
- **Market orders** execute against best available opposite orders.
- **Partial fills** are supported.
- Remaining unmatched orders are retained in the book if not market orders.

### 🏛 Exchange Architecture
- The `Exchange` is a **thread-safe singleton**.
- It manages a `ConcurrentHashMap<Symbol, OrderBook>`.
- Orders are routed using `routeOrder(order, actionType)`.

### ⚙ OrderBook Internals
- Maintains:
  - `PriorityQueue<Order>` for **buy** and **sell** sides.
  - A map of **active orders** by order ID.
  - A **trade history log**.

---

## 🛠️ Technologies Used

- **Java 17+**
- `PriorityQueue` for order ranking
- `ConcurrentHashMap` for concurrency-safe symbol management
- `Thread` and `Runnable` for testing multithreaded behavior

---

## 🚀 Getting Started

### 📁 Project Structure

```bash
src/
├── Main.java                 # Entry point for testing
└── engine/
    ├── Exchange.java         # Central trading engine
    ├── OrderBook.java        # Symbol-specific matching logic
    ├── Order.java            # Order model
    ├── Trade.java            # Trade record model
    ├── Side.java             # BUY / SELL enum
    ├── Symbol.java           # Enum of tradable instruments
    ├── OrderType.java        # MARKET / LIMIT
    └── ActionType.java       # ADD / MODIFY / CANCEL
