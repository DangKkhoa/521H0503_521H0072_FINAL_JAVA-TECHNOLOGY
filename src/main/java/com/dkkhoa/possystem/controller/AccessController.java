package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;

@Controller
public class AccessController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    private String login(HttpSession session) {
        System.out.println("hello");

        return "login";
    }

    @PostMapping("/login")
//    @ResponseBody
    private String login_auth(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User authenticatingUser = userRepo.findByUsername(username);
        int id = authenticatingUser.getUserId();
        String storedusername = authenticatingUser.getUsername();
        System.out.println(id + " " + storedusername);

//        System.out.println(username);
//        System.out.println(authenticatingUser);
        if(authenticatingUser != null) {
            String passwordHashed = passwordEncoder.encode(password);
            System.out.println(passwordHashed);
            if (passwordEncoder.matches(password, authenticatingUser.getPassword())) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setId(authenticatingUser.getUserId());
                sessionUser.setUsername(authenticatingUser.getUsername());
                sessionUser.setSessionFullname(authenticatingUser.getFullname());
                sessionUser.setAdmin(authenticatingUser.isAdmin());
                sessionUser.setProfilePicture(authenticatingUser.getProfilePicture());
//                sessionUser.setFirstLogin(authenticatingUser.isFirstLogin());
                System.out.println("Toi day roi ne");
                System.out.println(sessionUser);
                System.out.println("Toi day luon r");
                session.setAttribute("user", sessionUser);
                if(authenticatingUser.isFirstLogin() && !authenticatingUser.isAdmin()) {
                    return "redirect:/set_password";
                }
                return "redirect:/";
            }
            else {
                model.addAttribute("username", username);
                model.addAttribute("error", "Wrong username or password");
                return "login";
            }


        }
        model.addAttribute("username", username);
        model.addAttribute("error", "Account does not exist");
        return "login";
    }

//    @GetMapping("/login_succeed")
//    @ResponseBody
//    public String succeed(HttpServletRequest request) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication.toString());
//        User loginUser = userRepo.findByUsername(authentication.getName());
//        System.out.println(loginUser);
//        System.out.println(authentication.getName());
////        System.out.println(authentication.getAuthorities());
//        String role = authentication.getAuthorities().toString();
//        System.out.println(role.equals("[ROLE_Admin]"));
////        System.out.println(authentication.toString());
//        System.out.println(authentication.getDetails());
//        HttpSession session = request.getSession();
//        session.setAttribute("username", loginUser.getUsername());
//        session.setAttribute("fn", loginUser.getFullname());
//        session.setAttribute("profilePicture", loginUser.getProfilePicture());
//        session.setAttribute("firstLogin", loginUser.isFirstLogin());
//        session.setAttribute("role", role);
//        return "redirect:/";
//    }
    @GetMapping("/logout")
    private String logout(HttpSession session, HttpServletResponse response) {
        if(session.getAttribute("user") == null) return  "redirect/login";

        return "redirect:/login";

    }
}
