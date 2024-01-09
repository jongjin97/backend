package com.example.back.jpa.service;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.*;
import com.example.back.entity.Product;
import com.example.back.entity.ProductImage;
import com.example.back.entity.Region;
import com.example.back.entity.User;
import com.example.back.entity.*;
import com.example.back.mybatis.mapper.PurchaseHistoryMapper;
import com.example.back.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    private final SelectProductRepository selectProductRepository;
    private final ProductImageRepository productImageRepository;
    private final PurchaseHistoryMapper purchaseHistoryMapper;
    private final ProductImageService productImageService;
    private final SelectProductService selectProductService;
    @Transactional
    public Long createProduct(ProductDto productDto, List<MultipartFile> productImgFileList, PrincipalDetail principalDetail) throws Exception {

        Region region = regionRepository.findByRegionId(principalDetail.getId());

        System.out.println("region = " + region);

        User user = userRepository.findById(principalDetail.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserInfo not found with ID : " + principalDetail.getId()));

        Product product = Product.builder()
                .user(user)
                .region(region)
                .status("Y")
                .pdTitle(productDto.getPdTitle())
                .pdContents(productDto.getPdContents())
                .pdCategory(productDto.getPdCategory())
                .price(productDto.getPrice())
                .hideStatus("Y")
                .build();

        productRepository.save(product);

        System.out.println("product = " + product);
        //pdTitle, pdContents, pdCategory, price

        //이미지 등록
        for(int i=0; i<productImgFileList.size(); i++) {

            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            if(i == 0)
                productImage.setRepImgYn("Y");
            else
                productImage.setRepImgYn("N");
            productImageService.saveProductImage(productImage, productImgFileList.get(i));
        }

        return product.getId();
    }

    //상품 조회
    @Transactional//(readOnly = true)
    public ProductDto getProductList(Long productId, PrincipalDetail principalDetail) {

        List<ProductImage> productImageList = productImageRepository.findByProductIdOrderByIdAsc(productId);

        System.out.println("productImageList = " + productImageList);
        List<ProductImageDto> productImageDtoList = new ArrayList<>();

        for(ProductImage productImage : productImageList) {

            ProductImageDto productImageDto = ProductImageDto.of(productImage);
            productImageDtoList.add(productImageDto);
        }

        Product product = productRepository.findById(productId).orElseThrow(EntityNotFoundException::new);
        ProductDto productDto = ProductDto.of(product);
        productDto.setProductImageDtoList(productImageDtoList);

        if(principalDetail != null){
               selectProductService.saveSelectProduct(product, principalDetail.getUser());
        }
        return productDto;
    }

    @Transactional
    public List<ProductListDto> getProductListById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not exist with ID : " + id));

        List<Product> productList = productRepository.findAllByUser(user).orElse(Collections.emptyList());

        return productList.stream()
                .map(product -> new ProductListDto(product, user))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long updateProduct(Long productId, ProductDto productDto, List<MultipartFile> productImgFileList) throws Exception {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 입니다. :" + productId));

        product.updateProduct(productDto);

        List<Long> productImgIds = productImageRepository.countById(productId);

        //이미지 등록
        if(productImgFileList != null) {
            for (int i = 0; i < productImgFileList.size(); i++) {

                productImageService.updateProductImage(productImgIds.get(i), productImgFileList.get(i));
            }
        }

        return product.getId();
    }

    @Transactional
    public void deleteProduct(Long pdId) {

        productRepository.deleteById(pdId);
    }


    public void saveTestProduct(RequestProduct requestProduct) {
        User user = userRepository.findById(requestProduct.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID : " + requestProduct.getUserId()));

        Region region = regionRepository.findByUserAndAndRegionName(user, requestProduct.getRegion())
                .orElseThrow(() -> new IllegalArgumentException("Region not found with RegionName : " + requestProduct.getRegion()));


        Product product = Product.builder()
                .pdTitle(requestProduct.getPdTitle())
                .pdContents(requestProduct.getPdContents())
                .pdCategory(requestProduct.getPdCategory())
                .price(requestProduct.getPrice())
                .status(requestProduct.getStatus())
                .hideStatus(requestProduct.getHideStatus())
                .user(user)
                .region(region)
                .build();
        for (RequestProductImg productImg : requestProduct.getImages()) {
            ProductImage productImage = new ProductImage(productImg.getUrl(), product);
            product.getProductImages().add(productImage);
        }
        productRepository.save(product);
    }

    @Transactional
    public ProductListDto updateStatus(Product productDetails, PrincipalDetail principalDetail) {
        Product product = productRepository.findById(productDetails.getId())
                .orElseThrow(() -> new IllegalArgumentException("Product not exist with id :" + productDetails.getId()));

        User user = userRepository.findById(principalDetail.getId())
                .orElseThrow(() -> new IllegalArgumentException("UserInfo not found with ID : " + principalDetail.getId()));

        product.setStatus(productDetails.getStatus());

        //createPurchaseHistory 이거 불러오기 추가(status : C일 떄 생성하는 쿼리)

        PurchaseHistoryDto purchaseHistoryDto = PurchaseHistoryDto.builder()
                .productId(product.getId())
                .userId(user.getId())
                .build();

        if(productDetails.getStatus().equals("C")) {
            purchaseHistoryMapper.createPurchaseHistory(purchaseHistoryDto, user.getId());
        }


        Product updateStatus = productRepository.save(product);
        return new ProductListDto(updateStatus, product.getUser());

    }

    @Transactional
    public Slice<ResponseProduct> getProductListByRegionName(String regionName, Pageable pageable){
        // ProductSlice 조회
        Slice<Product> productList = productRepository.findByRegion_RegionNameContainsOrderByRegTimeDesc(regionName, pageable);
        // ResponeProduct로 변환
        Slice<ResponseProduct> responseProductSlice = productList.map(ResponseProduct::new);
        // ProductList 별 조회수 조회
        List<Object[]> objects = productRepository.findProductsAndCount(productList.toList());
        for(int i=0; i<objects.size(); i++){
            ResponseProduct responseProduct = responseProductSlice.toList().get(i);
            responseProduct.setSelectedCount((Long) objects.get(i)[1]);
            responseProduct.setAttentionCount((Long) objects.get(i)[2]);
        }
        return responseProductSlice;
    }

    @Transactional
    public ResponseProduct  getProductById(Long id, PrincipalDetail principalDetail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not exist with id :" + id));
        // 로그인 되어 있을 때 SelectProduct 추가
        if(principalDetail != null){
            User user = userRepository.findById(principalDetail.getId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID : " + principalDetail.getId()));

            SelectProduct selectProduct = selectProductRepository
                    .findByUser_IdAndProduct_Id(principalDetail.getId(), product.getId())
                    .orElse(new SelectProduct("Y",  user, product));
            selectProductRepository.save(selectProduct);
        }

        return new ResponseProduct(product);
    }

    @Transactional
    public Slice<ResponseProduct> getProductListByProductTitle(String productTitle, Pageable pageable){
        Slice<Product> products = productRepository.findProductsByProductName(productTitle, pageable);
        List<Object[]> objects = productRepository.findProductsAndCount(products.toList());
        Slice<ResponseProduct> responseProductSlice = products.map(ResponseProduct::new);
        for(int i=0; i<objects.size(); i++){
            ResponseProduct responseProduct = responseProductSlice.toList().get(i);
            responseProduct.setSelectedCount((Long) objects.get(i)[1]);
            responseProduct.setAttentionCount((Long) objects.get(i)[2]);
        }
        return responseProductSlice;
    }

    @Transactional
    public List<MainProductDto> getAllProduct(ProductSearchDto productSearchDto) {

        List<MainProductDto> products = productRepository.findAllProductAndImgUrl(productSearchDto);

        return products;
    }
    @Transactional
    public List<MainProductDto> getAllProductByUser(Long id) {

        List<MainProductDto> products = productRepository.findAllProductByUser(id);

        return products;
    }

    public List<MainProductDto> getSearchProduct(String searchQuery) {

        List<MainProductDto> products = productRepository.findSearchProductAndImgUrl(searchQuery);

        return products;
    }

    public List<MainProductDto> findCategoryProduct(String pdCategory) {
        List<MainProductDto> products = productRepository.findCategoryProduct(pdCategory);

        return products;
    }
    @Transactional
    public void updateProductStatus(Long productId, String status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not exist with id :" + productId));
        product.setStatus(status);
        if(status.equals("C")){
            PurchaseHistoryDto purchaseHistoryDto = new PurchaseHistoryDto();
            purchaseHistoryDto.setProductId(productId);
            purchaseHistoryMapper.createPurchaseHistoryWithoutUser(purchaseHistoryDto);
        }
        productRepository.save(product);
    }

    @Transactional
    public void renewProduct(Long productId){
        Product product = productRepository.findById(productId).orElseThrow(
                IllegalArgumentException::new);
        Product newProduct = Product.builder().pdTitle(product.getPdTitle())
                .price(product.getPrice())
                .pdContents(product.getPdContents())
                .pdCategory(product.getPdCategory())
                .hideStatus(product.getHideStatus())
                .status("Y")
                .user(product.getUser())
                .region(product.getRegion()).build();
        List<ProductImage> images = product.getProductImages();
        List<ProductImage> newImages = new ArrayList<>();
        for (ProductImage productImage: images){
            ProductImage pi = new ProductImage();
            pi.setImgUrl(productImage.getImgUrl());
            pi.setImgName(productImage.getImgName());
            pi.setOriImgName(productImage.getOriImgName());
            pi.setRepImgYn(productImage.getRepImgYn());
            pi.setImgUrl(productImage.getImgUrl());
            pi.setProduct(newProduct);
            newImages.add(pi);
        }
        newProduct.setProductImages(newImages);
        productRepository.save(newProduct);

    }

}
