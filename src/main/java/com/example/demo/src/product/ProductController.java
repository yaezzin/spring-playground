package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.GetProdDetailRes;
import com.example.demo.src.product.model.GetProdRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/products")
public class ProductController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private final ProductProvider productProvider;
    //@Autowired private final ProductService productService;

    public ProductController(ProductProvider productProvider) {
        this.productProvider = productProvider;
    }


    /* 전체 상품 조회 - 홈화면 */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetProdRes>> getProducts(@RequestParam String title) {
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


}
