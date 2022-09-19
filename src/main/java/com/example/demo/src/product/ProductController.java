package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.GetProdRes;
import com.example.demo.src.product.model.PostProdReq;
import com.example.demo.src.product.model.PostProdRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/products")
public class ProductController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    /* 상품 게시글 생성 - 더미데이터 */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProdRes> createProduct(@RequestBody PostProdReq postProdReq) {
        try {
            PostProdRes postProdRes = productService.createProduct(postProdReq);
            return new BaseResponse<>(postProdRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 검색 키워드별 조회 */
    @ResponseBody
    @GetMapping("/search")
    public BaseResponse<List<GetProdRes>> getProductsByKeyword(@RequestParam String keyword) {
        try {
            List<GetProdRes> productsByKeyword = productProvider.getProductsByKeyword(keyword);
            return new BaseResponse<>(productsByKeyword);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 카테고리별 상품 조회*/
    @ResponseBody
    @GetMapping("/category/{categoryIdx}")
    public BaseResponse<List<GetProdRes>> getProductsByCategory(@PathVariable("categoryIdx") int categoryIdx) {
        try {
            List<GetProdRes> productsByCategory = productProvider.getProductsByCategory(categoryIdx);
            return new BaseResponse<>(productsByCategory);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
