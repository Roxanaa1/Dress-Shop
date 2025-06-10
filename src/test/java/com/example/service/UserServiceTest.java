package com.example.service;
import com.example.mapper.AddressMapper;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.EmailService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductImageRepository productImageRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_user_should_save_and_send_verification_email() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plaintext");
        Role role = new Role();
        role.setRoleType(RoleType.USER);

        when(passwordEncoder.encode("plaintext")).thenReturn("encoded_password");
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.create(user);

        assertEquals("encoded_password", result.getPassword());
        assertNotNull(result.getVerificationCode());
        assertNotNull(result.getVerificationCodeExpiration());

        verify(userRepository).save(any(User.class));
        verify(cartRepository).save(any(Cart.class));
        verify(emailService).sendVerificationEmail(eq("test@example.com"), anyString());
    }

    @Test
    public void verify_user_with_correct_code_should_set_verified_true() {
        String email = "user@example.com";
        String code = "123456";
        User user = new User();
        user.setEmail(email);
        user.setVerificationCode(code);
        user.setVerificationCodeExpiration(LocalDateTime.now().plusMinutes(5));
        user.setVerifiedAccount(false);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.verify(email, code);

        assertTrue(result.getVerifiedAccount());
        assertNull(result.getVerificationCode());
        assertNull(result.getVerificationCodeExpiration());

        verify(userRepository).save(user);
    }
    @Test
    public void login_should_return_user_if_credentials_are_correct() {
        User user = new User();
        user.setEmail("login@example.com");
        user.setPassword("encodedPassword");
        user.setVerifiedAccount(true);

        when(userRepository.findByEmail("login@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "encodedPassword")).thenReturn(true);

        User result = userService.login("login@example.com", "plainPassword");

        assertEquals(user, result);
    }

    @Test
    public void send_reset_code_should_generate_and_send_code() {
        String email = "reset@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        userService.sendResetCode(email);

        verify(emailService).sendResetPasswordCode(eq(email), anyString());
    }

    @Test
    public void change_password_should_update_password_if_old_matches() {
        int userId = 1;
        User user = new User();
        user.setPassword("oldEncoded");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldRaw", "oldEncoded")).thenReturn(true);
        when(passwordEncoder.encode("newRaw")).thenReturn("newEncoded");

        userService.changePassword(userId, "oldRaw", "newRaw");

        verify(userRepository).save(user);
        assertEquals("newEncoded", user.getPassword());
    }

}

