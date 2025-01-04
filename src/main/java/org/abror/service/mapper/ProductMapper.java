package org.abror.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.abror.domain.Product;
import org.abror.service.dto.ProductDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public abstract class ProductMapper implements EntityMapper<ProductDTO, Product> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Mapping(source = "additionalInformation", target = "additionalInformation", qualifiedByName = "mapToString")
    public abstract Product toEntity(ProductDTO dto);

    @Override
    @Mapping(source = "additionalInformation", target = "additionalInformation", qualifiedByName = "stringToMap")
    public abstract ProductDTO toDto(Product entity);

    @Override
    @Mapping(source = "additionalInformation", target = "additionalInformation", qualifiedByName = "mapToString")
    public abstract void partialUpdate(@MappingTarget Product entity, ProductDTO dto);

    @Named("mapToString")
    public String mapToString(Map<String, Object> addInfo) throws JsonProcessingException {
        if (addInfo == null) {
            return null;
        }
        return objectMapper.writeValueAsString(addInfo);
    }

    @Named("stringToMap")
    public Map<String, Object> stringToMap(String addInfo) throws JsonProcessingException {
        if (addInfo == null) {
            return null;
        }
        return objectMapper.readValue(addInfo, new TypeReference<>() {
        });
    }
}
