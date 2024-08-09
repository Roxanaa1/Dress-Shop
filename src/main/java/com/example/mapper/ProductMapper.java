package com.example.mapper;
import com.example.model.*;
import com.example.model.dtos.AttributeValueDTO;
import com.example.model.dtos.ProductAttributeDTO;
import com.example.model.dtos.ProductDTO;
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
    @Mapping(source = "productAttributeAttributeValues", target = "productAttributeAttributeValues")
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

    default Category map(int categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    default int map(Category category) {
        return category != null ? category.getId() : 0;
    }

    @Named("imagesToUrls")
    default List<String> imagesToUrls(List<ProductImage> images) {
        return images.stream()
                .map(image -> "https://i.postimg.cc/" + image.getCode() + ".png")
                .collect(Collectors.toList());
    }

}
