package org.example.posting.hibernate.mapper;

import org.example.posting.hibernate.dto.UserEditDto;
import org.example.posting.hibernate.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserEditSetterMapper{
    public Users mapFrom(Users user,UserEditDto obj) {
        if (obj.getName() != null){
            user.setName(obj.getName());
        }

        if (obj.getSurname() != null){
            user.setSurname(obj.getSurname());
        }

        if (obj.getLastName() != null){
            user.setLastName(obj.getLastName());
        }

        if (obj.getBirthDate() != null){
            user.setBirthDate(obj.getBirthDate());
        }

        if (obj.getSex() != null){
            user.setSex(obj.getSex());
        }

        if (obj.getPhoto() != null){
            user.setPhoto(obj.getPhoto());
        }

        return user;
    }
}
