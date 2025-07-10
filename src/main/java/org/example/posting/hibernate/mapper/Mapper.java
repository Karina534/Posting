package org.example.posting.hibernate.mapper;

public interface Mapper<F, T>{
    T mapFrom(F obj);
}
