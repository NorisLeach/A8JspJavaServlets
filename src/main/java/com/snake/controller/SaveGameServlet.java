package com.snake.controller;

import com.snake.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/saveGame")
public class SaveGameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User loggedInUser = (User) session.getAttribute("currentUser");

        String scoreStr = request.getParameter("score");
        String durationStr = request.getParameter("duration");

        if (scoreStr != null && durationStr != null) {
            try {
                int score = Integer.parseInt(scoreStr);
                int duration = Integer.parseInt(durationStr);

                String dbUrl = "jdbc:mysql://localhost:3306/snake_db?useSSL=false&serverTimezone=UTC";
                String dbUser = "root";
                String dbPassword = "Norisayana1968";

                Class.forName("com.mysql.cj.jdbc.Driver");

                String insertGameQuery = "INSERT INTO games (user_id, score, duration_seconds, is_active) VALUES (?, ?, ?, false)";
                String insertGridQuery = "INSERT INTO grid_state (game_id, x_coord, y_coord, cell_type) VALUES (?, ?, ?, ?)";

                try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                    conn.setAutoCommit(false);

                    int gameId = -1;

                    try (PreparedStatement stmtGame = conn.prepareStatement(insertGameQuery, Statement.RETURN_GENERATED_KEYS)) {
                        stmtGame.setInt(1, loggedInUser.getId());
                        stmtGame.setInt(2, score);
                        stmtGame.setInt(3, duration);
                        stmtGame.executeUpdate();

                        try (ResultSet generatedKeys = stmtGame.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                gameId = generatedKeys.getInt(1);
                            }
                        }
                    }

                    if (gameId != -1) {
                        try (PreparedStatement stmtGrid = conn.prepareStatement(insertGridQuery)) {
                            int[][] obstacleCoords = {
                                    {5*20, 5*20}, {5*20, 6*20}, {5*20, 7*20},
                                    {14*20, 14*20}, {15*20, 14*20}, {16*20, 14*20}
                            };

                            for (int[] coord : obstacleCoords) {
                                stmtGrid.setInt(1, gameId);
                                stmtGrid.setInt(2, coord[0]);
                                stmtGrid.setInt(3, coord[1]);
                                stmtGrid.setString(4, "OBSTACLE");
                                stmtGrid.addBatch();
                            }
                            stmtGrid.executeBatch();
                        }

                        conn.commit();
                        response.setStatus(HttpServletResponse.SC_OK);
                    } else {
                        conn.rollback();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}