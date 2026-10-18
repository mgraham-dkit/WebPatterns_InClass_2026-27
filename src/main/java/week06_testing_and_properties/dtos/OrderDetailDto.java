package week06_testing_and_properties.dtos;

public record OrderDetailDto(ProductDto product, int quantity, double price) {
}
