package org.example.petproject.modules.repo;

import org.example.petproject.modules.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}