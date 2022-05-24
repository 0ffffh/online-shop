package com.k0s.security;

import com.k0s.security.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j

@Component("AuthFilter")
public class AuthFilter implements Filter {
    private static final String USER_TOKEN = "user-token";
    private static final String SESSION_ATT = "session";

    @Value("${security.skipPath}")
    private String skipPaths;

    @Autowired
    private SecurityService securityService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String requestPath = req.getServletPath();

        for (String s : skipPaths.split(",")) {
            if (requestPath.contains(s)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        Optional<Session> session = Optional.empty();
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (USER_TOKEN.equals(cookie.getName())) {
                    session = Optional.ofNullable(securityService.getSession(cookie.getValue()));
                    break;
                }
            }
        }
        req.setAttribute(SESSION_ATT, session.orElse(null));
        resp.setContentType("text/html;charset=utf-8");
        for (Role role : Role.values()) {
            if (requestPath.contains(role.getRole())) {
                if (session.isPresent()) {
                    if (role.equals(session.get().getUser().getRole())) {
                        filterChain.doFilter(servletRequest, servletResponse);
                    }
                } else {
                    log.info("Not authorized {} try connect to {}", req.getRemoteAddr(), req.getRequestURL());
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
                return;
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

}
