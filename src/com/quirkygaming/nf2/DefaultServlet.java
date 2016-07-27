package com.quirkygaming.nf2;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class DefaultServlet {
	
	/**
	 * Place this line at the beginning of your doGet/doPost methods to serve /public/ content universally:
	 * 		if (DefaultServlet.serveDefaultContent(this, request, response)) return;
	 * 
	 * 
	 * @param host The servlet meaning to forward the request
	 * @param request
	 * @param response
	 * @return Whether the content qualified as default
	 * @throws ServletException
	 * @throws IOException
	 */
	public static boolean forwardDefaultContent(HttpServlet host, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getRequestURI().contains("/public/")) {
			RequestDispatcher rd = host.getServletContext().getNamedDispatcher("default");
			
			HttpServletRequest wrapped = new HttpServletRequestWrapper(request) {
				public String getServletPath() {
					return "";
				}
				
				public String getPathInfo() {
					return super.getPathInfo().substring(super.getPathInfo().indexOf("/public"));
				}
				
				public String getRequestURI() {
					return this.getContextPath() + getPathInfo();
				}
			};
			
			rd.forward(wrapped, response);
			return true;
		}
		return false;
	}
}
