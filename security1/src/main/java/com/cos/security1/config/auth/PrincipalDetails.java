package com.cos.security1.config.auth;

// 시큐리티가 /login url을 낚아채서 로그인을 진행시킴.
// 로그인을 하면 security 전용 session을 만들어준다. (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 한다.
// User 오브젝트 타입 => UserDetails 타입 객체

import com.cos.security1.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Security Session 안에 들어갈 수 있는 객체: Authentication
// Authentication 안에 들어갈 수 있는 객체: UserDetails

public class PrincipalDetails implements UserDetails {

    private User user;

    public PrincipalDetails(User user){
        this.user = user;
    }

    // 해당 유저의 권한을 리턴하는 method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = user.getRole();

        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role;
            }
        });

        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    // 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // 잠금 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 회원 사용 여부
    public boolean isEnabled() {
        return true;
    }
}
