package com.example.mapper;
import com.example.model.*;
import com.example.model.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.w3c.dom.Attr;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper
{
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "productImages", target = "productImages", qualifiedByName = "imagesToUrls")
    @Mapping(source = "productAttributeAttributeValues", target = "productAttributeAttributeValues", qualifiedByName = "mapProductProductAttributes")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "productImages", ignore = true)
    Product productDTOToProduct(ProductDTO productDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "value", target = "value")
    AttributeValueDTO attributeValueToAttributeValueDTO(AttributeValue attributeValue);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "value", target = "value")
    AttributeValue attributeValueDTOToAttributeValue(AttributeValueDTO attributeValueDTO);

    @Mapping(source = "productAttribute.id", target = "productAttribute.id")
    @Mapping(source = "productAttribute.name", target = "productAttribute.name")
    @Mapping(source = "attributeValue.id", target = "attributeValue.id")
    @Mapping(source = "attributeValue.value", target = "attributeValue.value")
    ProductAttributeValueDTO productAttributeValueToProductAttributeValueDTO(ProductAttributeValue productAttributeValue);

    @Named("mapProductProductAttributes")
    default List<ProductProductAttributeDTO> mapProductProductAttributes(List<ProductProductAttribute> productProductAttributes) {
        return productProductAttributes.stream()
                .map(this::productProductAttributeToDTO)
                .collect(Collectors.toList());
    }

    default ProductProductAttributeDTO productProductAttributeToDTO(ProductProductAttribute productProductAttribute) {
        ProductProductAttributeDTO dto = new ProductProductAttributeDTO();
        dto.setId(productProductAttribute.getId());
        dto.setProductAttribute(productAttributeToDTO(productProductAttribute.getProductAttribute()));
        return dto;
    }

    default ProductAttributeDTO productAttributeToDTO(ProductAttribute productAttribute) {
        ProductAttributeDTO dto = new ProductAttributeDTO();
        dto.setId(productAttribute.getId());
        dto.setName(productAttribute.getName());

        if (productAttribute.getProductAttributeValues() != null && !productAttribute.getProductAttributeValues().isEmpty()) {
            ProductAttributeValue firstValue = productAttribute.getProductAttributeValues().get(0);
            dto.setValueId(firstValue.getAttributeValue().getId());
            dto.setValue(firstValue.getAttributeValue().getValue());
        }

        return dto;
    }

    @Named("imagesToUrls")
    default List<String> imagesToUrls(List<ProductImage> images) {
        return images.stream()
                .map(image -> "https://i.postimg.cc/" + image.getCode() + ".png")
                .collect(Collectors.toList());
    }
}