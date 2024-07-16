package com.example.mapper;
import com.example.model.Category;
import com.example.model.Product;
import com.example.model.dtos.ProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDTO productToProductDTO(Product product);

    @Mapping(source = "categoryId", target = "category.id")
    Product productDTOToProduct(ProductDTO productDTO);

    //default deoarece e interfata
    // mapeaza din Category in int si invers
    default Category map(int categoryId) //creeaza si ret un obiect Category,seteaza la Id ul categoriei categoryId
    {
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    default int map(Category category)
    {
        return category != null ? category.getId() : 0;
    }
}
