package com.cos.security1.config.auth;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Security 설정에서 loginProcessingUrl("/login")으로 걸었기 때문에
// "/login" 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 메소드가 실행된다.
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Security Session = Authentication
    // Authentication => UserDetails
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        User userEntity = userRepository.findByUsername(username);

        if(userEntity != null){
            return new PrincipalDetails(userEntity);
        }

        return null;
    }
}
