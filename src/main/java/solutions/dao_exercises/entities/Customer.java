package solutions.dao_exercises.entities;

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

    @Override
    public int hashCode() {
        return customerNumber;
    }
}
