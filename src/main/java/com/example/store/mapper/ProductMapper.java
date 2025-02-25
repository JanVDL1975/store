package com.example.store.mapper;
import org.mapstruct.Mapper;
import com.example.store.dto.ProductDTO;
import com.example.store.entity.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public class ProductMapper {

    // Map Product to ProductDTO with order IDs represented as a string
    public ProductDTO productToProductDTO(Product product) {
        String orderIds = Optional.ofNullable(product.getOrderIds()).orElse("");
        return new ProductDTO(product.getId(), product.getDescription(), orderIds);
    }

    // Map list of products to list of ProductDTOs
    public List<ProductDTO> productsToProductDTOs(List<Product> products) {
        return products.stream()
                .map(this::productToProductDTO)
                .collect(Collectors.toList());
    }

    // Map ProductDTO to Product entity
    public Product productDTOToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setOrderIds(productDTO.getOrderIds()); // Map orderIds directly
        return product;
    }
}


