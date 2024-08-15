package org.grupo1.markapbe.service;

import org.grupo1.markapbe.controller.dto.AuthCreateUserRequest;
import org.grupo1.markapbe.controller.dto.AuthLoginRequest;
import org.grupo1.markapbe.controller.dto.AuthResponse;
import org.grupo1.markapbe.persistence.entity.RoleEntity;
import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.RoleRepository;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.grupo1.markapbe.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
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
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario" + username + "no existe"));
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

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        List<String> roleRequest = authCreateUserRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest).stream().collect(Collectors.toSet());

        if (roleEntitySet.isEmpty()){
            throw new IllegalArgumentException("Los roles especificados no existen.");
        }

        UserEntity userEntity = UserEntity.builder().username(username).password(passwordEncoder.encode(password)).roles(roleEntitySet).isEnabled(true).accountNoLocked(true).accountNoExpired(true).credentialNoExpired(true).build();

        UserEntity userCreated = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userCreated.getRoles().stream().flatMap(role -> role.getPermissionSet().stream()).forEach(permissionEntity -> authorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated.getUsername(),userCreated.getPassword(),authorityList);
        String accessToken = jwtUtils.createToken(authentication);
        AuthResponse authResponse = new AuthResponse(userCreated.getUsername(),"User Created Successfully",accessToken,true);

        return authResponse;
    }


}
