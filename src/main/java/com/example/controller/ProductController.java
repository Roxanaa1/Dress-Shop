package com.example.controller;

import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.model.dtos.ProductDTO;
import com.example.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private final ProductMapper productMapper;
    @Autowired
    public ProductController(ProductService productService,ProductMapper productMapper)
    {
        this.productService=productService;
        this.productMapper=productMapper;
    }


    @PostMapping("/addProduct")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO)
    {
        Product product = productMapper.productDTOToProduct(productDTO);
        Product savedProduct = productService.addProduct(product);
        ProductDTO savedProductDTO = productMapper.productToProductDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id)
    {
        Optional<Product> product = productService.getProductById(id);
        return product.map(p -> ResponseEntity.ok(productMapper.productToProductDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id)
    {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO)
    {
        try {
            // converting DTO to entity
            Product productDetails = productMapper.productDTOToProduct(productDTO);
            Product updatedProduct = productService.updateProduct(id, productDetails);
            // converting the updated entity back to DTO
            ProductDTO updatedProductDTO = productMapper.productToProductDTO(updatedProduct);
            return ResponseEntity.ok(updatedProductDTO);
        } catch (EntityNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        } catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false, defaultValue = "all") String filter)
    {
        List<Product> products = productService.getFilteredProducts(filter);
        return ResponseEntity.ok(products);
    }

}
