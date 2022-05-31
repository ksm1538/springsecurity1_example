package com.cos.security1.config.oauth2;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserSerivce extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    // 구글 로그인 작동 방식
    // 구글로그인버튼 클릭 > 구글로그인 > code 리턴(OAuth-Client Library) > AccessToken 요청
    // userRequest 정보 > loadUser함수 호출 > 구글로부터 회원 프로필을 받아줌

    // 구글로부터 받은 userRequest 데이터에 대한 후처리 함수
    // 함수 종료 시, @AuthenticationPrincipal 어노테이션이 만들어진다.
    // OAuth2 로그인 시 사용
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        System.out.println("getClientRegistration: "+userRequest.getClientRegistration());  // registration: 어떤 매체에서 로그인했는지 확인 가능
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());    // access token
        System.out.println("getAttributes: "+super.loadUser(userRequest).getAttributes());      // 프로필 정보

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("getAttributes: "+oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getClientId();        // google
        String providerId = oAuth2User.getAttribute("sub");

        // OAuth2 로그인 시, username과 password는 필요없지만 형식상 넣어줌
        String username = provider + "_" + providerId;
        System.out.println("username: "+username);
        String password = bCryptPasswordEncoder.encode("provider");
        String role = "ROLE_USER";
        String email = oAuth2User.getAttribute("email");

        User findUser = userRepository.findByUsername(username);

        if(findUser == null){
            findUser = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(findUser);
        }

        return new PrincipalDetails(findUser, oAuth2User.getAttributes());
    }
}
