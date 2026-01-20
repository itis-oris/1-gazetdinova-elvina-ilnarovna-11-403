package com.oris_sem01.travelplanner.web;

import com.oris_sem01.travelplanner.config.FreemarkerConfig;
import com.oris_sem01.travelplanner.repository.jdbc.*;
import com.oris_sem01.travelplanner.service.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // Инициализируем Freemarker
        FreemarkerConfig.getConfig(ctx);

        // 2 создание Репозиторий объектов
        JdbcUserRepository userRepo = new JdbcUserRepository();
        JdbcTourRepository tourRepo = new JdbcTourRepository();
        JdbcBookingRepository bookingRepo = new JdbcBookingRepository();
        JdbcReviewRepository reviewRepo = new JdbcReviewRepository();
        JdbcScheduleRepository scheduleRepo = new JdbcScheduleRepository();

        // 3 создание сервис объектов
        UserService userService = new UserService(userRepo);
        TourService tourService = new TourService(tourRepo);
        BookingService bookingService = new BookingService(bookingRepo);
        ReviewService reviewService = new ReviewService(reviewRepo);
        ScheduleService scheduleService = new ScheduleService(scheduleRepo);

        // 4 сохранение в сервлетконтекст
        ctx.setAttribute("userService", userService);
        ctx.setAttribute("tourService", tourService);
        ctx.setAttribute("bookingService", bookingService);
        ctx.setAttribute("reviewService", reviewService);
        ctx.setAttribute("scheduleService", scheduleService);
    }
    //теперь в любом Servlet'е я могу написать:
    //UserService service = (UserService) getServletContext()
    //   .getAttribute("userService");
    // я получаю тот же самый объект, который был создан при инициализации.
    // синглтон = объект существует только один на всё приложение


    // при отстановке томката
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // можно закрыть пул
        // ConnectionPool.closePool();
    }
}
