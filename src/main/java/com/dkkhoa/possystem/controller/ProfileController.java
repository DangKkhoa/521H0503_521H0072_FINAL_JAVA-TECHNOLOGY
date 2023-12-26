package com.dkkhoa.possystem.controller;

import com.dkkhoa.possystem.model.users.SessionUser;
import com.dkkhoa.possystem.model.users.User;
import com.dkkhoa.possystem.model.users.UserRepository;
import com.dkkhoa.possystem.model.users.UserService;
import com.dkkhoa.possystem.utils.RenameFileUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private RenameFileUtil renameFileUtil;

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/upload/profile";

    @GetMapping("")
    private String profile(@RequestParam(name = "success", required = false) boolean success, HttpSession session, Model model, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        SessionUser userSession = (SessionUser) session.getAttribute("user");
        System.out.println(success);

        if(userSession == null) {
            return "redirect:/login";
        }

        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }


        model.addAttribute("user", userSession);
        model.addAttribute("success", success);


        return "profile";
    }

    @PostMapping("")
    private String updateProfile(@RequestParam(name = "avatar", required = false) MultipartFile file,
                                 @RequestParam(name = "fullname", required = true) String fullname,
                                 @RequestParam(name = "email", required = true) String email,
                                 @RequestParam(name = "username", required = true) String username,
                                 @RequestParam(name = "phone", required = false) String phone,
                                 HttpSession session, Model model) throws IOException {
        SessionUser userSession = (SessionUser) session.getAttribute("user");
//        User userDb = userRepo.getUserByUserId(userSession.getId());

        if(userSession == null) {
            return "redirect:/login";
        }

        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }




        String currentAvatar = userSession.getProfilePicture();
        System.out.println("Current avatar: " + currentAvatar);
        if(!file.isEmpty() && file != null) {
            String newFilename = renameFileUtil.gerateNewFileName(username, file, userSession.getId());
            StringBuilder fileName = new StringBuilder();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, newFilename);
            fileName.append(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            currentAvatar = newFilename;
        }

        boolean isUpdated = userService.updateProfile(currentAvatar, fullname, email, username, phone, userSession.getId());
        if(isUpdated) {
            userSession.setProfilePicture(currentAvatar);
            System.out.println("Updated Avatar: " + userSession.getProfilePicture());
            userSession.setSessionFullname(fullname);
            userSession.setEmail(email);
            userSession.setUsername(username);
            userSession.setPhoneNumber(phone);

//            model.addAttribute("success", true);
//            model.addAttribute("user", userSession);
            session.setAttribute("user", userSession);
            return "redirect:/profile?success=true";
        }
        model.addAttribute("error", "Error while updating profile");
        return "error";
    }

    @GetMapping("/salesperson/{salespersonId}")
    private String viewSalespersonProfile(@PathVariable("salespersonId") int salespersonId, HttpSession session, Model model, HttpServletResponse response) {

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        SessionUser userSession = (SessionUser) session.getAttribute("user");

        if(userSession == null) {
            return "redirect:/login";
        }

        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }

        User salesperson = userRepo.getUserByUserId(salespersonId);
        model.addAttribute("salesperson", salesperson);
        model.addAttribute("user", userSession);
        return "viewAndUpdateSalespersonProfile";

    }

    @PostMapping("/salesperson/{salespersonId}")
    private String updateSalespersonProfile(@PathVariable("salespersonId") int salespersonId,
                                 @RequestParam(name = "avatar", required = false) MultipartFile file,
                                 @RequestParam(name = "fullname", required = true) String fullname,
                                 @RequestParam(name = "email", required = true) String email,
                                 @RequestParam(name = "username", required = true) String username,
                                 @RequestParam(name = "phone", required = false) String phone,
                                 HttpSession session, Model model) throws IOException {
        SessionUser userSession = (SessionUser) session.getAttribute("user");
//        User userDb = userRepo.getUserByUserId(userSession.getId());

        if(userSession == null) {
            return "redirect:/login";
        }

        if(userSession.isFirstLogin() && !userSession.isAdmin()) {
            return "redirect:/set_password";
        }

        String currentAvatar = userSession.getProfilePicture();
        if(!file.isEmpty() && file != null) {
            String newFilename = renameFileUtil.gerateNewFileName(username, file, salespersonId);
            StringBuilder fileName = new StringBuilder();
            Path filePath = Paths.get(UPLOAD_DIRECTORY, newFilename);
            fileName.append(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            currentAvatar = newFilename;
        }

        boolean isUpdated = userService.updateProfile(currentAvatar, fullname, email, username, phone, salespersonId);
        if(isUpdated) {
            return "redirect:/salespersons";
        }
        model.addAttribute("error", "Error while updating profile");
        return "error";
    }
}
