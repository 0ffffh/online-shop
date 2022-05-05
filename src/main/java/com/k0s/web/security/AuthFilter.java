package com.k0s.web.security;

import com.k0s.security.user.Role;
import com.k0s.security.Session;
import com.k0s.security.SecurityService;
import com.k0s.service.ServiceLocator;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AuthFilter implements Filter {
    private static final String USER_TOKEN = "user-token";
    private static final String SESSION_ATT = "session";
    private static final List<String> SKIP_AUTHORIZATION_PATH_LIST = new ArrayList<>();
    private final SecurityService securityService = ServiceLocator.getService(SecurityService.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Authorization filter init");
        SKIP_AUTHORIZATION_PATH_LIST.addAll(securityService.getSkipList());
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String requestPath = req.getServletPath();

        if (SKIP_AUTHORIZATION_PATH_LIST.contains(requestPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
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
                if(session.isPresent()){
                    if(role.equals(session.get().getUser().getRole())){
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
