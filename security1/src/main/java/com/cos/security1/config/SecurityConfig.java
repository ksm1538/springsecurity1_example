package com.cos.security1.config;

import com.cos.security1.config.filter.MyFilter1;
import com.cos.security1.config.filter.MyFilter3;
import com.cos.security1.config.oauth2.PrincipalOauth2UserSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;


// 구글 로그인 과정
// 1. 코드받기(인증) 2. 액세스토큰(권한) 3. 사용자 프로필정보 가져오기 4. 그 정보를 토대로 회원가입 진행

@Configuration
@EnableWebSecurity      // spring security filter가 Spring filter chain 에 등록
// securedEnabled: @Secured 어노테이션 활성화. @Secured? 특정 URL에 대해서만 간단하게 권한 처리를 할 수 있는 어노테이션
// prePostEnabled: @PreAuthorize, @PostAuthorize 어노테이션 활성화.
// @PreAuthorize: 해당 메소드 진입 전 처리. @PostAuthorize: 해당 메소드 진입 후 처리
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserSerivce principalOauth2UserSerivce;

    @Autowired
    private final CorsFilter corsFilter;

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

        /********* JWT 관련 설정 부분(시작) ******/
        // Spring Security 기본 로그인 및 OAuth2를 사용하기 위해서는 아래의 설정 주석 처리 필요
        ///*

        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);     // BasicAuthenticationFilter 이전에 MyFilter3을 추가하겠다.
        // 시큐리티 필터가 커스텀 필터보다 먼저 실행됨

        // 커스텀 필터를 시큐리티 필터보다 먼저 실행시키는 방법
        // 시큐리티 필터 중, 가장 먼저 실행되는 필터는 SecurityContextPersistenceFilter 이므로 이것보다 앞에 위치하면 됨.0
        //http.addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)         // 세션을 사용하지 않겠다라는 의미
                        .and()
                        .addFilter(corsFilter)              // 인증이 있어야할 때 시큐리티 필터에 등록. (인증이 필요없을 때 해당 컨트롤러에 @CrossOrigin 어노테이션을 사용하면 됨)
                        .formLogin().disable()              // Spring Security 로그인 사용 X
                        .httpBasic().disable()              // http를 제외한 다른 방식(js로 요청 등)을 허용하겠다.
                        .authorizeRequests()
                        .antMatchers("/api/v1/user/**")
                        .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/api/v1/manager/**")
                        .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                        .antMatchers("/api/v1/admin/**")
                        .access("hasRole('ROLE_ADMIN')")
                        .anyRequest().permitAll();
        //*/
        /********* JWT 관련 설정 부분(끝) ******/

        /********* Spring Security 기본 로그인 및 Oauth2 설정 부분(시작) ******/
        // JWT를 사용하기 위해서는 아래의 설정 주석 처리 필요

        /*
        // URL에 따른 접근 제한 처리
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()        // URL user : 인증이 되어야 함
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")      // URL manager : 권한 'ROLE_ADMIN', 'ROLE_MANAGER'가 있어야함
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")         // URL admin : 권한 'ROLE_ADMIN'이 있어야함
                .anyRequest().permitAll()      // 위에 명시되지 않은 URL 은 로그인 및 권한 검사 X
                .and()
                .formLogin()
                .loginPage("/loginForm")       // formLogin이 필요한 경우, /loginForm 으로 보낸다.
                .loginProcessingUrl("/login")   // login 주소가 호출이 되면 시큐리티가 낚이채서 대신 로그인을 진행
                .defaultSuccessUrl("/")        // login 성공 시, 보내줄 기본 url
                // OAuth2 로그인 설정(시작)
                .and()
                .oauth2Login()
                .loginPage("/loginForm")    // OAuth2의 로그인 페이지 URL.
                .userInfoEndpoint()
                .userService(principalOauth2UserSerivce)// 구글 로그인이 완료된 이후에 후 처리가 필요. (코드가 아닌 액세스토큰+사용자프로필의정보)를 가져옴
                // OAuth2 로그인 설정(끝)
        ;
        */
        /********* Spring Security 기본 로그인 및 Oauth2 설정 부분(끝) ******/
    }
}
