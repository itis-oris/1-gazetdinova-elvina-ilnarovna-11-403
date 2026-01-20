package com.oris_sem01.travelplanner.controller.auth;

import com.oris_sem01.travelplanner.config.FreemarkerConfig;
import com.oris_sem01.travelplanner.model.User;
import com.oris_sem01.travelplanner.service.UserService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;
    private Configuration freemarkerConfig;

    @Override
    public void init() throws ServletException {
        userService = (UserService) getServletContext().getAttribute("userService");
        freemarkerConfig = FreemarkerConfig.getConfig(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("request", req);

        Template template = freemarkerConfig.getTemplate("login.ftl");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Ошибка обработки шаблона login.ftl", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Optional<User> userOpt = userService.login(email, password);

        if (userOpt.isEmpty()) {
            Map<String, Object> model = new HashMap<>();
            model.put("error", "Неверный email или пароль");
            model.put("request", req);

            Template template = freemarkerConfig.getTemplate("login.ftl");
            resp.setContentType("text/html;charset=UTF-8");
            try {
                template.process(model, resp.getWriter());
            } catch (TemplateException e) {
                throw new ServletException("Ошибка обработки шаблона login.ftl", e);
            }
            return;
        }

        User user = userOpt.get();

        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
