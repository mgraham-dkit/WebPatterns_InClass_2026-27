package week07_logging.persistence;


import week07_logging.entities.Customer;

public interface CustomerDao {
    Customer getCustomerByCustomerNumber(int customerNumber);
}
