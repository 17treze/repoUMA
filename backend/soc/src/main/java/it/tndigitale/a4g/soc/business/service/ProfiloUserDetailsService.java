package it.tndigitale.a4g.soc.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ProfiloUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteService utenteService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();

        //TODO: definire i ruoli (utilizzare utenteService)

        UserDetails user = new UserDetails() {

            private static final long serialVersionUID = 6477216455468967827L;

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public String getPassword() {
                return "***";
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authorities;
            }
        };
        return user;
    }

}
