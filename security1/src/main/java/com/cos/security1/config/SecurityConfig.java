package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity      // spring security filter가 Spring filter chain 에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * password 암호화
     * @return
     */
    @Bean
    public BCryptPasswordEncoder encodePw(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{       // Spring Security 설정
        http.csrf().disable();      // csrf 비활성화

        // URL에 따른 접근 제한 처리
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()        // URL user : 인증이 되어야 함
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")      // URL manager : 권한 'ROLE_ADMIN', 'ROLE_MANAGER'가 있어야함
                .antMatchers("admin/**").access("hasRole('ROLE_ADMIN')")        // URL admin : 권한 'ROLE_ADMIN'이 있어야함
                .anyRequest().permitAll()      // 위에 명시되지 않은 URL 은 로그인 및 권한 검사 X
                .and()
                .formLogin()
                .loginPage("/loginForm");       // formLogin이 필요한 경우, /login 으로 보낸다.

    }
}
