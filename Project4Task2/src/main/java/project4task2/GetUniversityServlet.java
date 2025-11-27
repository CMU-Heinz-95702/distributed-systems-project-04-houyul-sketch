package project4task2;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * This servlet receives requests from the index page and the dashboard page.
 * It validates user input, calls the model to perform API lookup,
 * loads MongoDB logs, and forwards data to JSP pages.
 *
 * @author Houyu Lin
 * @andrewID houyul
 * @email houyul@andrew.cmu.edu
 */
@WebServlet(name = "GetUniversityServlet",
        urlPatterns = {"/GetUniversity", "/getDashBoard", "/api/university"})
public class GetUniversityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        String nextPage = "";

        if (path.equals("/GetUniversity")) {

            String country = req.getParameter("country");

            if (country == null || country.trim().isEmpty()) {
                req.setAttribute("error", "Country input cannot be empty.");
                nextPage = "index.jsp";
            } else {

                JSONObject info = GetUniversityModel.query(country.trim());

                if (info == null) {
                    req.setAttribute("error", "No university data found.");
                    nextPage = "index.jsp";
                } else {
                    req.setAttribute("result", info);
                    nextPage = "result.jsp";
                }
            }
        }

        else if (path.equals("/getDashBoard")) {

            GetUniversityModel.loadDashboard();

            req.setAttribute("rawLogs", GetUniversityModel.allLogs);
            req.setAttribute("popularCountry", GetUniversityModel.popularCountry);
            req.setAttribute("totalRequests", GetUniversityModel.totalRequests);
            req.setAttribute("avgResults", GetUniversityModel.avgResults);

            nextPage = "dashboard.jsp";
        }

        else if (path.equals("/api/university")) {

            String country = req.getParameter("country");

            resp.setContentType("application/json; charset=UTF-8");

            if (country == null || country.trim().isEmpty()) {
                resp.getWriter().write("{\"error\":\"Country is required\"}");
                return;
            }

            JSONObject info = GetUniversityModel.query(country.trim());

            if (info == null) {
                resp.getWriter().write("{\"error\":\"No university found\"}");
            } else {
                resp.getWriter().write(info.toJSONString());
            }

            return;
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher(nextPage);
        dispatcher.forward(req, resp);
    }
}
