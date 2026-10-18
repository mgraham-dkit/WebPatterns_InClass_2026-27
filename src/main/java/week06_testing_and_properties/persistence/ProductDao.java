package week06_testing_and_properties.persistence;


import week06_testing_and_properties.entities.Product;

import java.util.List;

public interface ProductDao {
    Product getProductByProductCode(String productCode);
    List<Product> getAllProductsContainingKeyword(String keyword);
    Product deleteProductByCode(String code);
    boolean addProduct(Product p);
}
