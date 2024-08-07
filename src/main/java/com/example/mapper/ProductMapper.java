package com.example.mapper;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.ProductImage;
import com.example.model.dtos.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper
{
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "productImages", target = "productImages", qualifiedByName = "imagesToUrls")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "productImages", ignore = true)  // ignoram maparea pentru productImages cand convertim din DTO
    Product productDTOToProduct(ProductDTO productDTO);

    default Category map(int categoryId)
    {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    default int map(Category category)
    {
        return category != null ? category.getId() : 0;
    }

    @Named("imagesToUrls")
    default List<String> imagesToUrls(List<ProductImage> images)
    {
        return images.stream()
                .map(image -> "https://i.postimg.cc/" + image.getCode() + ".png")
                .collect(Collectors.toList());
    }

}
