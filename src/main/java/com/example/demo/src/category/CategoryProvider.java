package com.example.demo.src.category;

import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryProvider {

    private final CategoryDao categoryDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CategoryProvider(CategoryDao categoryDao, JwtService jwtService) {
        this.categoryDao = categoryDao;
        this.jwtService = jwtService;
    }
}
