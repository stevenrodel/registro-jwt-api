package com.sistema.app.service;

import javax.transaction.Transactional;

import com.sistema.app.entity.Suscriptor;
import com.sistema.app.repository.SuscriptorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SuscriptorService {

    @Autowired
    SuscriptorRepository suscriptorRepository;

    public Suscriptor save(Suscriptor suscriptor){
        return suscriptorRepository.save(suscriptor);
    }

    public Page<Suscriptor> listAll(Pageable page){
        return suscriptorRepository.findAll(page);
    }
    
}
