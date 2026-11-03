package week07_logging.dtos;

public record OrderDetailDto(ProductDto product, int quantity, double price) {
}
