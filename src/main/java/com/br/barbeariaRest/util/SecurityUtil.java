package com.br.barbeariaRest.util;

import com.br.barbeariaRest.model.Usuario;
import com.br.barbeariaRest.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Usuario getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUsuario();
        }
        return null;
    }

    public static Long getCurrentUserId() {
        Usuario usuario = getCurrentUser();
        return usuario != null ? usuario.getId() : null;
    }

    public static String getCurrentUserEmail() {
        Usuario usuario = getCurrentUser();
        return usuario != null ? usuario.getEmail() : null;
    }

    public static boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }
}
