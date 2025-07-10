package org.example.posting.hibernate.mapper;

import org.example.posting.hibernate.dto.UserUpdateDto;
import org.example.posting.hibernate.entity.Users;
import org.springframework.stereotype.Component;

// Гпт предлагал вариант с рефлексией, чтобы один класс на все классы для обновления (но есть ограничения)
@Component
public class UserPatchMapper implements PatchMapper<UserUpdateDto, Users>{
    @Override
    public void patch(UserUpdateDto dto, Users user) {
        if (dto.getName() != null){
            user.setName(dto.getName());
        }
        if (dto.getSurname() != null){
            user.setSurname(dto.getSurname());
        }
        if (dto.getLastName() != null){
            user.setLastName(dto.getLastName());
        }
        if (dto.getSex() != null){
            user.setSex(dto.getSex());
        }
        if (dto.getPhoto() != null){
            user.setPhoto(dto.getPhoto());
        }
    }
}
