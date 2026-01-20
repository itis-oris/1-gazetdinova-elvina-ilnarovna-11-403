package com.oris_sem01.travelplanner.controller.booking;

import com.oris_sem01.travelplanner.model.Booking;
import com.oris_sem01.travelplanner.model.User;
import com.oris_sem01.travelplanner.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private BookingService bookingService;

    @Override
    public void init() {
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //первая проверка = аутентификация
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
       //проверяю:есть ли сессия?Есть ли в сессии атрибут "user"? Если нет - редирект на /login, и дальше код не выполняется

        //вторая проверка= валидация параметра (проверка данных перед испоьзованием)
        String tourIdParam = req.getParameter("tourId");
        if (tourIdParam == null || tourIdParam.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/tours");
            return;
        }
        //получаю tourId как строку (в HTML всё строки). Преобразую в Long.
        // Если это не число - выбросится исключение, и пользователя редиректит на /tours.
        Long tourId;
        try {
            tourId = Long.valueOf(tourIdParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/tours");
            return;
        }

        User user = (User) session.getAttribute("user");

        // третья проверка= дубликат брони
        List<Booking> bookings = bookingService.findByUserId(user.getId());
        boolean alreadyBooked = bookings.stream().anyMatch(b -> b.getTourId().equals(tourId));

        if (alreadyBooked) {
            session.setAttribute("bookingError", "❌ Этот тур уже забронирован!");
            resp.sendRedirect(req.getContextPath() + "/tours");
            return;
        }
        //получаю все брони этого пользователя из БД.потом через Stream API проверяю - есть ли уже бронь на этот тур? Если есть - ошибка и редирект.



        // еси все проверки прошли= создаем бронь
        bookingService.create(user.getId(), tourId);
        resp.sendRedirect(req.getContextPath() + "/profile?booking=ok");
    }
}
