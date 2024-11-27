package com.trollyworld.bootapp.service.impl;

import com.trollyworld.bootapp.dto.CategoryDTO;
import com.trollyworld.bootapp.dto.CategoryResponseDTO;
import com.trollyworld.bootapp.exception.APIException;
import com.trollyworld.bootapp.exception.ResourceNotFoundException;
import com.trollyworld.bootapp.model.Category;
import com.trollyworld.bootapp.repository.CategoryRepository;
import com.trollyworld.bootapp.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public CategoryResponseDTO getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize,sortByAndOrder);
        Page<Category> pageContent = categoryRepository.findAll(pageRequest);
        List<Category> categories = pageContent.getContent();
        if (categories.isEmpty()) {
            throw new APIException("No Categories found!");
        }
        List<CategoryDTO> convertedCategories = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        return CategoryResponseDTO.builder()
                .data(convertedCategories)
                .pageNumber(pageContent.getNumber())
                .pageSize(pageContent.getSize())
                .totalElements(pageContent.getTotalElements())
                .totalPages(pageContent.getTotalPages())
                .last(pageContent.isLast())
                .build();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (optionalCategory.isPresent()) {
            throw new APIException("Category with name '" + categoryDTO.getCategoryName() + "' already exists!");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category categoryToBeDeleted = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(categoryToBeDeleted);
        return modelMapper.map(categoryToBeDeleted, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category categoryToBeUpdated = optionalCategory
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryToBeUpdated.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(categoryToBeUpdated);
        return modelMapper.map(updatedCategory, CategoryDTO.class);
    }
}
