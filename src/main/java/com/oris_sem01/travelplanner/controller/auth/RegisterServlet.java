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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private Configuration freemarkerConfig;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        freemarkerConfig = FreemarkerConfig.getConfig(getServletContext());
        userService = (UserService) getServletContext().getAttribute("userService");
        if (userService == null) {
            throw new ServletException("userService не найден в ServletContext");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<>();
        model.put("request", req);
        renderTemplate(resp, model);
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {

        // чтобы русские буквы нормально читались
        req.setCharacterEncoding("UTF-8");

        // поля из формы (username = имя)
        String username = req.getParameter("username");
        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        Map<String, Object> model = new HashMap<>();
        model.put("request", req);
        model.put("username", username);
        model.put("email", email);

        // базовая проверка заполненности
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || password == null || password.isBlank()) {

            model.put("error", "Заполни все поля");
            renderTemplate(resp, model);
            return;
        }

        // длина пароля
        if (password.length() < 6) {
            model.put("error", "Пароль должен быть минимум 6 символов");
            renderTemplate(resp, model);
            return;
        }

        try {
            // username используем как firstName, lastName пока пустой
            String firstName = username.trim();
            String lastName  = null;

            // под твой UserService: (email, firstName, lastName, password)
            User user = userService.register(email.trim(), firstName, lastName, password);

            // сразу логиним пользователя
            req.getSession(true).setAttribute("user", user);

            // на главную
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (IllegalArgumentException e) {
            // например, "email уже существует" из UserService
            model.put("error", e.getMessage());
            renderTemplate(resp, model);
        }
    }

    private void renderTemplate(HttpServletResponse resp,
                                Map<String, Object> model) throws IOException, ServletException {
        try {
            Template template = freemarkerConfig.getTemplate("register.ftl");
            resp.setContentType("text/html;charset=UTF-8");
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Ошибка обработки шаблона register.ftl", e);
        }
    }
}
