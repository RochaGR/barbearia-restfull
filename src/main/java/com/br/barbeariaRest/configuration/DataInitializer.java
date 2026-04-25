package com.br.barbeariaRest.configuration;


import com.br.barbeariaRest.model.Role;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.RoleRepository;
import com.br.barbeariaRest.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!usuarioRepository.findByUsername("admin@gmail.com").isPresent()) {

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
            admin.setUsername("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminRole));

            usuarioRepository.save(admin);


        } else {
            System.out.println("Usuário admin já existe no banco");
        }
    }
}