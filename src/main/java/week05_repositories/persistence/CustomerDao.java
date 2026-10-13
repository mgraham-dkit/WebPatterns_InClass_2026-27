package week05_repositories.persistence;


import week05_repositories.entities.Customer;

public interface CustomerDao {
    Customer getCustomerByCustomerNumber(int customerNumber);
}
