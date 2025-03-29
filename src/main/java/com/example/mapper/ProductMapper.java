package com.example.mapper;

import com.example.model.*;
import com.example.model.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
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

    default Product productDTOToProductManual(ProductDTO dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setAvailableQuantity(dto.getAvailableQuantity());
        product.setAddedDate(dto.getAddedDate());
        product.setBuyingPrice(dto.getBuyingPrice());

        if (dto.getCategory() != null) {
            Category category = new Category();
            category.setName(dto.getCategory().getName());
            category.setDescription(dto.getCategory().getDescription());
            product.setCategory(category);
        }

        if (dto.getProductImages() != null) {
            List<ProductImage> images = dto.getProductImages().stream().map(code -> {
                ProductImage img = new ProductImage();
                img.setCode(code);
                img.setProduct(product);
                return img;
            }).toList();
            product.setProductImages(images);
        }

        if (dto.getAttributes() != null) {
            List<ProductProductAttribute> links = new ArrayList<>();
            for (AttributeWithValuesDTO attr : dto.getAttributes()) {
                for (String val : attr.getValues()) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setName(attr.getAttributeName());

                    AttributeValue av = new AttributeValue();
                    av.setValue(val);

                    ProductProductAttribute ppa = new ProductProductAttribute();
                    ppa.setProduct(product);
                    ppa.setProductAttribute(pa);
                    ppa.setAttributeValue(av);

                    links.add(ppa);
                }
            }
            product.setProductAttributeAttributeValues(links);
        }
        return product;
    }
}
