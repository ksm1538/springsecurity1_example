package com.cos.security1.config;

import com.cos.security1.config.oauth2.PrincipalOauth2UserSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


// 구글 로그인 과정
// 1. 코드받기(인증) 2. 액세스토큰(권한) 3. 사용자 프로필정보 가져오기 4. 그 정보를 토대로 회원가입 진행

@Configuration
@EnableWebSecurity      // spring security filter가 Spring filter chain 에 등록
// securedEnabled: @Secured 어노테이션 활성화. @Secured? 특정 URL에 대해서만 간단하게 권한 처리를 할 수 있는 어노테이션
// prePostEnabled: @PreAuthorize, @PostAuthorize 어노테이션 활성화.
// @PreAuthorize: 해당 메소드 진입 전 처리. @PostAuthorize: 해당 메소드 진입 후 처리
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserSerivce principalOauth2UserSerivce;

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
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")         // URL admin : 권한 'ROLE_ADMIN'이 있어야함
                .anyRequest().permitAll()      // 위에 명시되지 않은 URL 은 로그인 및 권한 검사 X
                .and()
                .formLogin()
                .loginPage("/loginForm")       // formLogin이 필요한 경우, /login 으로 보낸다.
                .loginProcessingUrl("/login")   // login 주소가 호출이 되면 시큐리티가 낚이채서 대신 로그인을 진행
                .defaultSuccessUrl("/")        // login 성공 시, 보내줄 기본 url
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                .userInfoEndpoint()
                .userService(principalOauth2UserSerivce)// 구글 로그인이 완료된 이후에 후 처리가 필요. (코드가 아닌 액세스토큰+사용자프로필의정보)를 가져옴
        ;


    }
}
