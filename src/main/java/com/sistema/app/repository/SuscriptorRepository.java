package com.sistema.app.repository;

import com.sistema.app.entity.Suscriptor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuscriptorRepository extends JpaRepository<Suscriptor,Long>{    
}
