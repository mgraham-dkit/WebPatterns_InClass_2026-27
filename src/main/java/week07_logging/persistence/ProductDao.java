package week07_logging.persistence;


import week07_logging.entities.Product;

import java.util.List;

public interface ProductDao {
    Product getProductByProductCode(String productCode);
    List<Product> getAllProductsContainingKeyword(String keyword);
    Product deleteProductByCode(String code);
    boolean addProduct(Product p);
}
