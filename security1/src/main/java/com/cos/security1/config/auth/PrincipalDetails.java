package com.cos.security1.config.auth;

// 시큐리티가 /login url을 낚아채서 로그인을 진행시킴.
// 로그인을 하면 security 전용 session을 만들어준다. (Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 한다.
// User 오브젝트 타입 => UserDetails 타입 객체

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// Security Session 안에 들어갈 수 있는 객체: Authentication
// Authentication 안에 들어갈 수 있는 객체: UserDetails, OAuth2User
// 즉, PrincipalDetails를 사용하는 이유는 Oauth2로 로그인 하는 사용자와 일반 로그인 하는 사용자를 하나의 객체로 이용하기 위함이다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attibutes;

    // 일반 로그인
    public PrincipalDetails(User user){
        this.user = user;
    }

    // OAuth2 로그인
    public PrincipalDetails(User user, Map<String, Object> attibutes){
        this.user = user;
        this.attibutes = attibutes;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    @Override
    public Map<String, Object> getAttributes() {
        return attibutes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public String getName() {
        return null;
    }
}
