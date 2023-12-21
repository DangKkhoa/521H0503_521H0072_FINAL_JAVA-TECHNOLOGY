package com.dkkhoa.possystem.model.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUserId(Integer user_id);
    User findByUsername(String username);

    long countByIsAdminFalse();
}
