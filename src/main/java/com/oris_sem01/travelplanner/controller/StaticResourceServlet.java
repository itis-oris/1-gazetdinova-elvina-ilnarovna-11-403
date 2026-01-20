package com.oris_sem01.travelplanner.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/static/*")
public class StaticResourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo(); // /css/style.css

        if (pathInfo == null || pathInfo.isEmpty()) {
            resp.sendError(404);
            return;
        }

        String realPath = getServletContext().getRealPath("/");
        File file = new File(realPath, "static" + pathInfo);

        if (!file.exists() || file.isDirectory()) {
            System.out.println("❌ Файл не найден: " + file.getAbsolutePath());
            resp.sendError(404);
            return;
        }

        if (pathInfo.endsWith(".css")) {
            resp.setContentType("text/css;charset=UTF-8");
        } else if (pathInfo.endsWith(".js")) {
            resp.setContentType("application/javascript;charset=UTF-8");
        } else if (pathInfo.endsWith(".png")) {
            resp.setContentType("image/png");
        } else if (pathInfo.endsWith(".jpg") || pathInfo.endsWith(".jpeg")) {
            resp.setContentType("image/jpeg");
        } else if (pathInfo.endsWith(".gif")) {
            resp.setContentType("image/gif");
        }

        byte[] fileContent = Files.readAllBytes(file.toPath());
        resp.setContentLength(fileContent.length);
        resp.getOutputStream().write(fileContent);
        System.out.println("✅ Файл отправлен: " + file.getAbsolutePath());
    }
}
