package it.richkmeli.jframework.web.account;

import it.richkmeli.jframework.auth.model.User;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.web.response.KOResponse;
import it.richkmeli.jframework.web.response.OKResponse;
import it.richkmeli.jframework.web.response.StatusCode;
import it.richkmeli.jframework.web.util.ServletException;
import it.richkmeli.jframework.web.util.ServletManager;
import it.richkmeli.jframework.web.util.Session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public abstract class SignUp {

    protected abstract void doSpecificAction();

    public void doAction(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        HttpSession httpSession = request.getSession();
        Session session = null;
        PrintWriter out = response.getWriter();

        try {
            session = ServletManager.getServerSession(request);

            if (session.getUser() == null) {
                //Map<String, String> attribMap = ServletManager.doDefaultProcessRequest(request);
                Map<String, String> attribMap = ServletManager.extractParameters(request);

                String email = attribMap.get("email");
                String pass = attribMap.get("password");
                if (session.getAuthDatabaseManager().isUserPresent(email)) {
                    out.println((new KOResponse(StatusCode.ALREADY_REGISTERED)).json());
                } else {
                    session.getAuthDatabaseManager().addUser(new User(email, pass, false));
                    session.setUser(email);
                    out.println((new OKResponse(StatusCode.SUCCESS)).json());
                    doSpecificAction();
                }
            } else {
                out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
            }
        } catch (ServletException e) {
            out.println((new KOResponse(StatusCode.ALREADY_LOGGED)).json());
        } catch (DatabaseException e) {
            out.println((new KOResponse(StatusCode.DB_ERROR)).json());
        }

    }


}
