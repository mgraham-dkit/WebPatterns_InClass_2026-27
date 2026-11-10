package week08_services.persistence;

public interface TransactionManager {
    void begin();
    void commit();
    void rollback();
}