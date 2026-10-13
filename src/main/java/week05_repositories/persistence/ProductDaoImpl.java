package week05_repositories.persistence;

import org.jspecify.annotations.NonNull;
import week05_repositories.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDaoImpl implements ProductDao {
    private Connector connector;

    public ProductDaoImpl(Connector connector){
        this.connector = connector;
    }

    @Override
    public Product getProductByProductCode(String productCode) {
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

    static void main(String[] args) {
        Connector connector = new MySqlConnector();
        ProductDao productDao = new ProductDaoImpl(connector);

        String productCode = "S10_4698";
        Product product = productDao.getProductByProductCode(productCode);
        System.out.println("Product #" + productCode + ": ");
        System.out.println(product);
    }
}
