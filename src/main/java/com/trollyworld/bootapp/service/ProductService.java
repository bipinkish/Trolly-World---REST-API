package com.trollyworld.bootapp.service;

import com.trollyworld.bootapp.dto.ProductDTO;
import com.trollyworld.bootapp.dto.ProductResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponseDTO getAllProductsBySearchTerm(String searchTerm, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO createProduct(ProductDTO productDTO, Long categoryId);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO updateImageForProduct(@Valid Long productId, MultipartFile image) throws IOException;
}
