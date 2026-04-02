package com.br.barbeariaRest.configuration;


import com.br.barbeariaRest.model.Role;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.RoleRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!usuarioRepository.findByUsername("admin").isPresent()) {

            Role adminRole = roleRepository.findByNome("ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome("ADMIN");
                        return roleRepository.save(role);
                    });

            Role barbeiroRole = roleRepository.findByNome("BARBEIRO")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome("BARBEIRO");
                        return roleRepository.save(role);
                    });

            Role clienteRole = roleRepository.findByNome("CLIENTE")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setNome("CLIENTE");
                        return roleRepository.save(role);
                    });

            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));

            usuarioRepository.save(admin);


        } else {
            System.out.println("Usuário admin já existe no banco");
        }
    }
}