package com.webapps.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String query = "INSERT INTO BookData(BookName, BookEdition, BookPrice) VALUES (?, ?, ?)";
	private Object href;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter pw = resp.getWriter();

		// Get form parameters
		String BookName = req.getParameter("bookName");
		String BookEdition = req.getParameter("bookEdition");
		String priceParam = req.getParameter("bookPrice");

//		// Validate price input
//		if (priceParam == null || priceParam.trim().isEmpty()) {
//			pw.println("<h3 style='color:red'>Book Price is required</h3>");
//			return;
//		}
//
		float bookPrice;
		try {
			bookPrice = Float.parseFloat(priceParam);
		} catch (NumberFormatException nfe) {
			pw.println("<h3 style='color:red'>Invalid Book Price format</h3>");
			return;
		}

		// Load JDBC Driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException cnf) {
			pw.println("<h3 style='color:red'>JDBC Driver not found</h3>");
			cnf.printStackTrace();
			return;
		}

		// Insert into DB
		try (Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "root123");
		     PreparedStatement ps = con.prepareStatement(query)) {

			ps.setString(1, BookName);
			ps.setString(2, BookEdition);
			ps.setFloat(3, bookPrice);

			int count = ps.executeUpdate();
			if (count == 1) {
				pw.println("<h2 style='color:green'>Record Registered Successfully</h2>");
			} else {
				pw.println("<h2 style='color:red'>Record Not Registered</h2>");
			}

		} catch (SQLException se) {
			se.printStackTrace();
			pw.println("<h3 style='color:red'>Database Error: " + se.getMessage() + "</h3>");
		}
		pw.println("<a href = 'home.html'>Home</a>");
		pw.println("<br>");
		pw.println("<a href = 'bookList' >BookList</a>");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}

