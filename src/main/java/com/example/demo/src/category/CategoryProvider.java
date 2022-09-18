package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    public List<GetCategoryRes> getCategories() throws BaseException {
        try {
            List<GetCategoryRes> getCategoryRes = categoryDao.getCategories();
            return getCategoryRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
