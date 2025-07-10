package org.example.posting.hibernate.mapper;

public interface PatchMapper<D, E> {
    void patch(D source, E target);
}
