package com.example.controller;

import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.model.User;
import com.example.model.dtos.ProductDTO;
import com.example.service.ProductService;
import com.example.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {
    private final ProductService productService;

    private final ProductMapper productMapper;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper, UserService userService) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.userService = userService;
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

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO, @RequestParam int userId) {
        User user = userService.getUserById(userId);
        if (user.getRole().getId() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        Product product = productMapper.productDTOToProduct(productDTO);
        Product savedProduct = productService.addProduct(product);

        if (productDTO.getProductImages() != null && !productDTO.getProductImages().isEmpty()) {
            for (String imageCode : productDTO.getProductImages()) {
              productService.addImageToProduct(savedProduct.getId(), imageCode);
            }
        }

        ProductDTO savedProductDTO = productMapper.productToProductDTO(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
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

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        try {
            Product productDetails = productMapper.productDTOToProduct(productDTO);
            Product updatedProduct = productService.updateProduct(id, productDetails);
            ProductDTO updatedProductDTO = productMapper.productToProductDTO(updatedProduct);
            return ResponseEntity.ok(updatedProductDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
}
