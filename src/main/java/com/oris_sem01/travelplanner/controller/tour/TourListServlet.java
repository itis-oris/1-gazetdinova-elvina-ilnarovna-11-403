package com.oris_sem01.travelplanner.controller.tour;

import com.oris_sem01.travelplanner.config.FreemarkerConfig;
import com.oris_sem01.travelplanner.model.Tour;
import com.oris_sem01.travelplanner.model.User;
import com.oris_sem01.travelplanner.service.TourService;
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
import java.util.List;
import java.util.Map;

@WebServlet({"/", "/tours"})
public class TourListServlet extends HttpServlet {

    private TourService tourService;
    private Configuration freemarkerConfig;

    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
        freemarkerConfig = FreemarkerConfig.getConfig(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {

        String servletPath = req.getServletPath(); // "/" или "/tours"
        String templateName = "/tours".equals(servletPath) ? "tours.ftl" : "index.ftl";

        List<Tour> tours = tourService.listAll();

        Map<String, Object> model = new HashMap<>();
        model.put("tours", tours);
        model.put("request", req);  // <--- ВАЖНО

        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                model.put("user", user);
            }
        }

        Template template = freemarkerConfig.getTemplate(templateName);
        resp.setContentType("text/html;charset=UTF-8");

        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Ошибка обработки шаблона " + templateName, e);
        }
    }
}
