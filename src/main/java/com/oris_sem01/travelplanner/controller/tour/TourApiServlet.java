package com.oris_sem01.travelplanner.controller.tour;

import com.google.gson.Gson;
import com.oris_sem01.travelplanner.model.Tour;
import com.oris_sem01.travelplanner.service.TourService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/tours")
public class TourApiServlet extends HttpServlet {

    private TourService tourService;
    private final Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        tourService = (TourService) getServletContext().getAttribute("tourService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Tour> tours = tourService.listAll();
        resp.setContentType("application/json;charset=UTF-8");
        gson.toJson(tours, resp.getWriter());
    }
}
