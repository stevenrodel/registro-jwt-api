package com.sistema.app.util;

import java.util.Optional;

import com.sistema.app.entity.Rol;
import com.sistema.app.enums.RolNombre;
import com.sistema.app.service.RolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateRoles implements CommandLineRunner{

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {
        Optional<Rol> admin = rolService.getByRolNombre(RolNombre.ROLE_ADMIN);
        if(admin.isEmpty()){
            Rol rolAdmin = new Rol(RolNombre.ROLE_ADMIN);
            rolService.save(rolAdmin);
        }

        Optional<Rol> user = rolService.getByRolNombre(RolNombre.ROLE_USER);
        if(user.isEmpty()){
            Rol rolUser = new Rol(RolNombre.ROLE_USER);
            rolService.save(rolUser);
        }
    }
    
}
