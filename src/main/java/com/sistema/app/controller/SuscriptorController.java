package com.sistema.app.controller;

import java.util.Date;

import com.sistema.app.entity.Suscriptor;
import com.sistema.app.service.SuscriptorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/suscriptor")
@CrossOrigin
public class SuscriptorController {

    @Autowired
    SuscriptorService suscriptorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@RequestBody Suscriptor suscriptorNuevo){

        if(suscriptorNuevo.getIdentificacion() == null || suscriptorNuevo.getIdentificacion().equalsIgnoreCase(""))
            return new ResponseEntity<>("La identificación no puede estar vacia",HttpStatus.BAD_REQUEST);

        if(suscriptorNuevo.getNombre() == null || suscriptorNuevo.getNombre().equalsIgnoreCase(""))
            return new ResponseEntity<>("El nombre no puede estar vacio",HttpStatus.BAD_REQUEST);

        if(suscriptorNuevo.getFechaNacimiento() == null || suscriptorNuevo.getFechaNacimiento().equals(new Date(0)))
            return new ResponseEntity<>("La fecha de nacimiento no puede estar vacia",HttpStatus.BAD_REQUEST);

        Suscriptor suscriptor = suscriptorService.save(suscriptorNuevo); 
        if(suscriptor!=null && !suscriptor.getId().equals(0L))   
            return new ResponseEntity<>("suscriptor guardado",HttpStatus.CREATED);
        else
            return new ResponseEntity<>("No ser pudo almacenar suscriptor",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/lista/{page}/{limit}")
    public ResponseEntity<?> lista(@PathVariable Integer page,@PathVariable Integer limit){
        Pageable pageable = PageRequest.of(page, limit);
        return new ResponseEntity<>(suscriptorService.listAll(pageable),HttpStatus.OK);
    }
    
}
