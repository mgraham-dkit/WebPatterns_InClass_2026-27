package week03_daos.apps;

import week03_daos.entities.Customer;
import week03_daos.persistence.CustomerDao;
import week03_daos.persistence.CustomerDaoImpl;

import java.util.List;
import java.util.Scanner;

public class CustomerSearch {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerDao customerDao = new CustomerDaoImpl();

        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        List<Customer> customers = customerDao.selectCustomersByName(name);

        if (customers.isEmpty()) {
            customers = customerDao.selectCustomersContainingName(name);
        }

        if (customers.isEmpty()) {
            System.out.println("No matching customers found.");
        } else {
            System.out.println("Matching customers:");

            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }
}
