package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.src.category.model.PatchCategoryReq;
import com.example.demo.src.category.model.PostCategoryReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CategoryService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CategoryDao categoryDao;
    private final CategoryProvider categoryProvider;
    private final JwtService jwtService;

    @Autowired
    public CategoryService(CategoryDao categoryDao, CategoryProvider categoryProvider, JwtService jwtService) {
        this.categoryDao = categoryDao;
        this.categoryProvider = categoryProvider;
        this.jwtService = jwtService;
    }

    public void createCategory(PostCategoryReq postCategoryReq) throws BaseException {
       // try {
            int result = categoryDao.createCategory(postCategoryReq);
            if (result == 0) {
                throw new BaseException(CREATE_FAIL_CATEGORY);
            }
       // } catch (Exception exception) {
       //     throw new BaseException(DATABASE_ERROR);
       // }
    }

    public void modifyCategory(PatchCategoryReq patchCategoryReq) throws BaseException {
        try {
            int result = categoryDao.modifyCategory(patchCategoryReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_CATEGORY);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deleteCategory(int categoryIdx) throws BaseException {
        try {
            int result = categoryDao.deleteCategory(categoryIdx);
            if (result == 0) {
                throw new BaseException(DELETE_FAIL_CATEGORY);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
