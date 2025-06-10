package com.example.repository;

import com.example.model.Category;
import com.example.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private CartEntryRepository cartEntryRepository;
    @Autowired
    private ProductProductAttributeRepository productAttributeRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    private Category category;
    private Product product;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Evening Dresses");
        category = categoryRepository.save(category);

        product = new Product();
        product.setName("Elegant Black Dress");
        product.setDescription("A perfect evening dress.");
        product.setPrice(299.99f);
        product.setAvailableQuantity(10);
        product.setAddedDate(LocalDate.now());
        product.setCategory(category);
        product.setBuyingPrice(150.0f);
        product = productRepository.save(product);
    }
    @Test
    public void givenMatchingCategory_whenFindByCategoryNameIgnoreCase_thenReturnProducts() {
        List<Product> results = productRepository.findByCategoryNameIgnoreCase("evening dresses");

        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getCategory().getName()).isEqualToIgnoringCase("Evening Dresses"); // ✅ fix
    }

    @Test
    public void givenNonMatchingCategory_whenFindByCategoryNameIgnoreCase_thenReturnEmptyList() {
        wishlistRepository.deleteAll();
        cartEntryRepository.deleteAll();
        productAttributeRepository.deleteAll();
        productImageRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        List<Product> results = productRepository.findByCategoryNameIgnoreCase("nonexistent");
        assertThat(results).isEmpty();
    }

    @Test
    public void givenNamePart_whenFindByNameContainingIgnoreCase_thenReturnProduct() {
        List<Product> results = productRepository.findByNameContainingIgnoreCase("black");
        assertThat(results).isNotEmpty();
        assertThat(results.get(0).getName()).containsIgnoringCase("black");
    }

    @Test
    public void givenNoMatchInName_whenFindByNameContainingIgnoreCase_thenReturnEmpty() {
        List<Product> results = productRepository.findByNameContainingIgnoreCase("jeans");
        assertThat(results).isEmpty();
    }

    @Test
    public void givenProductWithAddedDate_whenCountProductsByMonth_thenReturnValidCount() {
        List<Map<String, Object>> results = productRepository.countProductsByMonth();
        assertThat(results).isNotEmpty();

        int currentMonth = LocalDate.now().getMonthValue();
        boolean found = results.stream()
                .anyMatch(row -> ((Number) row.get("month")).intValue() == currentMonth);
        assertThat(found).isTrue();
    }
}
