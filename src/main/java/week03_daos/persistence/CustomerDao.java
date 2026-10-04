package week03_daos.persistence;

import week03_daos.entities.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> selectCustomerByName(String name);
    List<Customer> selectCustomerContainingName(String name);
    Customer findCustomerById(int customerNumber);
    boolean addCustomer(Customer c);
}
