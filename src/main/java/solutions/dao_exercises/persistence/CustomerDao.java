package solutions.dao_exercises.persistence;

import solutions.dao_exercises.entities.Customer;

import java.util.List;

public interface CustomerDao {
    List<Customer> selectCustomersByName(String name);
    List<Customer> selectCustomersContainingName(String name);
}
