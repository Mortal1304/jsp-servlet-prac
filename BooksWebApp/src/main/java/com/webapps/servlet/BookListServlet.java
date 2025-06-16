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

@WebServlet("/bookList")
public class BookListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String query = "Select Id,BookName,BookEdition,BookPrice From BookData";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();



		// Load JDBC Driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException cnf) {
			pw.println("<h3 style='color:red'>JDBC Driver not found</h3>");
			cnf.printStackTrace();
			return;
		}

		// Generate the connection
		try (Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "root123");
		     PreparedStatement ps = con.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			pw.println("<table border = '1' align = 'center'>");
			pw.println("<tr>");
			pw.println("<th> Book Id </th>");
			pw.println("<th> Book Name </th>");
			pw.println("<th> Book Edition </th>");
			pw.println("<th> Book Price </th>");
			
			pw.println("<th>Edit</h>");
			pw.println("<th>Delete</h>");
			
			pw.println("</tr>");
			
			while(rs.next()) {
				pw.println("<tr>");
				pw.println("<td>"+rs.getInt(1)+ "</td>");
				pw.println("<td>"+rs.getString(2)+ "</td>");
				pw.println("<td>"+rs.getString(3)+ "</td>");
				pw.println("<td>"+rs.getFloat(4)+ "</td>");
				
				pw.println("<td><a href = 'editscreen?id="+ rs.getInt(1)+"'>Edit</a></td>");
				pw.println("<td><a href = 'deleteurl?id="+ rs.getInt(1)+"'>Delete</a></td>");
				
				pw.println("</tr>");
				
			}
			pw.println("</table>");
			
		} catch (SQLException se) {
			se.printStackTrace();
			pw.println("<h3 style='color:red'>Database Error: " + se.getMessage() + "</h3>");
		}
		pw.println("<a href = 'home.html'>Home</a>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
