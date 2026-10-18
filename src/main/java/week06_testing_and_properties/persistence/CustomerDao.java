package week06_testing_and_properties.persistence;


import week06_testing_and_properties.entities.Customer;

public interface CustomerDao {
    Customer getCustomerByCustomerNumber(int customerNumber);
}
