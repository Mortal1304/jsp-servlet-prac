package com.webapps.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/editurl")
public class EditServlet extends HttpServlet {
    private static final String query = "update BookData set BookName = ?, BookEdition = ?, BookPrice = ? where id = ?";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            pw.println("<h3 style='color:red'>Invalid or missing ID parameter.</h3>");
            return;
        }

        int id = Integer.parseInt(idStr);

        // Get updated form values
        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        float bookPrice = Float.parseFloat(req.getParameter("bookPrice"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            pw.println("<h3 style='color:red'>JDBC Driver not found</h3>");
            cnf.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "root123");
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, bookName);
            ps.setString(2, bookEdition);
            ps.setFloat(3, bookPrice);
            ps.setInt(4, id);

            int count = ps.executeUpdate();

            if (count == 1) {
                pw.println("<h2 style='color:green'>Record updated successfully.</h2>");
            } else {
                pw.println("<h2 style='color:red'>Record update failed.</h2>");
            }

        } catch (SQLException se) {
            se.printStackTrace();
            pw.println("<h3 style='color:red'>Database Error: " + se.getMessage() + "</h3>");
        }

        pw.println("<a href='home.html'>Home</a><br>");
        pw.println("<a href='bookList'>Book List</a>");
    }

    // Optional: You can handle GET to redirect back or show an error
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method required.");
    }
}

