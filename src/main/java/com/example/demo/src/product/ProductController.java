package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.product.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.SUCCESS_CREATE_WISH;
import static com.example.demo.config.BaseResponseStatus.SUCCESS_UPDATE_VIEW_COUNT;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private final ProductProvider productProvider;
    @Autowired private final ProductService productService;

    public ProductController(ProductProvider productProvider, ProductService productService) {
        this.productProvider = productProvider;
        this.productService = productService;
    }

    /* 전체 상품 조회 - 홈화면 */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProdRes>> getProducts(@RequestParam(required = false) String title) throws BaseException {
        try {
            if (title == null) {
                List<GetProdRes> getProdRes = productProvider.getProducts();
                return new BaseResponse<>(getProdRes);
           }
           List<GetProdRes> getProdRes = productProvider.getProductsByTitle(title);
           return new BaseResponse<>(getProdRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
       }
    }

    /* 상품 상세 조회 */
    @ResponseBody
    @GetMapping("/{productIdx}")
    public BaseResponse<List<GetProdDetailRes>> getProduct(@PathVariable("productIdx") int productIdx) {
        try {
            List<GetProdDetailRes> getProdRes = productProvider.getProduct(productIdx);
            return new BaseResponse<>(getProdRes);
        } catch(BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 게시글 생성*/
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostProdRes> createProduct(@RequestBody PostProdReq postProdReq) {
        try {
            PostProdRes postProductRes = productService.createProduct(postProdReq);
            return new BaseResponse<>(postProductRes);
        } catch(BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 찜 설정 */
    @ResponseBody
    @PostMapping("/wish")
    public BaseResponse<String> createWish(@RequestBody PostWishReq postWishReq) {
        try {
            productService.createWish(postWishReq);
            return new BaseResponse<>(SUCCESS_CREATE_WISH);
        } catch(BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 게시물 수정 */
    @ResponseBody
    @PatchMapping("/{productIdx}")
    public BaseResponse<String> modifyProductInfo(@PathVariable("productIdx") int productIdx, @RequestBody PatchProdReq patchProdReq) {
        try {
            patchProdReq = new PatchProdReq(productIdx,
                    patchProdReq.getTitle(),
                    patchProdReq.getDescription(),
                    patchProdReq.getPrice(),
                    patchProdReq.getCanProposal(),
                    patchProdReq.getCategoryIdx()
            );
            productService.modifyProductInfo(patchProdReq);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_MODIFY_PRODUCT);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 조회수 증가 */
    @ResponseBody
    @PatchMapping("/{productIdx}/viewCount")
    public BaseResponse<String> updateViewCount(@PathVariable("productIdx") int productIdx) {
        try {
            productService.updateViewCount(productIdx);
            return new BaseResponse<>(SUCCESS_UPDATE_VIEW_COUNT);
        } catch (BaseException exception) {
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /* 상품 게시물 삭제 */
    @ResponseBody
    @DeleteMapping("/{productIdx}")
    public BaseResponse<String> deleteProduct(@PathVariable("productIdx") int productIdx) {
        try {
            productService.deleteProduct(productIdx);
            return new BaseResponse<>(BaseResponseStatus.SUCCESS_DELETE_PRODUCT);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
