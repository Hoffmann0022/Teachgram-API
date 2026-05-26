package com.Teach.Teachgram.repository;

import com.Teach.Teachgram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserNameAndDeletedFalse(String userName);

    Optional<User> findByMailAndDeletedFalse(String mail);

    Optional<User> findByPhoneAndDeletedFalse(String phone);

    boolean existsByUserNameAndDeletedFalse(String userName);
    boolean existsByMailAndDeletedFalse(String mail);
    boolean existsByPhoneAndDeletedFalse(String phone);

    List<User> findAllByDeletedFalse();

    Optional<User> findByUserName(String userName);
}
