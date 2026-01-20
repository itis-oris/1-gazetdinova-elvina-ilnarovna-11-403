package com.oris_sem01.travelplanner.controller.review;

import com.oris_sem01.travelplanner.model.User;
import com.oris_sem01.travelplanner.service.ReviewService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        reviewService = (ReviewService) getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Long tourId = Long.parseLong(req.getParameter("tourId"));
        int rating = Integer.parseInt(req.getParameter("rating"));
        String comment = req.getParameter("comment");

        try {
            reviewService.addReview(user.getId(), tourId, rating, comment);
            resp.sendRedirect(req.getContextPath() + "/tours");
        } catch (IllegalArgumentException e) {
            session.setAttribute("reviewError", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/tours");
        }
    }
}
