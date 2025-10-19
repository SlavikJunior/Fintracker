package com.slavikjunior.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(urlPatterns = {"/test", "/test/"})
public class TestMethodsServletRemoveLater extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(req.getPathInfo());
        System.out.println(req.getPathTranslated());
        System.out.println(req.getServletPath());
        System.out.println(req.getContextPath());

        //        req.getParameterMap().forEach((key, value) -> {
//            System.out.print("key: " + key + ", value: " + Arrays.toString(value));
//        });
//        resp.setContentType("text/html");
//        resp.getWriter().write("""
//                <html>
//                <head>
//                <title>test</title>
//                </head>
//                <body>
//                <p>
//                one: %s
//                two: %s
//                </p>
//                <form action='/test' method='post'>
//                <input type='text' name='one'>
//                <input type='text' name='two'>
//                <input type='submit' name='submit'>
//                </form>
//                </body>
//                </html>
//                """.formatted(req.getParameter("one"), req.getParameter("two")));

        //        https://playground.learnqa.ru/demo/xss?q=%3Cscript%3Ealert(document.cookie)%3C/script%3E
    }
}
