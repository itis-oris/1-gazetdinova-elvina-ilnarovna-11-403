package com.oris_sem01.travelplanner.controller;

import com.oris_sem01.travelplanner.config.FreemarkerConfig;
import com.oris_sem01.travelplanner.model.Booking;
import com.oris_sem01.travelplanner.model.Review;
import com.oris_sem01.travelplanner.model.Tour;
import com.oris_sem01.travelplanner.model.User;
import com.oris_sem01.travelplanner.service.BookingService;
import com.oris_sem01.travelplanner.service.ReviewService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    private Configuration freemarkerConfig;
    private BookingService bookingService;
    private TourService tourService;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        freemarkerConfig = FreemarkerConfig.getConfig(getServletContext());
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
        tourService = (TourService) getServletContext().getAttribute("tourService");
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Получаем бронирования
        List<Booking> bookings = bookingService != null ?
                bookingService.findByUserId(user.getId()) : new ArrayList<>();

        // Обогащаем бронирования турами и отзывами
        List<Map<String, Object>> enrichedBookings = new ArrayList<>();

        if (bookings != null && !bookings.isEmpty()) {
            for (Booking b : bookings) {
                Map<String, Object> bookingData = new HashMap<>();
                bookingData.put("booking", b);

                // Получаем тур
                var tourOpt = tourService != null ? tourService.findById(b.getTourId()) : java.util.Optional.empty();
                Tour tour = tourOpt.isPresent() ? (Tour) tourOpt.get() : null;
                bookingData.put("tour", tour);

                // Получаем отзывы для этого тура
                List<Review> reviews = new ArrayList<>();
                if (tour != null && reviewService != null) {
                    var reviewList = reviewService.findByTourId(tour.getId());
                    if (reviewList != null) {
                        reviews = reviewList;
                    }
                }
                bookingData.put("reviews", reviews);

                enrichedBookings.add(bookingData);
            }
        }

        System.out.println("🎫 ProfileServlet:");
        System.out.println("   User: " + user.getId());
        System.out.println("   Bookings: " + bookings.size());
        System.out.println("   Enriched: " + enrichedBookings.size());

        Map<String, Object> model = new HashMap<>();
        model.put("user", user);
        model.put("bookings", enrichedBookings);
        model.put("request", req);

        if ("ok".equals(req.getParameter("booking"))) {
            model.put("success", "Тур успешно забронирован! ✨");
        }

        Template template = freemarkerConfig.getTemplate("profile.ftl");
        resp.setContentType("text/html;charset=UTF-8");
        try {
            template.process(model, resp.getWriter());
        } catch (TemplateException e) {
            throw new ServletException("Ошибка обработки шаблона profile.ftl", e);
        }
    }
}
