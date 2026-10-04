package week03_daos.entities;

import java.util.Objects;

public record Customer(int customerNumber, String customerName, String contactLastName, String contactFirstName, String phone, String addressLine1, String addressLine2, String city, String state, String postalCode, String Country, int salesRepEmployeeNumber, double creditLimit) {

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer customer)) return false;
        return customerNumber == customer.customerNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(customerNumber);
    }
}
