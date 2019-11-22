package it.richkmeli.jframework.web;

import it.richkmeli.jframework.util.Logger;
import it.richkmeli.jframework.web.response.KOResponse;
import it.richkmeli.jframework.web.response.StatusCode;
import it.richkmeli.jframework.web.util.ServletException;
import it.richkmeli.jframework.web.util.ServletManager;
import it.richkmeli.jframework.web.util.Session;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.ResourceBundle;

@WebServlet("/secureConnection")
public abstract class secureConnection extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String encryptionKey;

    public secureConnection() {
        super();
        encryptionKey = ResourceBundle.getBundle("configuration").getString("encryptionkey");

    }

    public abstract void doSpecificActionGet();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        HttpSession httpSession = request.getSession();
        Session session = null;
        try {
            session = ServletManager.getServerSession(httpSession);
        } catch (ServletException e) {
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);

        }

        try {
            PrintWriter out = response.getWriter();

            File secureDataServer = new File("TESTsecureDataServer.txt");
            String serverKey = "testkeyServer";
            // TODO rendere unico il client ID, che sarebbe il codice del RMC, vedi se passarlo come parametro o generato o altro fattore

            if (request.getParameterMap().containsKey("clientID")) {
                String clientID = new String(Base64.getUrlDecoder().decode(request.getParameter("clientID")));

                session.setRmcID(clientID);

                String clientResponse = "";
                if (request.getParameterMap().containsKey("data")) {
                    clientResponse = request.getParameter("data");

                    // TODO metti info in sessione per farla prendere dalle altre servlet, solo questa fa init, altrimenti bisogna mettere il device id davanti in tutte le richieste
                    String serverResponse = session.getCryptoServer().init(secureDataServer, serverKey, clientID, clientResponse);

                    int serverState = new JSONObject(serverResponse).getInt("state");
                    String serverPayload = new JSONObject(serverResponse).getString("payload");

                    doSpecificActionGet();

                    // TODO cambia con OKResponse
                    out.println(serverPayload);
                    out.flush();
                } else {
                    out.println((new KOResponse(StatusCode.SECURE_CONNECTION, "data parameter not present")).json());
                }
            } else {
                out.println((new KOResponse(StatusCode.SECURE_CONNECTION, "clientID parameter not present")).json());
            }
        } catch (Exception e) {
            Logger.error("SERVLET encryptionKey, doGet", e);
            httpSession.setAttribute("error", e);
            request.getRequestDispatcher(ServletManager.ERROR_JSP).forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }


}
