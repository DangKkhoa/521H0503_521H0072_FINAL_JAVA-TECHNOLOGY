package com.dkkhoa.possystem.model.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    private String username;
    private String password;
    private String email;

    @Column(columnDefinition = "nvarchar(255) COLLATE utf8_unicode_ci")
    private String fullname;

    @Column(name = "is_admin")
    private boolean isAdmin;
    @Column(name = "profile_picture")
    private String profilePicture;
    @Column(name = "is_locked")
    private boolean isLocked;
    @Column(name = "first_login")
    private boolean firstLogin;
    private String token;
    private String phone;

}
