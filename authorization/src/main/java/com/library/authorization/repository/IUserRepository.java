package com.library.authorization.repository;

import com.library.authorization.model.Role;
import com.library.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Optional<List<User>> findByRoleIdNot(Role role);
}
