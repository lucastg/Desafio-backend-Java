package com.challenge.backend.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GenericMapper {
    public <T, U> T toTransform(U object, Class<T> domainClass) {
        T domain = null;
        try {
            domain = domainClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(object, domain);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return domain;
    }

    public <T, U> List<T> toTransformList(List<U> objectList, Class<T> domainClass) {
        List<T> domainList = new ArrayList<>();
        for (U object : objectList) {
            T domain = toTransform(object, domainClass);
            domainList.add(domain);
        }
        return domainList;
    }
}
