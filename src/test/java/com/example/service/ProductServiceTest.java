package com.example.service;

import com.example.mapper.ProductMapper;
import com.example.model.*;
import com.example.repository.*;
import com.example.service.ProductService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductImageRepository productImageRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductAttributeRepository productAttributeRepository;
    @Mock
    private AttributeValueRepository attributeValueRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void add_product_should_save_with_existing_or_new_category() {
        Product product = new Product();
        Category category = new Category();
        category.setName("Dresses");
        product.setCategory(category);

        when(categoryRepository.findByName("Dresses")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product saved = productService.addProduct(product);

        assertEquals(category, saved.getCategory());
        verify(productRepository).save(product);
    }

    @Test
    public void get_product_by_id_should_return_optional() {
        Product product = new Product();
        product.setId(1);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    public void find_all_should_return_all_products() {
        List<Product> products = Arrays.asList(new Product(), new Product());

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void delete_product_should_remove_if_exists() {
        when(productRepository.existsById(1)).thenReturn(true);

        productService.deleteProduct(1);

        verify(productRepository).deleteById(1);
    }
}

