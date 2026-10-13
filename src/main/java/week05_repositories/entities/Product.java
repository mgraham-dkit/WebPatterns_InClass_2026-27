package week05_repositories.entities;
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
}
