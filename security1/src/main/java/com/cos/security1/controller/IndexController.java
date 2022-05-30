package com.cos.security1.controller;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @GetMapping({"", "/"})
    public String index(){
        // mustache default folder : src/main/resources/
        // view resolver setting : templates (prefix), .mustache (suffix)
        return "index"; // src/main/resources/templates/index.mustache
    }

    @GetMapping({"/user"})
    @ResponseBody
    public String user(){
        return "user";
    }

    @GetMapping({"/admin"})
    @ResponseBody
    public String admin(){
        return "admin";
    }

    @GetMapping({"/manager"})
    @ResponseBody
    public String manager(){
        return "manager";
    }

    // /login SpringSecurity 에서 사용하지만, SecurityConfig 에서 설정 가능
    // 로그인 폼으로 이동
    @GetMapping({"/loginForm"})
    public String loginForm(){
        return "loginForm";
    }

    // 회원가입 폼으로 이동
    @GetMapping({"/joinForm"})
    public String joinForm(){
        return "joinForm";
    }

    // 회원가입 로직
    @PostMapping({"/join"})
    public String join(User user){
        user.setRole("ROLE_ADMIN");

        // 패스워드 암호화를 진행하지 않으면 시큐리티 로그인이 불가능
        String encPw = encoder.encode(user.getPassword());
        user.setPassword(encPw);

        userRepository.save(user);

        return "redirect:/loginForm";
    }

    @GetMapping({"/joinProc"})
    @ResponseBody
    public String joinProc(){
        return "회원가입 완료";
    }

    @Secured("ROLE_ADMIN")          // 이 메소드에 대해서만 특정 권한이 필요할 때 사용 가능
    @GetMapping("/info")
    @ResponseBody
    public String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")       // 메소드 접근 이전에 확인하는 로직
    //@PostAuthorize()                                                      // 메소드 접근 이후에 확인하는 로직
    @GetMapping("/data")
    @ResponseBody
    public String data(){
        return "데이터정보";
    }
}
