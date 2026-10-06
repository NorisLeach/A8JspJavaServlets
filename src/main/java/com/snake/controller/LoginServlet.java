package com.snake.controller;

import com.snake.dao.UserDAO;
import com.snake.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userParam = request.getParameter("username");
        String passParam = request.getParameter("password");

        User user = userDAO.validateLogin(userParam, passParam);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            response.sendRedirect("game");
        } else {
            request.setAttribute("errorMessage", "Username sau parolă incorectă!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

}
