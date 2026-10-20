package week06_testing_and_properties.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import week06_testing_and_properties.entities.Customer;

import java.sql.Connection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoImplTest {
    private CustomerDao customerDao;

    @BeforeEach
    public void configureCustomerDao(){
        Connector connector = new MySqlConnector("db_properties/database_test.properties");

        connector.getConnection();
        connector.disableConnectionAutoCommit();

        customerDao = new CustomerDaoImpl(connector);
    }

    /*
        Possible test cases:
            - number > 0 - valid match
            - number > 0 - no match
            - number < 0 - invalid
            - number == 0 - invalid
     */

    public static Stream<Arguments> provideCustomerNumbers(){
        int validCustomer = 103;
        Customer validExpectedResult = new Customer(validCustomer, "Atelier graphique", "Schmitt", "Carine ", "40.32" +
                ".2555", "54, rue Royale", null, "Nantes", null, "44000", "France", 1102, 21000);
        int invalidCustomer = 87;
        Customer invalidExpectedResult = null;
        int negative = -1;
        Customer negativeExpectedResult = null;
        int zero = 0;
        Customer zeroExpectedResult = null;

        return Stream.of(
                Arguments.of(validCustomer, validExpectedResult),
                Arguments.of(invalidCustomer, invalidExpectedResult),
                Arguments.of(negative, negativeExpectedResult),
                Arguments.of(zero, zeroExpectedResult)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCustomerNumbers")
    void getCustomerByCustomerNumber(int input, Customer expectedResult) {
        Customer result = customerDao.getCustomerByCustomerNumber(input);

        if(expectedResult == null){
            assertNull(result);
        }else {
            assertTrue(result.deepEquals(expectedResult),
                    "Retrieved Customer for " + input + " did not match expected value \nExpected: " + expectedResult
                            + "\nActual: " + result);
        }
    }
}