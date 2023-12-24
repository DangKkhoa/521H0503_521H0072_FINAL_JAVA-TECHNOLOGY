package com.dkkhoa.possystem.model.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User getUserByUserId(Integer user_id);
    User findByUsername(String username);

    long countByIsAdminFalse();

//    Iterable<User> findUsersByAdminIsFalse();
    List<User> findByIsAdminFalse();

    @Modifying
    @Query("UPDATE User u SET u.isLocked = :isLocked WHERE u.userId = :userId")
    void updateByIsLocked(@Param("isLocked") boolean isLocked, @Param("userId") int userId);

}
