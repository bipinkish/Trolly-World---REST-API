package com.trollyworld.bootapp.service.impl;

import com.trollyworld.bootapp.dto.ProductDTO;
import com.trollyworld.bootapp.dto.ProductResponseDTO;
import com.trollyworld.bootapp.exception.APIException;
import com.trollyworld.bootapp.exception.ResourceNotFoundException;
import com.trollyworld.bootapp.model.Category;
import com.trollyworld.bootapp.model.Product;
import com.trollyworld.bootapp.repository.CategoryRepository;
import com.trollyworld.bootapp.repository.ProductRepositpory;
import com.trollyworld.bootapp.service.ProductService;
import com.trollyworld.bootapp.utils.FilesUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Value("${trollyworld.image-dest}")
    String destFolder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepositpory productRepositpory;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageContent = productRepositpory.findAll(pageRequest);
        List<Product> productList = pageContent.getContent();
        if (productList.isEmpty()) {
            throw new APIException("No Products found!");
        }
        return getProductResponseDTO(productList, pageContent);
    }

    @Override
    public ProductResponseDTO getAllProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category productCategory = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageContent = productRepositpory.findByCategory(productCategory, pageRequest);
        List<Product> productList = pageContent.getContent();
        if (productList.isEmpty()) {
            throw new APIException("No Products found for the category '" + productCategory.getCategoryName() + "'!");
        }
        return getProductResponseDTO(productList, pageContent);
    }

    @Override
    public ProductResponseDTO getAllProductsBySearchTerm(String searchTerm, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> pageContent = productRepositpory.findByPrdNameLikeIgnoreCase("%" + searchTerm + "%", pageRequest);
        List<Product> productList = pageContent.getContent();
        if (productList.isEmpty()) {
            throw new APIException("No Products found for the search term '" + searchTerm + "'");
        }
        return getProductResponseDTO(productList, pageContent);
    }

    private ProductResponseDTO getProductResponseDTO(List<Product> productList, Page<Product> pageContent) {
        List<ProductDTO> convertedProducts = productList.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        return ProductResponseDTO.builder()
                .data(convertedProducts)
                .pageNumber(pageContent.getNumber())
                .pageSize(pageContent.getSize())
                .totalElements(pageContent.getTotalElements())
                .totalPages(pageContent.getTotalPages())
                .lastPage(pageContent.isLast())
                .build();
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category productCategory = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Product> products = productCategory.getProducts();
        Optional<Product> optionalDuplicateProduct = products.stream().filter(product -> product.getPrdName().equals(productDTO.getPrdName())).findFirst();
        if (optionalDuplicateProduct.isPresent()) {
            throw new APIException("Product with name '" + productDTO.getPrdName() + "' already exists!");
        }
        Product product = modelMapper.map(productDTO, Product.class);
        double specialPrice = product.getActlPrice() - (product.getDiscount() * 0.01 * product.getActlPrice());
        product.setSplPrice(specialPrice);
        product.setCategory(productCategory);
        Product savedProduct = productRepositpory.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepositpory.findById(productId);
        Product productToBeDeleted = optionalProduct
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepositpory.delete(productToBeDeleted);
        return modelMapper.map(productToBeDeleted, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepositpory.findById(productId);
        Product productToBeDeleted = optionalProduct
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productToBeDeleted.setActlPrice(productDTO.getActlPrice());
        productToBeDeleted.setDiscount(productDTO.getDiscount());
        productToBeDeleted.setImage(productDTO.getImage());
        productToBeDeleted.setPrdDesc(productDTO.getPrdDesc());
        productToBeDeleted.setPrdName(productDTO.getPrdName());
        productToBeDeleted.setQuantity(productDTO.getQuantity());
        Product updatedProduct = productRepositpory.save(productToBeDeleted);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateImageForProduct(Long productId, MultipartFile image) throws IOException {
        Product product = productRepositpory.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        String uploadedFileName = FilesUtil.uploadFile(destFolder, image);
        product.setImage(uploadedFileName);
        Product savedProduct = productRepositpory.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }
}
