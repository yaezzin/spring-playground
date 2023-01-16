package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.GetProdDetailRes;
import com.example.demo.src.product.model.GetProdRes;
import com.example.demo.src.product.model.PostProdReq;
import com.example.demo.src.product.model.PostProdRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, UserProvider userProvider, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.userProvider = userProvider;
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
    public BaseResponse<List<GetProdRes>> getProductsByKeyword(@RequestParam(required = false) String keyword) {
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

    /* 상품 상세 조회 */
    @ResponseBody
    @GetMapping("/{productIdx}")
    public BaseResponse<GetProdDetailRes> getProductDetail(@PathVariable("productIdx") int productIdx) {
        try {
            GetProdDetailRes getProdDetailRes = productProvider.getProductDetail(productIdx);
            return new BaseResponse<>(getProdDetailRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 필터링 - 키워드 유무에 따른 가격 높은순, 낮은순 정렬 */
    @ResponseBody
    @GetMapping("/filter")
    public BaseResponse<List<GetProdRes>> getProductsByPrice(@RequestParam(required = false) String keyword,
                                                             @RequestParam(required = false) Integer categoryIdx,
                                                             @RequestParam int price) {

        // 상품 필터링의 경우 검색 전후로 가능하므로 keyword를 request 파라미터로 전달함
        try {
            if (keyword == null) { // 키워드가 없으면 -> 카테고리 + 가격 필터링
                if (price == 0) { // price 값이 0 이면 높은 가격 순으로 필터링
                    List<GetProdRes> productsByHighPrice = productProvider.getProductsByHighPrice(categoryIdx);
                    return new BaseResponse<>(productsByHighPrice);
                } else {
                    List<GetProdRes> productsByLowPrice = productProvider.getProductsByLowPrice(categoryIdx);
                    return new BaseResponse<>(productsByLowPrice);
                }

            } else { // 키워드가 있으면 키워드 + 가격 필터링
                if (price == 0) {
                    List<GetProdRes> productsByKeywordAndHighPrice = productProvider.getProductsByKeywordAndHighPrice(keyword);
                    return new BaseResponse<>(productsByKeywordAndHighPrice);
                } else {
                    List<GetProdRes> productsByKeywordAndLowPrice = productProvider.getProductsByKeywordAndLowPrice(keyword);
                    return new BaseResponse<>(productsByKeywordAndLowPrice);
                }
            }
        }  catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /* 상품 찜 등록 */
    @ResponseBody
    @PostMapping("/{productIdx}/wish")
    public BaseResponse<String> createProductWish(@PathVariable("productIdx") int productIdx) {
        try {
            int userIdx = jwtService.getUserIdx();
            if (userProvider.checkUser(userIdx) == 0) {
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if (productProvider.checkProductExist(productIdx) == 0) {
                return new BaseResponse<>(EMPTY_PRODUCT);
            }
            // 이미 좋아요를 눌렀으면
            if (productProvider.checkProductWishExist(productIdx, userIdx) != 0) {
                return new BaseResponse<>(POST_PRODUCT_WISH_EXIST);
            }
            productService.createProductWish(userIdx, productIdx);
            return new BaseResponse<>(SUCCESS_CREATE_WISH);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 찜 해제*/
    @ResponseBody
    @PutMapping("/{productIdx}/wish")
    public BaseResponse<String> deleteProductWish(@PathVariable("productIdx") int productIdx) {
        try {
            int userIdx = jwtService.getUserIdx();
            if (userProvider.checkUser(userIdx) == 0) {
                return new BaseResponse<>(USERS_EMPTY_USER_ID);
            }
            if (productProvider.checkProductExist(productIdx) == 0) {
                return new BaseResponse<>(EMPTY_PRODUCT);
            }
            // 좋아요를 누르지 않았으면
            if (productProvider.checkProductWishExist(productIdx, userIdx) == 0) {
                return new BaseResponse<>(POST_PRODUCT_WISH_NOT_EXIST);
            }
            productService.deleteProductWish(userIdx, productIdx);
            return new BaseResponse<>(SUCCESS_DELETE_WISH);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
