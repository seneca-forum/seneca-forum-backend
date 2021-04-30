package com.seneca.senecaforum.utils;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javassist.NotFoundException;

public class DatabaseUtils {
    public static <T> int generateRandomNumWithinObjSizeFromDb(CrudRepository<T, Integer>repo) {
        int size = ((List<T>)repo.findAll()).size();
        int randomNumber = NumberStringUtils.generateRandomNumber(1, size);
        return randomNumber;
    }


    public static <T> T generateRandomObjFromDb(JpaRepository<T,Integer> repo, Integer first){
        // please check first index in the database, using generics prohibit from getting the first index in entity
        int last = repo.findAll().size();
        T object = null;
        Boolean found = false;
        while(!found){
            int randomNumber = NumberStringUtils.generateRandomNumber(first, first+last);
            try {
                object = repo.findById(randomNumber).orElseThrow(()->new NotFoundException("Found nothing with this id"));
                if(object!=null){
                    found = true;
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        return object;
    }

}
