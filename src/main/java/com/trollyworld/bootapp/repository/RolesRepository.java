package com.trollyworld.bootapp.repository;

import com.trollyworld.bootapp.model.AppRole;
import com.trollyworld.bootapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(AppRole appRole);
}
