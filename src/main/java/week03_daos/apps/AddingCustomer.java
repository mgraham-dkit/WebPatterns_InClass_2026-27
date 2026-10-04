package week03_daos.apps;

import week03_daos.entities.Customer;
import week03_daos.persistence.CustomerDao;
import week03_daos.persistence.CustomerDaoImpl;

import java.util.Scanner;

public class AddingCustomer {
    static void main() {
        Scanner kb = new Scanner(System.in);

        CustomerDao cdi = new CustomerDaoImpl();

        System.out.println("Please enter a desired ID: ");
        int inputID = kb.nextInt();

        boolean spaceAvailable = false;

        while(!spaceAvailable){

            if(cdi.findCustomerById(inputID) == null){
                System.out.println("Chosen ID is available!");
                spaceAvailable = true;
            }
            else{
                System.out.println("ID is already in use, please enter a new ID: ");
                inputID = kb.nextInt();
            }
        }

        // User inputting info

        kb.nextLine();
        System.out.println("Please enter Customer Name: ");
        String custName = kb.nextLine();
        System.out.println("Please enter Contact Last Name: ");
        String lastName = kb.nextLine();
        System.out.println("Please enter Contact First Name: ");
        String firstName = kb.nextLine();
        System.out.println("Please enter Phone Number: ");
        String phoneNum = kb.nextLine();
        System.out.println("Please enter Address Line 1: ");
        String addLine1 = kb.nextLine();
        System.out.println("Please enter Address Line 2: ");
        String addLine2 = kb.nextLine();
        System.out.println("Please enter City: ");
        String city = kb.nextLine();
        System.out.println("Please enter State: ");
        String state = kb.nextLine();
        System.out.println("Please enter Postal Code: ");
        String postalCode = kb.nextLine();
        System.out.println("Please enter Country: ");
        String country = kb.nextLine();
        System.out.println("Please enter Sales Rep Employee Number: ");
        int salesRepEmpNum = kb.nextInt();
        System.out.println("Please enter Credit Limit: ");
        double creditLimit = kb.nextDouble();


        Customer newCustomer = new Customer(inputID,custName,lastName,firstName,phoneNum,addLine1,addLine2,city,state,postalCode,country,salesRepEmpNum,creditLimit);

        cdi.addCustomer(newCustomer);
    }
}
