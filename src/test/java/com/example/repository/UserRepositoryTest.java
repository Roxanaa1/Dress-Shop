package com.example.repository;

import com.example.model.Role;
import com.example.model.RoleType;
import com.example.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.Assertions;

import java.time.LocalDateTime;
import java.util.Optional;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
 class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setVerifiedAccount(true);
        testUser.setFirstName("Roxana");
        testUser.setLastName("Enache");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setPhoneNumber("0712345678");

        Role role = roleRepository.findByRoleType(RoleType.USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleType(RoleType.USER);
                    return roleRepository.save(newRole);
                });

        testUser.setRole(role);
    }
    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void givenEmail_findUserByEmail_shouldReturnUser() {
        userRepository.save(testUser);
        Optional<User> result = userRepository.findUserByEmail("test@example.com");
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void givenNothing_findUserByEmail_shouldReturnEmpty() {
        Optional<User> result = userRepository.findUserByEmail("test@example.com");
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    public void givenExistingEmail_existsByEmail_shouldReturnTrue() {
        userRepository.save(testUser);
        Optional<Boolean> exists = userRepository.existsByEmail("test@example.com");
        Assertions.assertThat(exists).isPresent();
        Assertions.assertThat(exists.get()).isTrue();
    }

    @Test
    public void givenNonExistingEmail_existsByEmail_shouldReturnFalse() {
        Optional<Boolean> exists = userRepository.existsByEmail("nouser@example.com");
        Assertions.assertThat(exists).isPresent();
        Assertions.assertThat(exists.get()).isFalse();
    }

    @Test
    public void givenVerifiedUser_findByEmailAndPasswordAndVerifiedAccountTrue_shouldReturnUser() {
        userRepository.save(testUser);
        Optional<User> result = userRepository.findByEmailAndPasswordAndVerifiedAccountTrue(
                "test@example.com", "password");
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getVerifiedAccount()).isTrue();
    }
}
