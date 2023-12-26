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

        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user != null) {
            return "redirect:/home";
        }

        return "login";
    }

    @PostMapping("/login")
//    @ResponseBody
    private String login_auth(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        User authenticatingUser = userRepo.findByUsername(username);
        if(authenticatingUser != null && !authenticatingUser.isLocked()) {
            if(authenticatingUser.getToken() != null && !authenticatingUser.isAdmin()) {
                model.addAttribute("error", "YOU MUST LOGIN VIA THE LINK THAT WAS SENT TO YOUR EMAIL");
                return "error";
            }
            String passwordHashed = passwordEncoder.encode(password);
//            System.out.println(passwordHashed);
            if (passwordEncoder.matches(password, authenticatingUser.getPassword())) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setId(authenticatingUser.getUserId());
                sessionUser.setUsername(authenticatingUser.getUsername());
                sessionUser.setSessionFullname(authenticatingUser.getFullname());
                sessionUser.setAdmin(authenticatingUser.isAdmin());
                sessionUser.setProfilePicture(authenticatingUser.getProfilePicture());
                sessionUser.setEmail(authenticatingUser.getEmail());
                sessionUser.setPhoneNumber(authenticatingUser.getPhone());

                session.setAttribute("user", sessionUser);
                if(authenticatingUser.isFirstLogin() && !authenticatingUser.isAdmin()) {
                    return "redirect:/set_password";
                }
                return "redirect:/home";
            }
            else {
                model.addAttribute("username", username);
                model.addAttribute("error", "Wrong password");
                return "login";
            }


        }
        model.addAttribute("username", username);
        model.addAttribute("error", "Account does not exist");
        return "login";
    }


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
    private String firstLogin(@RequestParam(name = "token") String token, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        boolean isValid = jwtUtil.validateToken(token);
//        System.out.println(isValid);

        if(isValid) {
            return "firstLogin";
        }
        model.addAttribute("error", "YOUR LINK IS INVALID OR EXPIRED. PLEASE CONTACT ADMIN FOR NEW LINK");
        return "error";

    }

    @PostMapping("/first_login")
    private String handleFirstLogin(@RequestParam(name = "username", required = true) String username, @RequestParam(name = "password", required = true) String password, HttpSession session, Model model) {
        User authenticatingUser = userRepo.findByUsername(username);

//        System.out.println(authenticatingUser);
        if(authenticatingUser != null) {
            if (passwordEncoder.matches(password, authenticatingUser.getPassword())) {
                SessionUser sessionUser = new SessionUser();
                sessionUser.setId(authenticatingUser.getUserId());
                sessionUser.setUsername(authenticatingUser.getUsername());
                sessionUser.setSessionFullname(authenticatingUser.getFullname());
                sessionUser.setAdmin(authenticatingUser.isAdmin());
                sessionUser.setProfilePicture(authenticatingUser.getProfilePicture());
                sessionUser.setEmail(authenticatingUser.getEmail());
                sessionUser.setFirstLogin(authenticatingUser.isFirstLogin());


//                sessionUser.setFirstLogin(authenticatingUser.isFirstLogin());
                session.setAttribute("user", sessionUser);
                if(!authenticatingUser.isAdmin()) {
                    return "redirect:/set_password";
                }
                return "redirect:/home";
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
    private String setPassword(HttpSession session, Model model) {
        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user == null) {
            return "redirect:/login";
        }

        boolean isFirstLogin = user.isFirstLogin();
        model.addAttribute("isFirstLogin", isFirstLogin);
        model.addAttribute("isAdmin", user.isAdmin());

        return "setPassword";

    }

    @PostMapping("/set_password")
    private String handleSetPassword(@RequestParam(name = "currentPassword") String currentPassword, @RequestParam(name = "newPassword") String newPassword, Model model, HttpSession session) {
        SessionUser userSession = (SessionUser) session.getAttribute("user");
        User userDb = userRepo.getUserByUserId(userSession.getId());
        try {
//            System.out.println("Current pass: " + currentPassword + " - New password: " + newPassword);
            boolean isMatched = passwordEncoder.matches(currentPassword, userDb.getPassword());
            if(!isMatched) {
                model.addAttribute("error", "Incorrect password. Please try again");
                model.addAttribute("isFirstLogin", userSession.isFirstLogin());
                model.addAttribute("isAdmin", userSession.isAdmin());
                return "setPassword";

            }
            if(newPassword.equals(currentPassword)) {
                model.addAttribute("error", "New password must be different from the current password");
                model.addAttribute("isFirstLogin", userSession.isFirstLogin());
                model.addAttribute("isAdmin", userSession.isAdmin());
                return "setPassword";
            }
            if(newPassword.length() < 10) {
                model.addAttribute("error", "New password must contain at least 10 characters");
                model.addAttribute("isFirstLogin", userSession.isFirstLogin());
                model.addAttribute("isAdmin", userSession.isAdmin());
                return "setPassword";
            }

            String password = passwordEncoder.encode(newPassword);
            userDb.setPassword(password);
            userDb.setFirstLogin(false);
            userDb.setToken(null);
            userSession.setFirstLogin(false);
            userRepo.save(userDb);
            return "redirect:/";
        }
        catch (Exception e) {
            System.out.println("Error occured while setting new password: " + e);
            model.addAttribute("isFirstLogin", userSession.isFirstLogin());
            model.addAttribute("isAdmin", userSession.isAdmin());
            return "setPassword";
        }



    }
}
