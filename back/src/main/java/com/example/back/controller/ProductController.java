package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.Product;
import com.example.back.jpa.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list/{productId}") //상품 조회
    public ProductDto getProductList(@PathVariable Long productId) {

        return productService.getProductList(productId);
    }

    //전체 상품조회
    @GetMapping("/list")
    public ResponseEntity<?> findAllItem(ProductSearchDto productSearchDto) {

        List<MainProductDto> items = productService.getAllProduct(productSearchDto);

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/user") //user에 따른 상품 조회
    public ResponseEntity<List<MainProductDto>> getProductListById(@AuthenticationPrincipal PrincipalDetail principalDetail) {

//        List<ProductListDto> pdDtoList = productService.getProductListById(principalDetail.getId());
        List<MainProductDto> items = productService.getAllProductByUser(principalDetail.getId());

        return ResponseEntity.ok(items);
    }

    @PostMapping(value = "/new") // 상품 등록
    public Long createProduct(@RequestPart ProductDto productDto, @RequestPart List<MultipartFile> productImgFileList,
                              @AuthenticationPrincipal PrincipalDetail principalDetail) throws Exception {

        return productService.createProduct(productDto, productImgFileList, principalDetail);
    }

    @PutMapping("/update/{productId}") //상품 수정
    public Long updateProduct(@PathVariable Long productId, @RequestPart ProductDto productDto, @RequestPart(required = false) List<MultipartFile> productImgFileList) throws Exception {


        return productService.updateProduct(productId, productDto, productImgFileList);
    }

    @DeleteMapping("/{productId}") //상품 삭제
    public ResponseEntity<Object> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/pdStatus") //Status만 수정 (N: 상품 없음 C: 거래 완료 R: 예약중 Y: 상품 있음)
    public ResponseEntity<ProductListDto> updateStatus(@RequestBody Product productDetails, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        ProductListDto productListDto = productService.updateStatus(productDetails, principalDetail);

        return ResponseEntity.ok(productListDto);

    }

    @GetMapping("/{regionName}") //regionNamge을 포함하는 ProductList 조회
    public ResponseEntity<Slice<ResponseProduct>> getProductList(@PathVariable(value = "regionName") String regionName, @RequestParam(defaultValue = "0") int page) throws IOException {
        //Sort 정의 regTime, desc
        Sort sort = Sort.by(Sort.Direction.DESC, "regTime");
        // Pageable 정의 PageRequest.of(index, size, sort)
        Pageable pageable = PageRequest.of(page, 2, sort);
        Slice<ResponseProduct> responseProductList = productService.getProductListByRegionName(regionName, pageable);

        // ResponseProductList의 Image를 byte[]로 추가
        for(ResponseProduct responseProduct: responseProductList) {
            for(ResponseProductImg responseProductImg: responseProduct.getImages()) {
                ClassPathResource classPathResource = new ClassPathResource(responseProductImg.getUrl()
                        .replace("back/src/main/resources/", ""));
                // url을 통해 image파일 byte[]로 저장
                byte[] imageBytes = classPathResource.getInputStream().readAllBytes();
                responseProductImg.setData(imageBytes);
            }
        }
        return ResponseEntity.ok(responseProductList);
    }

    @GetMapping("/title")
    public ResponseEntity<Slice<ResponseProduct>> getProductList(@RequestParam(defaultValue = "0") int page
            ,@RequestParam("title") String title) throws IOException {
        Pageable pageable = PageRequest.of(page, 2);
        Slice<ResponseProduct> responseProducts = productService.getProductListByProductTitle(title, pageable);
        // ResponseProductList의 Image를 byte[]로 추가
        for(ResponseProduct responseProduct: responseProducts) {
            for(ResponseProductImg responseProductImg: responseProduct.getImages()) {
                ClassPathResource classPathResource = new ClassPathResource(responseProductImg.getUrl()
                        .replace("back/src/main/resources/", ""));
                // url을 통해 image파일 byte[]로 저장
                byte[] imageBytes = classPathResource.getInputStream().readAllBytes();
                responseProductImg.setData(imageBytes);
            }
        }
        return ResponseEntity.ok(responseProducts);
    }
}
