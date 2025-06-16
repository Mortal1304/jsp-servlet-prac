package com.webapps.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/editscreen")
public class EditScreenServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
	private static final String query = "Select Id,BookName,BookEdition,BookPrice From BookData where Id = ?";
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter pw = resp.getWriter();

    String idStr = req.getParameter("id");
    if (idStr == null || idStr.trim().isEmpty()) {
        pw.println("<h3 style='color:red'>Invalid or missing ID parameter.</h3>");
        return;
    }

    int id = Integer.parseInt(idStr);

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException cnf) {
        pw.println("<h3 style='color:red'>JDBC Driver not found</h3>");
        cnf.printStackTrace();
        return;
    }

    try (Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "root123");
         PreparedStatement ps = con.prepareStatement(query)) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            pw.println("<form action='editurl?id=" +id+ "' method='post'>");
            pw.println("<table align='center'>");

            pw.println("<tr><td>Book Name</td>");
            pw.println("<td><input type='text' name='bookName' value='" + rs.getString(2) + "'></td></tr>");

            pw.println("<tr><td>Book Edition</td>");
            pw.println("<td><input type='text' name='bookEdition' value='" + rs.getString(3) + "'></td></tr>");

            pw.println("<tr><td>Book Price</td>");
            pw.println("<td><input type='text' name='bookPrice' value='" + rs.getFloat(4) + "'></td></tr>");

            pw.println("<tr><td><input type='submit' value='Submit'></td>");
            pw.println("<td><input type='reset' value='Cancel'></td></tr>");

            pw.println("</table></form>");
        } else {
            pw.println("<h3 style='color:red'>No record found for ID: " + id + "</h3>");
        }
    } catch (SQLException se) {
        se.printStackTrace();
        pw.println("<h3 style='color:red'>Database Error: " + se.getMessage() + "</h3>");
    }

    pw.println("<a href='home.html'>Home</a>");
}
}
