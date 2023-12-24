package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/profile")
    private String profile(@RequestParam(name = "user_id") int userId, HttpSession session, Model model) {
        System.out.println("User id: " + userId);
        SessionUser userSession = (SessionUser) session.getAttribute("user");

        if(userSession == null) {
            return "redirect:/login";
        }

        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }

        User userDb = userRepo.getUserByUserId(userId);
        model.addAttribute("user", userSession);


        return "profile";
    }
}
