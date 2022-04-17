package com.sistema.app.repository;

import java.util.Optional;

import com.sistema.app.entity.Rol;
import com.sistema.app.enums.RolNombre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol,Integer>{
    Optional<Rol> findByRolNombre(RolNombre rolNombre);   
}
