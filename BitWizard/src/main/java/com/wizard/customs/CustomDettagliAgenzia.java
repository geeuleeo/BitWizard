package com.wizard.customs;

import com.wizard.entities.Agenzia;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomDettagliAgenzia implements UserDetails {

    private Agenzia agenzia;

    public CustomDettagliAgenzia(Agenzia agenzia) {
        this.agenzia = agenzia;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("privato"));
        return authorities;
    }

    @Override
    public String getPassword() {
        return agenzia.getPassword();
    }

    @Override
    public String getUsername() {
        return agenzia.getPartitaIva();
    }

    public Agenzia getAgenzia() {return this.agenzia;}
}
