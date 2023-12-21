package com.dkkhoa.possystem.model.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionUser{
    int id;
    String username;
//    String email;
    String sessionFullname;
    boolean isAdmin;
    String profilePicture;
    boolean firstLogin;


}
