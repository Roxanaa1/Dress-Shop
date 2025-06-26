package com.example.mapper;

import com.example.model.AttributeValue;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductAttribute;
import com.example.model.ProductProductAttribute;
import com.example.model.dtos.AttributeValueDTO;
import com.example.model.dtos.CategoryDTO;
import com.example.model.dtos.ProductAttributeDTO;
import com.example.model.dtos.ProductDTO;
import com.example.model.dtos.ProductProductAttributeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-11T00:48:17+0300",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductDTO productToProductDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setCategory( categoryToCategoryDTO( product.getCategory() ) );
        productDTO.setProductImages( imagesToUrls( product.getProductImages() ) );
        productDTO.setProductAttributeAttributeValues( mapProductProductAttributes( product.getProductAttributeAttributeValues() ) );
        productDTO.setBuyingPrice( product.getBuyingPrice() );
        productDTO.setId( product.getId() );
        productDTO.setName( product.getName() );
        productDTO.setDescription( product.getDescription() );
        productDTO.setPrice( product.getPrice() );
        productDTO.setAvailableQuantity( product.getAvailableQuantity() );
        productDTO.setAddedDate( product.getAddedDate() );

        return productDTO;
    }

    @Override
    public Product productDTOToProduct(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        product.setCategory( categoryDTOToCategory( productDTO.getCategory() ) );
        product.setBuyingPrice( productDTO.getBuyingPrice() );
        product.setId( productDTO.getId() );
        product.setName( productDTO.getName() );
        product.setDescription( productDTO.getDescription() );
        product.setPrice( productDTO.getPrice() );
        product.setAvailableQuantity( productDTO.getAvailableQuantity() );
        product.setAddedDate( productDTO.getAddedDate() );
        product.setProductAttributeAttributeValues( productProductAttributeDTOListToProductProductAttributeList( productDTO.getProductAttributeAttributeValues() ) );

        return product;
    }

    @Override
    public AttributeValueDTO attributeValueToAttributeValueDTO(AttributeValue attributeValue) {
        if ( attributeValue == null ) {
            return null;
        }

        AttributeValueDTO attributeValueDTO = new AttributeValueDTO();

        attributeValueDTO.setId( attributeValue.getId() );
        attributeValueDTO.setValue( attributeValue.getValue() );

        return attributeValueDTO;
    }

    @Override
    public AttributeValue attributeValueDTOToAttributeValue(AttributeValueDTO attributeValueDTO) {
        if ( attributeValueDTO == null ) {
            return null;
        }

        AttributeValue attributeValue = new AttributeValue();

        attributeValue.setId( attributeValueDTO.getId() );
        attributeValue.setValue( attributeValueDTO.getValue() );

        return attributeValue;
    }

    @Override
    public ProductProductAttributeDTO productProductAttributeToProductProductAttributeDTO(ProductProductAttribute productProductAttribute) {
        if ( productProductAttribute == null ) {
            return null;
        }

        ProductProductAttributeDTO productProductAttributeDTO = new ProductProductAttributeDTO();

        productProductAttributeDTO.setProductAttribute( productAttributeToProductAttributeDTO( productProductAttribute.getProductAttribute() ) );
        productProductAttributeDTO.setAttributeValue( attributeValueToAttributeValueDTO1( productProductAttribute.getAttributeValue() ) );
        productProductAttributeDTO.setId( productProductAttribute.getId() );

        return productProductAttributeDTO;
    }

    @Override
    public ProductProductAttribute productProductAttributeDTOToProductProductAttribute(ProductProductAttributeDTO dto) {
        if ( dto == null ) {
            return null;
        }

        ProductProductAttribute productProductAttribute = new ProductProductAttribute();

        productProductAttribute.setProductAttribute( productAttributeDTOToProductAttribute( dto.getProductAttribute() ) );
        productProductAttribute.setAttributeValue( attributeValueDTOToAttributeValue1( dto.getAttributeValue() ) );
        productProductAttribute.setId( dto.getId() );

        return productProductAttribute;
    }

    protected CategoryDTO categoryToCategoryDTO(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setId( category.getId() );
        categoryDTO.setName( category.getName() );
        categoryDTO.setDescription( category.getDescription() );

        return categoryDTO;
    }

    protected Category categoryDTOToCategory(CategoryDTO categoryDTO) {
        if ( categoryDTO == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( categoryDTO.getId() );
        category.setName( categoryDTO.getName() );
        category.setDescription( categoryDTO.getDescription() );

        return category;
    }

    protected List<ProductProductAttribute> productProductAttributeDTOListToProductProductAttributeList(List<ProductProductAttributeDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<ProductProductAttribute> list1 = new ArrayList<ProductProductAttribute>( list.size() );
        for ( ProductProductAttributeDTO productProductAttributeDTO : list ) {
            list1.add( productProductAttributeDTOToProductProductAttribute( productProductAttributeDTO ) );
        }

        return list1;
    }

    protected ProductAttributeDTO productAttributeToProductAttributeDTO(ProductAttribute productAttribute) {
        if ( productAttribute == null ) {
            return null;
        }

        ProductAttributeDTO productAttributeDTO = new ProductAttributeDTO();

        productAttributeDTO.setId( productAttribute.getId() );
        productAttributeDTO.setName( productAttribute.getName() );

        return productAttributeDTO;
    }

    protected AttributeValueDTO attributeValueToAttributeValueDTO1(AttributeValue attributeValue) {
        if ( attributeValue == null ) {
            return null;
        }

        AttributeValueDTO attributeValueDTO = new AttributeValueDTO();

        attributeValueDTO.setId( attributeValue.getId() );
        attributeValueDTO.setValue( attributeValue.getValue() );

        return attributeValueDTO;
    }

    protected ProductAttribute productAttributeDTOToProductAttribute(ProductAttributeDTO productAttributeDTO) {
        if ( productAttributeDTO == null ) {
            return null;
        }

        ProductAttribute productAttribute = new ProductAttribute();

        productAttribute.setId( productAttributeDTO.getId() );
        productAttribute.setName( productAttributeDTO.getName() );

        return productAttribute;
    }

    protected AttributeValue attributeValueDTOToAttributeValue1(AttributeValueDTO attributeValueDTO) {
        if ( attributeValueDTO == null ) {
            return null;
        }

        AttributeValue attributeValue = new AttributeValue();

        attributeValue.setId( attributeValueDTO.getId() );
        attributeValue.setValue( attributeValueDTO.getValue() );

        return attributeValue;
    }
}
