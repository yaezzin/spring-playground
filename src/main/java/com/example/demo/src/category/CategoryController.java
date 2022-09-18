package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.PatchCategoryReq;
import com.example.demo.src.category.model.PostCategoryReq;
import com.example.demo.src.category.model.GetCategoryRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.SUCCESS_CREATE_CATEGORY;
import static com.example.demo.config.BaseResponseStatus.SUCCESS_MODIFY_CATEGORY;

@RestController
@RequestMapping("/app/categories")
public class CategoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryProvider categoryProvider;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryProvider categoryProvider, CategoryService categoryService, JwtService jwtService){
        this.categoryProvider = categoryProvider;
        this.categoryService = categoryService;
        this.jwtService = jwtService;
    }

    /* 카테고리 생성 */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createCategory(@RequestBody PostCategoryReq postCategoryReq) {
        try{
            categoryService.createCategory(postCategoryReq);
            return new BaseResponse<>(SUCCESS_CREATE_CATEGORY);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 카테고리 전체 조회*/
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getCategories() {
        try {
            List<GetCategoryRes> getCategoryRes = categoryProvider.getCategories();
            return new BaseResponse<>(getCategoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 카테고리 이름 수정 */
    @ResponseBody
    @PatchMapping("/{categoryIdx}")
    public BaseResponse<String> modifyCategory(@PathVariable("categoryIdx") int categoryIdx, @RequestBody PatchCategoryReq patchCategoryReq) {
        try {
            patchCategoryReq = new PatchCategoryReq(categoryIdx, patchCategoryReq.getName());
            categoryService.modifyCategory(patchCategoryReq);
            return new BaseResponse<>(SUCCESS_MODIFY_CATEGORY);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 카테고리 삭제 */

}
