package com.k0s.security;

import com.k0s.entity.Product;
import com.k0s.security.user.User;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

//@Builder
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Session {
    private String token;
    private User user;
    private LocalDateTime expireDate;
    private List<Product> cart;

}
