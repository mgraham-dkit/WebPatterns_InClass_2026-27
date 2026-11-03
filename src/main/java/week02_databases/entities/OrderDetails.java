package week02_databases.entities;

import java.util.Objects;

public class OrderDetails {
    private int orderNumber;
    private String productCode;
    private int quantityOrdered;
    private double priceEach;
    private int orderLineNumber;


    public OrderDetails(int orderNumber, String productCode, int quantityOrdered, double priceEach, int orderLineNumber) {
        this.orderNumber = orderNumber;
        this.productCode = productCode;
        this.quantityOrdered = quantityOrdered;
        this.priceEach = priceEach;
        this.orderLineNumber = orderLineNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getQuantityOrdered() {
        return quantityOrdered;
    }

    public double getPriceEach() {
        return priceEach;
    }

    public int getOrderLineNumber() {
        return orderLineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderDetails that = (OrderDetails) o;
        return orderNumber == that.orderNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderNumber);
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderLineNumber=" + orderLineNumber +
                ", priceEach=" + priceEach +
                ", quantityOrdered=" + quantityOrdered +
                ", productCode='" + productCode + '\'' +
                ", orderNumber=" + orderNumber +
                '}';
    }
}
