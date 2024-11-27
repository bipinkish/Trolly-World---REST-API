package com.trollyworld.bootapp.repository;

import com.trollyworld.bootapp.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    Boolean existsByUserName(@NotBlank @Size(min = 3, max = 20) String username);

    Boolean existsByEmail(@NotBlank @Size(max = 50) @Email String email);
}
