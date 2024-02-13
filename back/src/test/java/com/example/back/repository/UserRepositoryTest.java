package com.example.back.repository;

import com.example.back.config.TestConfig;
import com.example.back.constant.Role;
import com.example.back.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@Import({TestConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail() {
        User user = User.builder()
                .email("yoon@test.com")
                        .nickname("yoon")
                                .password("123qwe")
                                        .status("Y")
                                                .role(Role.USER)
                                                        .provider("user")
                                                                .build();
        // Save the user to the repository
        userRepository.save(user);

        // Act
        User foundUser = userRepository.findByEmail("yoon@test.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals("yoon@test.com", foundUser.getEmail());
    }

    @Test
    void deleteByEmail() {
        User user = User.builder()
                .email("XXXXXXXXXXXXX")
                .nickname("yoon")
                .password("123qwe")
                .status("Y")
                .role(Role.USER)
                .provider("user")
                .build();
        // Save the user to the repository
        userRepository.save(user);

        // Act
        userRepository.deleteByEmail("XXXXXXXXXXXXX");

        // Assert
        assertNull(userRepository.findByEmail("XXXXXXXXXXXXX"));
    }
}