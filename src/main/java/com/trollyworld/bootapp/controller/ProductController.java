package com.trollyworld.bootapp.controller;

import com.trollyworld.bootapp.dto.ProductDTO;
import com.trollyworld.bootapp.dto.ProductResponseDTO;
import com.trollyworld.bootapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponseDTO> getProducts(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "prdId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/public/category/{categoryId}/products")
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "prdId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/public/products/{searchTerm}")
    public ResponseEntity<ProductResponseDTO> getProductsByCategory(
            @PathVariable String searchTerm,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "prdId") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        return new ResponseEntity<>(productService.getAllProductsBySearchTerm(searchTerm, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.FOUND);
    }

    @PostMapping("/admin/category/{categoryId}/product")
    ResponseEntity<ProductDTO> createProduct(@PathVariable Long categoryId,
                                             @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO product = productService.createProduct(productDTO, categoryId);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteCategory(@PathVariable Long productId) {
        ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteCategory(@Valid @PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateImage(@Valid @PathVariable Long productId, @RequestParam MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateImageForProduct(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
