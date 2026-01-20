package com.oris_sem01.travelplanner.web.filter;

import com.oris_sem01.travelplanner.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        boolean publicPath = path.equals("/")
                || path.startsWith("/login")
                || path.startsWith("/register")
                || path.startsWith("/static/")
                || path.startsWith("/tours")
                || path.startsWith("/api/tours");

        if (publicPath) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }
}

//Проверяет: есть ли пользователь в сессии?
// Если это /admin и пользователь не админ → 403 Forbidden.
// Если всё ОК → пускаем дальше.
