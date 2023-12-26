package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.users.*;
import com.dkkhoa.possystem.utils.JWTUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequestMapping("/salespersons")
public class SalespersonController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;


    @GetMapping("")
    private String viewSalespersons(HttpSession session, HttpServletResponse response, Model model) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        SessionUser user = (SessionUser) session.getAttribute("user");

        if(user == null ) {
            return "redirect:/login";
        }


        List<User> salespersons = userRepo.findByIsAdminFalse();
        long salespersonQuantity = userRepo.countByIsAdminFalse();
        System.out.println(salespersons);
        model.addAttribute("user", user);
        model.addAttribute("salespersons", salespersons);
        model.addAttribute("salespersonQuantity", salespersonQuantity);
        salespersons.forEach(s -> System.out.println(s.toString()));
        return "salespersons";
    }


    @PostMapping("/add")
    private String addSalesperson(@RequestParam(name = "fullname") String fullname, @RequestParam(name = "email") String email, Model model, HttpSession session) {
        boolean isAdded = userService.addSalesperson(fullname, email);
        return "redirect:/salespersons";
    }

//    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/delete/{salespersonId}")
    @ResponseBody
    private LinkedHashMap<String, Object> deleteSalesperson(@PathVariable("salespersonId") int salespersonId) {
        System.out.println(salespersonId);


        boolean isDeleted = userService.deleteSalesperson(salespersonId);
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        if(isDeleted) {
            response.put("code", 1);
            response.put("message", "Deleted salesperson successfully");

            return response;
        }

        response.put("code", 0);
        response.put("message", "Unable to delete salesperson");
        return response;
    }

    @PostMapping("/send_link/{salespersonId}")
    @ResponseBody
    private LinkedHashMap<String, Object> sendLink(@PathVariable("salespersonId") int salespersonId) throws MessagingException {
        System.out.println(salespersonId);
        User salesperson = userRepo.getUserByUserId(salespersonId);

        String email = salesperson.getEmail();
        String username = salesperson.getUsername();
//        String password = passwordEncoder.encode(salesperson.getPassword());
        String token = jwtUtil.generateToken(username);
        String loginLink = "http://localhost:8080/first_login?token=" + token;



        String subject = "YOUR ACCOUNT HAS BEEN CREATED";
        String body = "<p style=\"font-size: 14px\"><strong>Username:</strong> " + username + "</p>" +
                "<p style=\"font-size: 14px\"><strong>Password:</strong> " + username + "</p>" +
                "<h3 style=\"font-style:italic\"><span style=\"color: red\">Note:</span> The link is valid for only 1 minute. After this time, please contact the admin for a new link !</h3>" +
                "<a href=\"" + loginLink + "\">" + loginLink + "</a>";

        boolean isSent = emailService.sendEmail(email, subject, body);
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        if(isSent) {
            response.put("code", 1);
            response.put("message", "Email sent successfully");
            return response;
        }

        response.put("code", 0);
        response.put("message", "Unable to sent email to salesperson");
        return response;
    }

    @PostMapping("/lock_unlock/{salespersonId}")
    @ResponseBody
    private LinkedHashMap<String, Object> lockUnlockSalesperson(@PathVariable("salespersonId") String salespersonId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        try {
            System.out.println(salespersonId);
            User salesperson = userRepo.getUserByUserId(Integer.parseInt(salespersonId));

            boolean isLocked = salesperson.isLocked();
            System.out.println(isLocked);

            // if user is locked, unlock user
            if(isLocked) {
                salesperson.setLocked(false);
                userRepo.save(salesperson);
                response.put("code", 1);
                response.put("message", "Salesperson Unlocked");
            }
            else {
                salesperson.setLocked(true);
                userRepo.save(salesperson);
                response.put("code", 1);
                response.put("message", "Salesperson Locked");

            }
            return response;
        }
        catch (Exception e) {
            System.out.println(e);
            response.put("code", 0);
            response.put("message", "Salesperson not found");
            return response;
        }
    }
}
