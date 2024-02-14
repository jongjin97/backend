package com.example.back.repository;

import com.example.back.config.TestConfig;
import com.example.back.constant.Role;
import com.example.back.dto.SelectProductDto;
import com.example.back.entity.Product;
import com.example.back.entity.ProductImage;
import com.example.back.entity.SelectProduct;
import com.example.back.entity.User;
import com.github.javafaker.Faker;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import javax.inject.Inject;
import java.util.List;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(InstancioExtension.class)
class SelectProductRepositoryTest {
    @Autowired
    private SelectProductRepository selectProductRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JPAQueryFactory queryFactory;
    @Test
    void findByUser_IdAndProduct_Id() {
        User user = new User("user@example.com", "password", "John Doe", "Y", "provider", "providerId", Role.USER, null);

        userRepository.save(user);


        Product product = new Product("N", "Product Title", "Product Contents", "Category", "100", "N", null, user);
        // Set product properties if needed
        productRepository.save(product);


        SelectProduct selectProduct = new SelectProduct("Y", user, product);
        selectProductRepository.save(selectProduct);


        SelectProduct retrievedSelectProduct = selectProductRepository.findByUser_IdAndProduct_Id(user.getId(), product.getId()).get();


        assertNotNull(retrievedSelectProduct);

        assertEquals(user.getId(), retrievedSelectProduct.getUser().getId());
        assertEquals(product.getId(), retrievedSelectProduct.getProduct().getId());
    }

    @Test
    @InstancioSource
    void findAllByUserId() {
        Faker faker = new Faker();
        User user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().name()
                , "Y", "provider", "providerId", Role.USER, null);
        user = userRepository.save(user);

        Product product = new Product("N", faker.book().title(), "content", "Category"
                , faker.number().digits(3), "N", null, user);
        ProductImage productImage = new ProductImage("asd", product);
        productImage.setRepImgYn("Y");
        product.getProductImages().add(productImage);
        product = productRepository.save(product);


        SelectProduct selectProduct = new SelectProduct("Y", user, product);
        selectProductRepository.save(selectProduct);

        List<SelectProductDto> retrievedSelectProducts = selectProductRepository.findAllByUserId(user.getId(), "");

        assertNotNull(retrievedSelectProducts);
        assertFalse(retrievedSelectProducts.isEmpty());

    }
}