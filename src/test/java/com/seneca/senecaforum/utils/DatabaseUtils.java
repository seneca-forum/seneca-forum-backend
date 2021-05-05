package com.seneca.senecaforum.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.seneca.senecaforum.domain.User;
import com.seneca.senecaforum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;

public class DatabaseUtils { ;

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

    public static User generateRandomObjFromUserDb(JpaRepository<User,String> userRepository){
        Set<String> userIds = new HashSet<>();
        List<User>users = userRepository.findAll();
        for(User u:users){
            userIds.add(u.getUserId());
        }
//        Integer randomNumber = NumberStringUtils.generateRandomNumber(1,userIds.size());
        return users.get(1);
    }



}
