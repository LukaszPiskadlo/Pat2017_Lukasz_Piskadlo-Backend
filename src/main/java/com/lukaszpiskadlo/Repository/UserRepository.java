package com.lukaszpiskadlo.Repository;

import com.lukaszpiskadlo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
