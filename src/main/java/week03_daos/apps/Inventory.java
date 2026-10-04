package week03_daos.apps;

import week03_daos.persistence.ProductDaoImpl;

public class Inventory {
    static void main() {

        String productCode = "SA_8713";
        String productName = "2013 Kia";
        String productLine = "Classic Cars";
        String productScale = "1:10";
        String productVendor = "GabGop";
        String ProductDescription = "Black Kia car";
        int quantityInstock = 25;
        double buyPrice = 500.55;
        double MSRP = 600.55;


        if (ProductDaoImpl.addProduct(productCode, productName, productLine, productScale, productVendor, ProductDescription, quantityInstock, buyPrice, MSRP)) {
            System.out.println("Product added");
        }
        else {
            System.out.println("Product was not added");
        }
    }
}
