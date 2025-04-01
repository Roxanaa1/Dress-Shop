package com.example.controller;

import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.dtos.ProductDTO;
import com.example.service.ProductChartsService;
import com.example.service.ProductService;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;
    private final ProductChartsService productChartsService;

    private final ProductMapper productMapper;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService,
                             ProductMapper productMapper,
                             UserService userService,
                             ProductChartsService productChartsService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.userService = userService;
        this.productChartsService = productChartsService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO, @RequestParam int userId) {
        User user = userService.getUserById(userId);
        if (user.getRole().getId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        Product product = productMapper.productDTOToProductManual(productDTO);
        Product savedProduct = productService.addProduct(product);

        ProductDTO savedProductDTO = productMapper.productToProductDTO(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query) {
        List<Product> products = productService.searchProducts(query);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        return productService.getProductById(id)
                .map(productMapper::productToProductDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getProductsByCategory")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/by-month")
    public ResponseEntity<List<Map<String, Object>>> getProductsByMonth(@RequestParam int year) {
        return ResponseEntity.ok(productChartsService.getProductsAddedByMonth(year));
    }

    @GetMapping("/by-category")
    public ResponseEntity<Map<String, Long>> getProductCountByCategory() {
        return ResponseEntity.ok(productChartsService.getProductCountByCategory());
    }

    @GetMapping("/top-sold")
    public ResponseEntity<List<Map<String, Object>>> getTopSoldProducts() {
        return ResponseEntity.ok(productChartsService.getTop5SoldProducts());
    }

    @GetMapping("/sales-evolution/{productId}")
    public ResponseEntity<List<Map<String, Object>>> getSalesEvolution(@PathVariable int productId,
                                                                       @RequestParam int year) {
        return ResponseEntity.ok(productChartsService.getMonthlySalesForProduct(productId, year));
    }

    @GetMapping("/most-sold")
    public ResponseEntity<Map<String, Object>> getMostSoldProduct() {
        return ResponseEntity.ok(productChartsService.getMostSoldProduct());
    }

    @GetMapping("/most-profitable")
    public ResponseEntity<Map<String, Object>> getMostProfitableProduct() {
        return ResponseEntity.ok(productChartsService.getMostProfitableProduct());
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        try {
            Product productDetails = productMapper.productDTOToProduct(productDTO);

            if (productDTO.getProductImages() != null && !productDTO.getProductImages().isEmpty()) {
                productDetails.setProductImages(productMapper.mapCodesToImages(productDTO.getProductImages(), productDetails));
            }

            Product updatedProduct = productService.updateProduct(id, productDetails);
            ProductDTO updatedProductDTO = productMapper.productToProductDTO(updatedProduct);
            return ResponseEntity.ok(updatedProductDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
