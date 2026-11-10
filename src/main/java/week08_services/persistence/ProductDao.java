package week08_services.persistence;


import week08_services.entities.Product;

import java.util.List;

public interface ProductDao {
    Product getProductByProductCode(String productCode);
    List<Product> getAllProductsContainingKeyword(String keyword);
    Product deleteProductByCode(String code);
    boolean addProduct(Product p);
}
