package com.HotelBookingSystem.BookingBro.security.User;


import com.HotelBookingSystem.BookingBro.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserDetails implementation for BookingBro Security
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingBroUserDetails implements UserDetails {

    private Long id;
    private String email; // use email instead of username
    private String password;
    private Collection<GrantedAuthority> authorities;

    // Build BookingBroUserDetails from User entity
    public static BookingBroUserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new BookingBroUserDetails(
                user.getId(),
                user.getEmail(), // use email here
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email; // Spring Security will now use email for authentication
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
