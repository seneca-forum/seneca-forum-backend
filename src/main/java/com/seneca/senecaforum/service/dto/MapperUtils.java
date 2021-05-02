package com.seneca.senecaforum.service.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class MapperUtils {
    public static EntityDto convertToDto(Object obj, EntityDto mapper) {
        return new ModelMapper().map(obj, mapper.getClass());
    }

    public Object convertToEntity(Object obj, EntityDto mapper) {
        return new ModelMapper().map(mapper, obj.getClass());
    }
}
