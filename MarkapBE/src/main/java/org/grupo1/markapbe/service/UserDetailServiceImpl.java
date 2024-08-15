package org.grupo1.markapbe.service;

import org.grupo1.markapbe.controller.dto.AuthLoginRequest;
import org.grupo1.markapbe.controller.dto.AuthResponse;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() ->  new UsernameNotFoundException("El usuario" + username + "no existe"));
        System.out.println(userEntity.getUsername() + userEntity.getRoles() + userEntity.isEnabled());
        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        userEntity.getRoles().forEach(role -> grantedAuthorityList.add( new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userEntity.getRoles().stream().flatMap(role -> role.getPermissionSet().stream()).forEach(permissionEntity -> grantedAuthorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new User(userEntity.getUsername(),userEntity.getPassword(),userEntity.isEnabled(), userEntity.isAccountNoExpired(),userEntity.isCredentialNoExpired(),userEntity.isAccountNoLocked(),grantedAuthorityList);
    }

    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();


        Authentication authentication = this.authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accesstoken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(username,password, accesstoken,true);
        return authResponse;
    }


    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password,userDetails.getPassword())) {
            throw  new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(username,userDetails.getPassword(),userDetails.getAuthorities());
    }


}
