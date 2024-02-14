package com.example.back.repository;

import com.example.back.config.TestConfig;
import com.example.back.constant.Role;
import com.example.back.entity.User;
import com.example.back.entity.UserInfo;
import com.github.javafaker.Faker;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(InstancioExtension.class)
class UserInfoRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    void findByUser_Id() {
        Faker faker = new Faker();
        User user = new User("user@example.com", "password", "John Doe", "Y", "provider", "providerId", Role.USER, null);
        UserInfo userInfo = new UserInfo("Y", faker.phoneNumber().phoneNumber(), faker.name().name(), faker.file().fileName());
        user = userRepository.save(user);
        userInfo.setUser(user);
        userInfo = userInfoRepository.save(userInfo);

        UserInfo find = userInfoRepository.findByUser_Id(user.getId()).get();

        assertNotNull(find);
        assertEquals(userInfo.getId(), find.getId());
    }

}