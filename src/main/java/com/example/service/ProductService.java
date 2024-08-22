package com.example.service;

import com.example.mapper.ProductMapper;
import com.example.model.Product;
import com.example.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class ProductService
{
    @PersistenceContext
    private EntityManager entityManager;//pt search
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    @Autowired
    public ProductService(ProductRepository productRepository,ProductMapper productMapper)
    {
        this.productRepository=productRepository;
        this.productMapper=productMapper;

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

    public List<Product> findAll()
    {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String category)
    {
        List<Product> products = productRepository.findByCategoryNameIgnoreCase(category);
        return products;
    }

    public List<Product> searchProducts(String query)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        String[] keywords = query.split("\\s+");
        List<Predicate> predicates = new ArrayList<>();

        for (String keyword : keywords)
        {
            predicates.add(cb.like(cb.upper(product.get("name")), "%" + keyword.toUpperCase() + "%"));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq).getResultList();
    }

}
