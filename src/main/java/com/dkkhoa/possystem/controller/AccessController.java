package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import com.dkkhoa.possystem.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private JWTUtil jwtUtil;

    @GetMapping("/login")
    private String login(HttpSession session) {
        System.out.println("hello");
        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
//    @ResponseBody
    private String login_auth(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User authenticatingUser = userRepo.findByUsername(username);
//        int id = authenticatingUser.getUserId();
//        String storedusername = authenticatingUser.getUsername();
//        System.out.println(id + " " + storedusername);

//        System.out.println(username);
//        System.out.println(authenticatingUser);
        if(authenticatingUser != null && !authenticatingUser.isLocked()) {
            if(authenticatingUser.getToken() != null && !authenticatingUser.isAdmin()) {
                model.addAttribute("error", "YOU MUST LOGIN VIA THE LINK THAT WAS SENT TO YOUR EMAIL");
                return "error";
            }
            String passwordHashed = passwordEncoder.encode(password);
            System.out.println(passwordHashed);
            if (passwordEncoder.matches(password, authenticatingUser.getPassword())) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setId(authenticatingUser.getUserId());
                sessionUser.setUsername(authenticatingUser.getUsername());
                sessionUser.setSessionFullname(authenticatingUser.getFullname());
                sessionUser.setAdmin(authenticatingUser.isAdmin());
                sessionUser.setProfilePicture(authenticatingUser.getProfilePicture());
                sessionUser.setEmail(authenticatingUser.getEmail());
                sessionUser.setPhoneNumber(authenticatingUser.getPhone());


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
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        if(session.getAttribute("user") == null) return  "redirect:/login";

        session.removeAttribute("user");

        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/first_login")
//    @ResponseBody
    private String firstLogin(@RequestParam(name = "token") String token, Model model) {
        boolean isValid = jwtUtil.validateToken(token);
        System.out.println(isValid);

        if(isValid) {
            return "firstLogin";
        }
        model.addAttribute("error", "YOUR LINK IS INVALID OR EXPIRED. PLEASE CONTACT ADMIN FOR NEW LINK");
        return "error";

    }

    @PostMapping("/first_login")
    private String handleFirstLogin(@RequestParam(name = "username", required = true) String username, @RequestParam(name = "password", required = true) String password, HttpSession session, Model model) {
        User authenticatingUser = userRepo.findByUsername(username);

        if(authenticatingUser != null) {
            if (passwordEncoder.matches(password, authenticatingUser.getPassword())) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setId(authenticatingUser.getUserId());
                sessionUser.setUsername(authenticatingUser.getUsername());
                sessionUser.setSessionFullname(authenticatingUser.getFullname());
                sessionUser.setAdmin(authenticatingUser.isAdmin());
                sessionUser.setProfilePicture(authenticatingUser.getProfilePicture());
                sessionUser.setEmail(authenticatingUser.getEmail());


//                sessionUser.setFirstLogin(authenticatingUser.isFirstLogin());
                session.setAttribute("user", sessionUser);
                if(!authenticatingUser.isAdmin()) {
                    return "redirect:/set_password";
                }
                return "redirect:/";
            }
            else {
                model.addAttribute("username", username);
                model.addAttribute("error", "Wrong username or password");
                return "firstLogin";
            }


        }
        model.addAttribute("username", username);
        model.addAttribute("error", "Account does not exist");
        return "firstLogin";
    }


    @GetMapping("/set_password")
    private String setPassword(HttpSession session) {
        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user == null) {
            return "redirect:/login";
        }

        return "setPassword";

    }

    @PostMapping("/set_password")
    private String handleSetPassword(@RequestParam(name = "currentPassword") String currentPassword, @RequestParam(name = "newPassword") String newPassword, Model model, HttpSession session) {
        try {
            SessionUser userSession = (SessionUser) session.getAttribute("user");
            User userDb = userRepo.getUserByUserId(userSession.getId());
            System.out.println("Current pass: " + currentPassword + " - New password: " + newPassword);
            boolean isMatched = passwordEncoder.matches(currentPassword, userDb.getPassword());
            if(!isMatched) {
                model.addAttribute("error", "Incorrect password. Please try again");
                return "setPassword";

            }
            if(newPassword.equals(currentPassword)) {
                model.addAttribute("error", "New password must be different from the current password");
                return "setPassword";
            }
            if(newPassword.length() < 10) {
                model.addAttribute("error", "New password must contain at least 10 characters");
                return "setPassword";
            }

            String password = passwordEncoder.encode(newPassword);
            userDb.setPassword(password);
            userDb.setFirstLogin(false);
            userDb.setToken(null);
            userRepo.save(userDb);
            return "redirect:/";
        }
        catch (Exception e) {
            System.out.println("Error occured while setting new password: " + e);
            return "setPassword";
        }



    }
}
