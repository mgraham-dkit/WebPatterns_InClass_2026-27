package week06_testing_and_properties.entities;

public record OrderDetail(int orderNumber, String productCode, int quantityOrdered, double priceEach,
                          int orderLineNumber) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        OrderDetail that = (OrderDetail) o;
        return orderNumber == that.orderNumber && productCode.equals(that.productCode);
    }

    @Override
    public int hashCode() {
        int result = orderNumber;
        result = 31 * result + productCode.hashCode();
        return result;
    }
}
