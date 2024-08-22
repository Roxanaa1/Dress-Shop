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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController
{
    private final ProductService productService;

    private final ProductMapper productMapper;
    @Autowired
    public ProductController(ProductService productService,ProductMapper productMapper)
    {
        this.productService=productService;
        this.productMapper=productMapper;
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String query)
    {
        List<Product> products = productService.searchProducts(query);
        if (products.isEmpty())
        {
            return ResponseEntity.noContent().build();
        }
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }
    @PostMapping("/addProduct")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO)
    {
        Product product = productMapper.productDTOToProduct(productDTO);
        Product savedProduct = productService.addProduct(product);
        ProductDTO savedProductDTO = productMapper.productToProductDTO(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDTO);
    }

//    @GetMapping("/getProductById/{id}")
//    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id)
//    {
//        Optional<Product> product = productService.getProductById(id);
//        return product.map(p -> ResponseEntity.ok(productMapper.productToProductDTO(p)))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
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

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<ProductDTO>> getAllProducts()
    {
        List<Product> products = productService.findAll();
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::productToProductDTO) // folosim mapper ul pt a transforma entitatiile in DTO uri
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    @GetMapping("/getProductById/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id)
    {
        return productService.getProductById(id)
                .map(productMapper::productToProductDTO) // folosim mapper ul pt a transforma entitatea in DTO
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/getProductsByCategory")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@RequestParam String category)
    {
        List<Product> products = productService.getProductsByCategory(category);
        List<ProductDTO> productDTOs = products.stream()
                .map(productMapper::productToProductDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

}
