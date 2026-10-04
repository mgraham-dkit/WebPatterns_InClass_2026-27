package Intro_ex.entities;

import java.util.Objects;

public class ProductDetails {

    //Initialize
    private String productName;
    private String productCode;
    private String productLine;
    private String productScale;
    private String productVendor;
    private String productDescription;
    private  int quantityInStock;
    private double buyPrice;
    private  double MSRP;

    //Constructor
    public ProductDetails(String productName, String productCode, String productLine, String productScale, String productVendor, String productDescription, int quantityInStock, double buyPrice, double MSRP) {
        this.productName = productName;
        this.productCode = productCode;
        this.productLine = productLine;
        this.productScale = productScale;
        this.productVendor = productVendor;
        this.productDescription = productDescription;
        this.quantityInStock = quantityInStock;
        this.buyPrice = buyPrice;
        this.MSRP = MSRP;
    }

    // Getters
    public String getProductName() {
        return productName;
    }
    public String getProductCode() {
        return productCode;
    }
    public String getProductLine() {
        return productLine;
    }
    public String getProductScale() {
        return productScale;
    }
    public String getProductVendor() {
        return productVendor;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public int getQuantityInStock() {
        return quantityInStock;
    }
    public double getBuyPrice() {
        return buyPrice;
    }
    public double getMSRP() {
        return MSRP;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetails that = (ProductDetails) o;
        return Objects.equals(productCode, that.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productCode);
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
                "productName='" + productName + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productLine='" + productLine + '\'' +
                ", productScale='" + productScale + '\'' +
                ", productVendor='" + productVendor + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", buyPrice=" + buyPrice +
                ", MSRP=" + MSRP +
                '}'+"\n";
    }
}
