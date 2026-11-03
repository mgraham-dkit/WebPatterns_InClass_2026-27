package week03_daos.apps;

import week03_daos.entities.Customer;
import week03_daos.persistence.CustomerDao;
import week03_daos.persistence.CustomerDaoImpl;

import java.util.Scanner;

public class AddCustomer {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        CustomerDao customerDao = new CustomerDaoImpl();

        int customerNumber;

        while (true) {
            System.out.print("Enter a customer ID number: ");
            customerNumber = kb.nextInt();
            kb.nextLine();

            Customer existingCustomer = customerDao.findCustomerById(customerNumber);

            if (existingCustomer == null) {
                System.out.println("This customer ID is available.");
                break;
            }

            System.out.println("This customer ID is already in use. Try again.");
        }


        System.out.print("Customer name: ");
        String customerName = kb.nextLine();

        System.out.print("Contact last name: ");
        String contactLastName = kb.nextLine();

        System.out.print("Contact first name: ");
        String contactFirstName = kb.nextLine();

        System.out.print("Phone: ");
        String phone = kb.nextLine();

        System.out.print("Address line 1: ");
        String addressLine1 = kb.nextLine();

        System.out.print("Address line 2: ");
        String addressLine2 = kb.nextLine();

        System.out.print("City: ");
        String city = kb.nextLine();

        System.out.print("State: ");
        String state = kb.nextLine();

        System.out.print("Postal code: ");
        String postalCode = kb.nextLine();

        System.out.print("Country: ");
        String country = kb.nextLine();

        System.out.print("Sales representative number: ");
        int salesRepEmployeeNumber = kb.nextInt();

        System.out.print("Credit limit: ");
        Double creditLimit = kb.nextDouble();

        Customer newCustomer = new Customer(customerNumber, customerName, contactLastName, contactFirstName, phone, addressLine1, addressLine2, city, state, postalCode, country, salesRepEmployeeNumber, creditLimit);

        boolean added = customerDao.addCustomer(newCustomer);

        if (added) {
            System.out.println("Customer added successfully.");
        } else {
            System.out.println("Customer could not be added.");
        }
    }
}
