package com.smartshop.smartshop.session;

import com.smartshop.smartshop.models.user.UserRole;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SessionUser implements Serializable {
    private Long id;
    private String username;
    private UserRole role;
}

