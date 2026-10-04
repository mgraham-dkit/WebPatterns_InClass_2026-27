package week03_daos.apps;

import week03_daos.entities.Customer;
import week03_daos.persistence.CustomerDao;
import week03_daos.persistence.CustomerDaoImpl;

import java.util.List;
import java.util.Scanner;

public class CustomerSearch {
    static void main() {
        Scanner kb = new Scanner(System.in);

        System.out.println("Please enter a name to search for: ");
        String input = kb.next();

        CustomerDao cdi = new CustomerDaoImpl();

        List<Customer> customers = cdi.selectCustomersByName(input);

        if(customers.isEmpty()){
            customers = cdi.selectCustomersContainingName(input);
            if(customers.isEmpty()){
                System.out.println("No matching results...");
            }
        }
        for(Customer c : customers){
            System.out.println(c);
        }

        }
    }
