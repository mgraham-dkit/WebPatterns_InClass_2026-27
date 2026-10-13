package week05_repositories.dtos;

public record OrderDetailDto(ProductDto product, int quantity, double price) {
}
