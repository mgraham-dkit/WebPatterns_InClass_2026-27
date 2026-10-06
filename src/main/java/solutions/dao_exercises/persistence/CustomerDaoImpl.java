package solutions.dao_exercises.persistence;

import solutions.dao_exercises.entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao{
    @Override
    public List<Customer> selectCustomersByName(String name) {
        List<Customer> customers = new ArrayList<>();

        return customers;
    }
}
