package com.ofds.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.ofds.entity.User;
import com.ofds.repository.UserRepository;

class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsername_normalizesRolePrefixedValues() {
        UserRepository repo = mock(UserRepository.class);
        User user = new User();
        user.setUsername("admin");
        user.setPassword("pw");
        user.setRole("ROLE_ADMIN");
        when(repo.findByUsername("admin")).thenReturn(Optional.of(user));

        CustomUserDetailsService service = new CustomUserDetailsService();
        ReflectionTestUtils.setField(service, "repository", repo);

        UserDetails details = service.loadUserByUsername("admin");

        assertTrue(details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ADMIN"::equals));
        assertFalse(details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_ROLE_ADMIN"::equals));
    }
}
