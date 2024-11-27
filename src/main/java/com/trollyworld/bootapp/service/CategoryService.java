package com.trollyworld.bootapp.service;

import com.trollyworld.bootapp.dto.CategoryDTO;
import com.trollyworld.bootapp.dto.CategoryResponseDTO;
import com.trollyworld.bootapp.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO getCategories(Integer pageNumber,Integer pageSize,String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO category);
}
