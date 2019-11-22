package it.richkmeli.jframework.web.account;

import it.richkmeli.jframework.web.response.KOResponse;
import it.richkmeli.jframework.web.response.OKResponse;
import it.richkmeli.jframework.web.response.StatusCode;
import it.richkmeli.jframework.web.util.ServletException;
import it.richkmeli.jframework.web.util.ServletManager;
import it.richkmeli.jframework.web.util.Session;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet({"/LogOut"})
public class LogOut extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogOut() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);

            if (session != null) {
                // remove user from the session
                session.removeUser();
            }
            httpSession.invalidate();

            out.println((new OKResponse(StatusCode.SUCCESS)).json());

        } catch (ServletException e) {
            out.println((new KOResponse(StatusCode.GENERIC_ERROR, e.getMessage())).json());
        }

        out.flush();
        out.close();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        this.doGet(request, response);
    }
}
