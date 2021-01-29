package com.tetraquark.UserManagement.web;

import com.tetraquark.UserManagement.bean.User;
import com.tetraquark.UserManagement.dao.UserDao;

import javax.jws.soap.SOAPBinding;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.swing.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserServlet", value = "/")
public class UserServlet extends HttpServlet {
    UserDao userDao;
    public UserServlet() {
    }

    public void init(ServletConfig config) throws ServletException{
        userDao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action){
            case "/new":
                showNewForm(request, response);
                break;

            case "/insert":
                insertNewUser(request, response);
                break;

            case "/delete":
                deleteUser(request, response);
                break;

            case "/edit":
                showEditForm(request, response);
                break;

            case "/update":
                updateUser(request, response);
                break;

            default:
                showUserList(request, response);
                break;
        }

    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(request, response);
    }

    //inset user
    private void insertNewUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userDao.insertUsers(new User(request.getParameter("name"), request.getParameter("email"),
                request.getParameter("country")));
        response.sendRedirect("list");
    }

    // Delete user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            userDao.deleteUser(Integer.parseInt(request.getParameter("id")));
        } catch (Exception e){
            e.printStackTrace();
        }
        response.sendRedirect("list");
    }

    // Show edit user form
    private void showEditForm(HttpServletRequest request, HttpServletResponse response){
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            User user = userDao.selectUserByID(id);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
            request.setAttribute("user", user);
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Edit user
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userDao.updateUser(new User(Integer.parseInt(request.getParameter("id")),
                                    request.getParameter("name"),
                                    request.getParameter("email"),
                                    request.getParameter("country")));

        response.sendRedirect("list");
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response){
        try {
            List<User> users = userDao.selectAllUsers();
            request.setAttribute("users", users);
            RequestDispatcher dispatcher = request.getRequestDispatcher("user-list.jsp");
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
