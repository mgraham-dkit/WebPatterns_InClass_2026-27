package week08_services.persistence;


import week08_services.entities.Customer;

public interface CustomerDao {
    Customer getCustomerByCustomerNumber(int customerNumber);
}
