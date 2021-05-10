package com.seneca.senecaforum.service.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperUtils {

    public static <S,T> List<T> mapperList(List<S> source, Class<T> targetClass){
        ModelMapper modelMapper = new ModelMapper();
        return source
                .stream()
                .map(element -> modelMapper.map(element,targetClass))
                .collect(Collectors.toList());
    }
}
