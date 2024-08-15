package org.grupo1.markapbe.service;

import org.grupo1.markapbe.persistence.entity.UserEntity;
import org.grupo1.markapbe.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() ->  new UsernameNotFoundException("El usuario" + username + "no existe"));
        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        userEntity.getRoles().forEach(role -> grantedAuthorityList.add( new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));
        userEntity.getRoles().stream().flatMap(role -> role.getPermissionSet().stream()).forEach(permissionEntity -> grantedAuthorityList.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new User(userEntity.getUsername(),userEntity.getPassword(),userEntity.isEnabled(), userEntity.isAccountNoExpired(),userEntity.isCredentialNoExpired(),userEntity.isAccountNoLocked(),grantedAuthorityList);
    }
}
