package com.example.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldServlet extends HttpServlet {
    
    private String defaultName;
    
    @Override
    public void init() throws ServletException {
        // 從初始化參數中獲取默認名稱
        defaultName = getInitParameter("defaultName");
        if (defaultName == null) {
            defaultName = "World";
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 設置響應內容類型
        response.setContentType("text/html;charset=UTF-8");
        
        // 獲取請求參數
        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = defaultName;
        }
        
        // 輸出 HTML 響應
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello Servlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Hello, " + name + "!</h1>");
        out.println("<p>Today is " + new java.util.Date() + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 簡單起見，POST 請求也由 doGet 處理
        doGet(request, response);
    }
}