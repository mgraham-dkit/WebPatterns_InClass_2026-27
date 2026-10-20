package week06_testing_and_properties.entities;

import java.util.Objects;

public record Customer(int customerNumber, String customerName,
                       String contactLastName, String contactFirstName,
                       String phone, String addressLine1, String addressLine2, String city,
                       String state, String postalCode, String country, int salesRepEmployeeNumber,
                       double creditLimit) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        return customerNumber == customer.customerNumber;
    }

    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        return customerNumber == customer.customerNumber && Double.compare(creditLimit, customer.creditLimit) == 0 && salesRepEmployeeNumber == customer.salesRepEmployeeNumber && Objects.equals(city, customer.city) && Objects.equals(phone, customer.phone) && Objects.equals(state, customer.state) && Objects.equals(country, customer.country) && Objects.equals(postalCode, customer.postalCode) && Objects.equals(customerName, customer.customerName) && Objects.equals(addressLine1, customer.addressLine1) && Objects.equals(addressLine2, customer.addressLine2) && Objects.equals(contactLastName, customer.contactLastName) && Objects.equals(contactFirstName, customer.contactFirstName);
    }

    @Override
    public int hashCode() {
        return customerNumber;
    }
}
