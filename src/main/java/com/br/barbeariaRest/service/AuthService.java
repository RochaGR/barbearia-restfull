package com.br.barbeariaRest.service;

import com.br.barbeariaRest.dto.mapper.UsuarioMapper;
import com.br.barbeariaRest.dto.request.UsuarioRequestDTO;
import com.br.barbeariaRest.dto.response.LoginResponseDTO;
import com.br.barbeariaRest.dto.response.UsuarioResponseDTO;
import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.repository.UsuarioRepository;
import com.br.barbeariaRest.security.CustomUserDetails;
import com.br.barbeariaRest.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UsuarioResponseDTO registrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    public LoginResponseDTO login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        CustomUserDetails userDetails = new CustomUserDetails(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        // 2. Gerar o token
        String token = jwtUtil.generateToken(authentication);

        // 3. Usar o mapper e depois setar o token
        LoginResponseDTO response = usuarioMapper.toLoginResponseDTO(usuario);
        response.setToken(token);

        return response;
    }
}