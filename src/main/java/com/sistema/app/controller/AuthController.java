package com.sistema.app.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sistema.app.dto.JwtDto;
import com.sistema.app.dto.LoginUsuario;
import com.sistema.app.dto.NuevoUsuario;
import com.sistema.app.entity.Rol;
import com.sistema.app.entity.Usuario;
import com.sistema.app.enums.RolNombre;
import com.sistema.app.config.jwt.JwtProvider;
import com.sistema.app.service.RolService;
import com.sistema.app.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@RequestBody NuevoUsuario nuevoUsuario){


        if(nuevoUsuario.getPassword() == null || nuevoUsuario.getPassword().equalsIgnoreCase(""))
            return new ResponseEntity<>("El password no puede estar vacio",HttpStatus.BAD_REQUEST);

        if(nuevoUsuario.getNombre() == null || nuevoUsuario.getNombre().equalsIgnoreCase(""))
            return new ResponseEntity<>("El nombre no puede estar vacio",HttpStatus.BAD_REQUEST);

        if(nuevoUsuario.getNombreUsuario() == null || nuevoUsuario.getNombreUsuario().equalsIgnoreCase(""))
            return new ResponseEntity<>("El nombre de usuario no puede estar vacio",HttpStatus.BAD_REQUEST);

            // Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(nuevoUsuario.getEmail());

        if(nuevoUsuario.getEmail() == null || nuevoUsuario.getEmail().equalsIgnoreCase("") || !mather.find())
            return new ResponseEntity<>("El email no puede estar vacio o el formato es incorrecto",HttpStatus.BAD_REQUEST);

        if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity<>("Ese nombre ya existe",HttpStatus.BAD_REQUEST);

        if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity<>("Ese email ya existe",HttpStatus.BAD_REQUEST);

        Usuario usuario = new Usuario(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));   

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());

        usuario.setRoles(roles);    
        usuarioService.save(usuario);
        return new ResponseEntity<>("usuario guardado",HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<>(jwtDto,HttpStatus.OK); 
    }   
}
