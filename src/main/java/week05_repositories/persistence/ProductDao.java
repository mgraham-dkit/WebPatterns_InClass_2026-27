package week05_repositories.persistence;


import week05_repositories.entities.Product;


public interface ProductDao {
    Product getProductByProductCode(String productCode);
}
