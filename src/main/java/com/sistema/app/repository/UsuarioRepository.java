package com.sistema.app.repository;

import java.util.Optional;

import com.sistema.app.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer>{
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);  
    Boolean existsByNombreUsuario(String nombreUsuario); 
    Boolean existsByEmail(String email); 
}
