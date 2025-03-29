package com.example.mapper;
import com.example.model.*;
import com.example.model.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.w3c.dom.Attr;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category", target = "category")
    @Mapping(source = "productImages", target = "productImages", qualifiedByName = "imagesToUrls")
    @Mapping(source = "productAttributeAttributeValues", target = "productAttributeAttributeValues", qualifiedByName = "mapProductProductAttributes")
    @Mapping(source = "buyingPrice", target = "buyingPrice")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "category", target = "category")
    @Mapping(target = "productImages", ignore = true)
    @Mapping(source = "buyingPrice", target = "buyingPrice")
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
    ProductProductAttributeDTO productProductAttributeToProductProductAttributeDTO(ProductProductAttribute productProductAttribute);

    @Mapping(source = "productAttribute.id", target = "productAttribute.id")
    @Mapping(source = "productAttribute.name", target = "productAttribute.name")
    @Mapping(source = "attributeValue.id", target = "attributeValue.id")
    @Mapping(source = "attributeValue.value", target = "attributeValue.value")
    ProductProductAttribute productProductAttributeDTOToProductProductAttribute(ProductProductAttributeDTO dto);

    @Named("mapProductProductAttributes")
    default List<ProductProductAttributeDTO> mapProductProductAttributes(List<ProductProductAttribute> productProductAttributes) {
        if (productProductAttributes == null) {
            return Collections.emptyList(); // Evită eroarea returnând o listă goală
        }
        return productProductAttributes.stream()
                .map(this::productProductAttributeToProductProductAttributeDTO)
                .collect(Collectors.toList());
    }


    @Named("imagesToUrls")
    default List<String> imagesToUrls(List<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        return images.stream()
                .map(image -> "https://i.postimg.cc/" + image.getCode() + ".png") // Construiește manual URL-ul
                .collect(Collectors.toList());
    }




    default ProductAttributeDTO productAttributeToDTO(ProductAttribute productAttribute) {
        ProductAttributeDTO dto = new ProductAttributeDTO();
        dto.setId(productAttribute.getId());
        dto.setName(productAttribute.getName());
        return dto;
    }
    default List<ProductImage> mapCodesToImages(List<String> codes, Product product) {
        if (codes == null) return Collections.emptyList();

        return codes.stream().map(code -> {
            ProductImage img = new ProductImage();
            img.setCode(code);
            img.setProduct(product);
            return img;
        }).collect(Collectors.toList());
    }

}
