package com.sistema.app.config;

import com.sistema.app.config.jwt.JwtEntryPoint;
import com.sistema.app.config.jwt.JwtTokenFilter;
import com.sistema.app.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Autowired
    JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth)throws Exception{
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    public AuthenticationManager authenticationManager()throws Exception{
        return super.authenticationManager();
    }

    @Override
    public void configure(HttpSecurity http)throws Exception{
        http.cors().and().csrf().disable()
            .authorizeRequests()
            .antMatchers("/auth/**").permitAll()
            // -- Swagger UI v2
            .antMatchers("/v2/api-docs").permitAll()  
            .antMatchers("/swagger-resources").permitAll()    
            .antMatchers("/swagger-resources/**").permitAll()    
            .antMatchers("/configuration/ui").permitAll()    
            .antMatchers("/configuration/security").permitAll()    
            .antMatchers("/swagger-ui.html").permitAll()    
            .antMatchers("/webjars/**").permitAll()   
             // -- Swagger UI v3 (OpenAPI)       
            .antMatchers( "/v3/api-docs/**").permitAll()   
            .antMatchers("/swagger-resources/**").permitAll()         
            .antMatchers("/swagger-ui/**").permitAll()     
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(jwtEntryPoint)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}
