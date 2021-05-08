package com.seneca.senecaforum.service.utils;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {

    private static MapperUtils mapperUtils;

    private final ModelMapper modelMapper;

    public MapperUtils(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static MapperUtils getInstance(){
        if (mapperUtils == null){
            synchronized (ModelMapper.class){//lazy loading
                if (mapperUtils == null){
                    ModelMapper modelMapper = new ModelMapper();
                    mapperUtils = new MapperUtils(modelMapper);
                }
            }
        }
        return mapperUtils;
    }
    public <S,T> List<T> mapperList(List<S> source, Class<T> targetClass){
        return source
                .stream()
                .map(element -> modelMapper.map(element,targetClass))
                .collect(Collectors.toList());
    }
}
