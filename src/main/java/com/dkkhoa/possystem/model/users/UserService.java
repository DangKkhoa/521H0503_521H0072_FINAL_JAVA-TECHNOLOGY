package com.dkkhoa.possystem.model.users;

import com.dkkhoa.possystem.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JWTUtil jwtUtil;

    public boolean addSalesperson(String fullname, String email) {
        try {
            String username = email.split("@")[0]; // username is the part before '@'
            String password = passwordEncoder.encode(username);

            String token = jwtUtil.generateToken(username);

            User salesperson = new User();
            salesperson.setUsername(username);
            salesperson.setFullname(fullname);
            salesperson.setPassword(password);
            salesperson.setEmail(email);
            salesperson.setAdmin(false);
            salesperson.setLocked(false);
            salesperson.setFirstLogin(true);
            salesperson.setProfilePicture("user.jpg");
            salesperson.setToken(token);

            userRepo.save(salesperson);
            System.out.println(username);
            String loginLink = "http://localhost:8080/first_login?token=" + token;

            String subject = "YOUR ACCOUNT HAS BEEN CREATED";
            String body = "<p style=\"font-size: 14px\"><strong>Username:</strong> " + username + "</p>" +
                    "<p style=\"font-size: 14px\"><strong>Password:</strong> " + username + "</p>" +
                    "<h2 style=\"font-style:italic\"><span style=\"color: red\">Note:</span> You will be asked to change your password when logging in for the first time !</h2>" +
                    "<a href=\"" + loginLink + "\">" + loginLink + "</a>";
            emailService.sendEmail(email, subject, body);
            return true;
        }
        catch (Exception e) {
            System.out.println("Error occured while adding new salesperson: " + e);
            return false;
        }



    }

    public boolean deleteSalesperson(int salespersonId) {
        try {
            userRepo.deleteById(salespersonId);
            return true;
        }
        catch (Exception e) {
            System.out.println("Error occured while deleting salesperson with id " + salespersonId + ": " + e);
            return false;
        }
    }
}
