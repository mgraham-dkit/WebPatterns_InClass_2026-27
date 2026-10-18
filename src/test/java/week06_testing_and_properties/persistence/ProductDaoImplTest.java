package week06_testing_and_properties.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import week06_testing_and_properties.entities.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoImplTest {
    private ProductDao productDao;
    private Connector connector;
    private Product product;

    // Point connection manager to TEST version of database
    private static String propertiesPath = "db_properties/database_test.properties";

    @BeforeEach
    void setupDao(){
        // Reset connection for each test
        connector = new MySqlConnector(propertiesPath);
        // Create a connection to be used (do it here so we can standardise how it's set up)
        connector.getConnection();
        // Ensure the connection will not save changes to the database
        connector.disableConnectionAutoCommit();

        // Create dao object to be tested
        productDao = new ProductDaoImpl(connector);
    }

    /*
        Add product tests:
            - Product added
            - Product not added - existing productCode supplied
            - Product not added - null productCode supplied
            - Invalid (null) product supplied
     */
    static Stream<Arguments> productProvider(){
        // Create objects to be used in the parameterised add tests
        Product newProduct = new Product("Dummy Test Code",
                "TestAdd Product", "Ships",
                "1:10", "Dummy Vendor",
                "Dummy item to be inserted", 100, 5.59, 28.89);
        Product existingProduct = new Product("S12_3990",
                "1970 Plymouth Hemi Cuda", "Classic Cars",
                "1:12", "Studio M Art Models",
                "Very detailed 1970 Plymouth Cuda model in 1:12 scale. The Cuda is generally accepted as one of the fastest original muscle cars from the 1970s. This model is a reproduction of one of the orginal 652 cars built in 1970. Red color.", 5663, 31.92, 79.8);
        Product nullProductCodeProduct = new Product(null,
                "Product with existing Product code", "Ships",
                "1:10", "Dummy Vendor",
                "Dummy item to be inserted", 100, 5.59, 28.89);
        return Stream.of(
                Arguments.of(newProduct, true, newProduct),
                Arguments.of(existingProduct, false, existingProduct),
                Arguments.of(nullProductCodeProduct, false, null)
        );
    }

    @ParameterizedTest
    @MethodSource("productProvider")
    void addProduct_ProductObjectSupplied(Product p, boolean expectedAddResult,
                                             Product expectedGetResult) {
        boolean result = productDao.addProduct(p);

        assertEquals(expectedAddResult, result);

        if(p.productCode() != null){
            Product insertedProductResult = productDao.getProductByProductCode(p.productCode());
            assertEquals(expectedGetResult, insertedProductResult);
        }
    }

    @Test
    void addProduct_NullObjectSupplied() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    productDao.addProduct(null);
                }, "Expected IllegalArgumentException when attempting to add null Product to database"
        );
    }

    /*
        getProductByProductCode tests:
            - Match found
            - No matches found (valid productCode supplied)
            - Invalid (null) productCode supplied
     */
    static Stream<Arguments> productCodeProvider(){
        // Create objects to be used in the parameterised getProductByProductCode tests
        String existingProductCode = "S12_3990";
        Product matchingProduct = new Product("S12_3990",
                "1970 Plymouth Hemi Cuda", "Classic Cars",
                "1:12", "Studio M Art Models",
                "Very detailed 1970 Plymouth Cuda model in 1:12 scale. The Cuda is generally accepted as one of the fastest original muscle cars from the 1970s. This model is a reproduction of one of the orginal 652 cars built in 1970. Red color.", 5663, 31.92, 79.8);
        String nonexistentProductCode = "ABC123";
        return Stream.of(
                Arguments.of(existingProductCode, matchingProduct),
                Arguments.of(nonexistentProductCode, null)
        );
    }

    @ParameterizedTest
    @MethodSource("productCodeProvider")
    void getProductByProductCode_ProductCodeSupplied(String productCode, Product expectedGetResult) {
        Product result = productDao.getProductByProductCode(productCode);

        // If we're expecting null, confirm the result was null
        if(expectedGetResult == null){
            assertNull(result);
        }else {
            // Otherwise confirm the two objects match on ALL fields
            assertTrue(expectedGetResult.deepEquals(result));
        }
    }

    @Test
    void getProductByProductCode_NullProductCodeSupplied() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    productDao.getProductByProductCode(null);
                }, "Expected IllegalArgumentException when null productCode supplied to getProductByProductCode()"
        );
    }

    /*
    getAllProductsContainingKeyword tests:
    - One match found
    - Multiple matches found
    - No matches found (valid keyword supplied)
    - Invalid (null) keyword supplied
 */
    static Stream<Arguments> productKeywordProvider(){
        // Create objects to be used in the parameterised getAllProductsContainingKeyword tests
        String existingProductKeywordOneMatch = "original muscle";
        Product matchingProduct = new Product("S12_3990",
                "1970 Plymouth Hemi Cuda", "Classic Cars",
                "1:12", "Studio M Art Models",
                "Very detailed 1970 Plymouth Cuda model in 1:12 scale. The Cuda is generally accepted as one of the fastest original muscle cars from the 1970s. This model is a reproduction of one of the orginal 652 cars built in 1970. Red color.", 5663, 31.92, 79.8);
        List<Product> singleResultList = new ArrayList<>();
        singleResultList.add(matchingProduct);

        String existingProductKeywordMultipleMatches = "197";
        List<Product> multiResultList = new ArrayList<>();
        Product cudaMatch = new Product("S12_3990",
                "1970 Plymouth Hemi Cuda", "Classic Cars",
                "1:12", "Studio M Art Models",
                "Very detailed 1970 Plymouth Cuda model in 1:12 scale. The Cuda is generally accepted as one of the fastest original muscle cars from the 1970s. This model is a reproduction of one of the orginal 652 cars built in 1970. Red color.", 5663, 31.92, 79.8);
        Product granTorinoMatch = new Product("S18_3482",
                "1976 Ford Gran Torino", "Classic Cars",
                "1:18", "Gearbox Collectibles",
                "Highly detailed 1976 Ford Gran Torino \"Starsky and Hutch\" diecast model. Very well constructed and" +
                        " painted in red and white patterns.", 9127, 73.49, 146.99);
        Product albinoRenaultMatch = new Product("S24_3371",
                "1971 Alpine Renault 1600s", "Classic Cars",
                "1:24", "Welly Diecast Productions",
                "This 1971 Alpine Renault 1600s replica Features opening doors, superb detail and craftsmanship, working steering system, opening forward compartment, opening rear trunk with removable spare, 4 wheel independent spring suspension as well as factory baked enamel finish.", 7995, 38.58, 61.23);
        multiResultList.add(cudaMatch);
        multiResultList.add(granTorinoMatch);
        multiResultList.add(albinoRenaultMatch);

        String noKeywordMatches = "java";
        List<Product> noMatches = new ArrayList<>();
        return Stream.of(
                Arguments.of(existingProductKeywordOneMatch, singleResultList),
                Arguments.of(existingProductKeywordMultipleMatches, multiResultList),
                Arguments.of(noKeywordMatches, noMatches)
        );
    }

    @ParameterizedTest
    @MethodSource("productKeywordProvider")
    void getAllProductsContainingKeyword_KeywordSupplied(String keyword, List<Product> expectedResults) {
        List<Product> result = productDao.getAllProductsContainingKeyword(keyword);

        // Confirm the sizes match
        assertEquals(expectedResults.size(), result.size(), "Keyword search result lists sizes do not match");

        // Confirm the contents match
        for (int i = 0; i < expectedResults.size(); i++) {
            Product currentExpected = expectedResults.get(i);
            // Otherwise confirm the two objects match on ALL fields
            assertTrue(currentExpected.deepEquals(result.get(i)));
        }
    }

    @Test
    void getAllProductsContainingKeyword_NullKeywordSupplied() {
        assertThrows(IllegalArgumentException.class,
                () -> {
                    productDao.getAllProductsContainingKeyword(null);
                }, "Expected IllegalArgumentException when null productCode supplied to getAllProductsContainingKeyword()"
        );
    }
}