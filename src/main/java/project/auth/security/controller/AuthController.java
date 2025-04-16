package project.auth.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.auth.security.domain.Member;
import project.auth.security.domain.Role;
import project.auth.security.domain.SignupRequest;
import project.auth.security.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
//    public String home(@AuthenticationPrincipal Member member, Model model) {
//        model.addAttribute("username", member.getUsername());
//        return "home";
//    }
//    public String home(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        Object principal = authentication.getPrincipal();
//
//        model.addAttribute("username", username);
//        model.addAttribute("member", principal);
//        return "home";
//    }
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequest());
        return "signup";
    }

    @PostMapping("/signup")
//    public String signup(@RequestParam("username") String username,
//                         @RequestParam("password") String password) {
//        Member member = Member.builder()
//                .username(username)
//                .password(passwordEncoder.encode(password))
//                .build();
//        memberRepository.save(member);
//
//        return "redirect:/login";
//    }
    public String signup(@Valid SignupRequest request, BindingResult bindingResult, Model model) {
        if(memberRepository.findByUsername(request.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "duplicate", "이미 사용 중인 아이디입니다.");
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("signupRequest", request);
            return "signup";
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        memberRepository.save(member);

        return "redirect:/login";
    }
}
