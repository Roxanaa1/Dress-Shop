package com.example.service;

import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.model.dtos.ProductDTO;
import com.example.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService
{

    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository)
    {
        this.productRepository=productRepository;

    }

    public Product addProduct(Product product)
    {
        return productRepository.save(product);
    }
    public Optional<Product> getProductById(int id)
    {
        return productRepository.findById(id);
    }
    public Product updateProduct(int id,Product productDetails)
    {
        return productRepository.findById(id).map(product ->
        {
            if (productDetails.getName() != null) {
                product.setName(productDetails.getName());
            }
            if (productDetails.getDescription() != null) {
                product.setDescription(productDetails.getDescription());
            }
            if (productDetails.getPrice() != 0) {
                product.setPrice(productDetails.getPrice());
            }
            if (productDetails.getAvailableQuantity() != 0) {
                product.setAvailableQuantity(productDetails.getAvailableQuantity());
            }
            if (productDetails.getAddedDate() != null) {
                product.setAddedDate(productDetails.getAddedDate());
            }
            if (productDetails.getCategory() != null) {
                product.setCategory(productDetails.getCategory());
            }

            return  productRepository.save(product);

        }).orElseThrow(() -> new EntityNotFoundException("Product not found with id:" + id));
    }


    public void deleteProduct(int id)
    {
        if(productRepository.existsById(id))
        {
            productRepository.deleteById(id);
        }
        else
        {
            throw new RuntimeException("Product not found with id :"+id);
        }
    }

    public List<Product> getFilteredProducts(String filter) {
        List<Product> products;
        if ("all".equals(filter)) {
            products = productRepository.findAll();
        } else if ("evening".equals(filter)) {
            products = productRepository.findByDescriptionContainingIgnoreCase("seara");
        } else if ("day".equals(filter)) {
            products = productRepository.findByDescriptionContainingIgnoreCase("zi");
        } else if ("office".equals(filter)) {
            products = productRepository.findByDescriptionContainingIgnoreCase("office");
        } else {
            products = new ArrayList<>();
        }
        return products;
    }
}
