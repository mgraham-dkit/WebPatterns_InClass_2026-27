package week06_testing_and_properties.persistence;

import org.jspecify.annotations.NonNull;
import week06_testing_and_properties.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private Connector connector;

    public ProductDaoImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public Product getProductByProductCode(String productCode) {
        if(productCode == null){
            throw new IllegalArgumentException("productCode cannot be null");
        }

        Product product = null;

        Connection conn = connector.getConnection();
        if (conn == null) {
            System.out.println("No connection available!");
            return null;
        }
            String sql = "SELECT * FROM products WHERE productCode = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, productCode);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        product = mapRow(rs);
                    }
                } catch (SQLException e) {
                    System.out.println("Exception: \"" + e.getMessage() + "\"");
                    System.out.println("\tIssue occurred when processing query or results for getProductByProductCode()");
                }
            } catch (SQLException e) {
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tCannot prepare statement for getProductByProductCode(): " + sql);
            }

        return product;
    }

    private static @NonNull Product mapRow(ResultSet rs) throws SQLException {
        Product product = new Product(
                rs.getString("productCode"),
                rs.getString("productName"),
                rs.getString("productLine"),
                rs.getString("productScale"),
                rs.getString("productVendor"),
                rs.getString("productDescription"),
                rs.getInt("quantityInStock"),
                rs.getDouble("buyPrice"),
                rs.getDouble("MSRP")
        );
        return product;
    }

    public List<Product> getAllProductsContainingKeyword(String keyword){
        if(keyword == null){
            throw new IllegalArgumentException("keyword cannot be null");
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            System.out.println("No connection available!");
            return null;
        }

        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE productDescription LIKE ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try(ResultSet rs = ps.executeQuery()) {
                // Loop through the result set
                while(rs.next()){
                    Product product = mapRow(rs);
                    products.add(product);
                }
            }catch(SQLException e){
                System.out.println("Exception: \"" + e.getMessage() + "\"");
                System.out.println("\tIssue occurred when processing query or results for getAllProductsContainingKeyword()");
            }
        }catch(SQLException e){
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare statement for getAllProductsContainingKeyword(): " + sql);
        }
        return products;
    }

    public Product deleteProductByCode(String prodCode){
        Product removed = getProductByProductCode(prodCode);
        if(removed == null){
            return removed;
        }

        Connection conn = connector.getConnection();
        if(conn == null){
            System.out.println("No connection available!");
            return null;
        }

        int deletedRows = 0;
        String sql = "DELETE FROM products where productCode = ?";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prodCode);
            deletedRows = ps.executeUpdate();

        }catch(SQLException e){
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare or execute statement for deleteProductByCode(): " + sql);
        }
        if(deletedRows == 0){
            removed = null;
        }

        return removed;
    }

    public boolean addProduct(Product p){
        if(p == null){
            throw new IllegalArgumentException("Cannot add a null Product to database");
        }
        Connection conn = connector.getConnection();
        if(conn == null){
            System.out.println("No connection available!");
            return false;
        }

        int addedRows = 0;
        String sql = "INSERT INTO products VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.productCode());
            ps.setString(2, p.productName());
            ps.setString(3, p.productLine());
            ps.setString(4, p.productScale());
            ps.setString(5, p.productVendor());
            ps.setString(6, p.productDescription());
            ps.setInt(7, p.quantityInStock());
            ps.setDouble(8, p.buyPrice());
            ps.setDouble(9, p.msrp());

            addedRows = ps.executeUpdate();

        }catch(SQLException e){
            System.out.println("Exception: \"" + e.getMessage() + "\"");
            System.out.println("\tCannot prepare statement for addProduct(): " + sql);
        }
        return addedRows == 1;
    }


    static void main(String[] args) {
        String propertiesFilename = "db_properties/database.properties";
        // Create connection manager object
        Connector connector = new MySqlConnector(propertiesFilename);ProductDao productDao = new ProductDaoImpl(connector);

        String productCode = "S10_4698";
        Product product = productDao.getProductByProductCode(productCode);
        System.out.println("Product #" + productCode + ": ");
        System.out.println(product);
    }
}
