package week08_services.persistence;

public class JdbcTransactionManager implements TransactionManager {
    private final Connector connector;

    public JdbcTransactionManager(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void begin() {
        connector.getConnection();
        connector.disableConnectionAutoCommit();
    }

    @Override
    public void commit() {
        connector.commit();
    }

    @Override
    public void rollback() {
        connector.rollback();
    }
}
