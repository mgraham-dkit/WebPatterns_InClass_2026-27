package week08_services.dtos;

public record OrderDetailDto(ProductDto product, int quantity, double price) {
}
