package week06_testing_and_properties.entities;

import java.util.Objects;

/*
productCode varchar(15) NOT NULL,
  productName varchar(70) NOT NULL,
  productLine varchar(50) NOT NULL,
  productScale varchar(10) NOT NULL,
  productVendor varchar(50) NOT NULL,
  productDescription text NOT NULL,
  quantityInStock smallint(6) NOT NULL,
  buyPrice double NOT NULL,
  MSRP double NOT NULL,
 */
public record Product(String productCode, String productName, String productLine,
                      String productScale, String productVendor, String productDescription,
                      int quantityInStock, double buyPrice, double msrp) {
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product product)) return false;

        return productCode.equals(product.productCode);
    }

    @Override
    public int hashCode() {
        return productCode.hashCode();
    }

    public boolean deepEquals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return Double.compare(msrp, product.msrp) == 0 && Double.compare(buyPrice, product.buyPrice) == 0 && quantityInStock == product.quantityInStock && Objects.equals(productCode, product.productCode) && Objects.equals(productName, product.productName) && Objects.equals(productLine, product.productLine) && Objects.equals(productScale, product.productScale) && Objects.equals(productVendor, product.productVendor) && Objects.equals(productDescription, product.productDescription);
    }
}
