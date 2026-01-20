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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/tours/*")
public class TourAdminServlet extends HttpServlet {

    private TourService tourService;
    private Configuration freemarkerConfig;

    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
        if (tourService == null) {
            throw new ServletException("TourService не найден в ServletContext");
        }
        freemarkerConfig = FreemarkerConfig.getConfig(getServletContext());
    }

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        return currentUser != null
                && currentUser.getRole() != null
                && "ADMIN".equals(currentUser.getRole().name());
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = req.getPathInfo();
        if (path == null || "/".equals(path)) {
            showList(req, resp);
        } else if ("/new".equals(path)) {
            showForm(req, resp, null);
        } else if ("/edit".equals(path)) {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isBlank()) {
                resp.sendRedirect(req.getContextPath() + "/admin/tours");
                return;
            }
            Long id = Long.valueOf(idParam);
            Tour tour = tourService.findById(id).orElse(null);
            showForm(req, resp, tour);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/tours");
        }
    }

    private void showList(HttpServletRequest req,
                          HttpServletResponse resp) throws IOException, ServletException {

        List<Tour> tours = tourService.listAll();

        Map<String, Object> model = new HashMap<>();
        model.put("request", req);
        model.put("tours", tours);

        Template template = freemarkerConfig.getTemplate("admin/tours-list.ftl");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Ошибка обработки шаблона admin/tours-list.ftl", e);
        }
    }

    private void showForm(HttpServletRequest req,
                          HttpServletResponse resp,
                          Tour tour) throws IOException, ServletException {

        Map<String, Object> model = new HashMap<>();
        model.put("request", req);
        model.put("tour", tour);

        Template template = freemarkerConfig.getTemplate("admin/tour-form.ftl");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Ошибка обработки шаблона admin/tour-form.ftl", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp) throws ServletException, IOException {

        if (!isAdmin(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            Long id = Long.valueOf(req.getParameter("id"));
            tourService.delete(id);
            resp.sendRedirect(req.getContextPath() + "/admin/tours");
            return;
        }

        // save (create/update)
        String idParam = req.getParameter("id");
        String title = req.getParameter("title");
        String destination = req.getParameter("destination");
        String priceStr = req.getParameter("price");
        String durationStr = req.getParameter("durationDays");
        String tags = req.getParameter("tags");
        String description = req.getParameter("description");
        String imageUrl = req.getParameter("imageUrl");

        Map<String, Object> model = new HashMap<>();
        model.put("request", req);

        if (title == null || title.isBlank()
                || destination == null || destination.isBlank()
                || priceStr == null || priceStr.isBlank()
                || durationStr == null || durationStr.isBlank()
                || description == null || description.isBlank()) {

            model.put("error", "Заполни все обязательные поля");

            if (idParam != null && !idParam.isBlank()) {
                Long id = Long.valueOf(idParam);
                Tour tour = tourService.findById(id).orElse(null);
                model.put("tour", tour);
            }

            Template template = freemarkerConfig.getTemplate("admin/tour-form.ftl");
            resp.setContentType("text/html;charset=UTF-8");
            try {
                template.process(model, resp.getWriter());
            } catch (TemplateException e) {
                throw new ServletException("Ошибка обработки шаблона admin/tour-form.ftl", e);
            }
            return;
        }

        BigDecimal price = new BigDecimal(priceStr);
        int durationDays = Integer.parseInt(durationStr);

        Tour tour = new Tour();
        if (idParam != null && !idParam.isBlank()) {
            tour.setId(Long.valueOf(idParam));
        }
        tour.setTitle(title);
        tour.setDestination(destination);
        tour.setPrice(price);
        tour.setDurationDays(durationDays);
        tour.setTags(tags);
        tour.setDescription(description);
        tour.setImageUrl(imageUrl);

        tourService.save(tour);

        resp.sendRedirect(req.getContextPath() + "/admin/tours");
    }
}
