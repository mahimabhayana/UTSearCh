package GoogleSignIn;

import Database.SQLiteJDBC;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    public static final SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");

        // get email, account type, gmail, name, id_token
        String uoftEmail = req.getParameter("email");
        String accountType = req.getParameter("type");

        try {
            String idToken = req.getParameter("id_token");
            GoogleIdToken.Payload payLoad = IdTokenVerifierAndParser.getPayload(idToken);
            // get sub, unique to all users
            String firstName = (String) payLoad.get("given_name");
            String lastName = (String) payLoad.get("family_name");
            String gmail = payLoad.getEmail();
            String sub = payLoad.getSubject();

            String message = "";
            // check if email already in use
            if(db.accountExistsGmail(gmail)) {
                // -1 is gmail is in use
                message = "-1";
            }else if (db.accountExistsUtormail(uoftEmail)) {
                // -2 is uoft mail is in use
                message = "-2";

            }else  {
                // email not in use
                // create validation token
                String token = LoginServlet.tokenGenerator();
                db.createValidationEntry(sub,token);
                // send email
                SendMail.send(uoftEmail, firstName, token);
                // create an account
                db.addAccount(sub, firstName, lastName, gmail, uoftEmail, accountType, 0);


                // send '0' as response to indicate ok
                message = "0";
                // send an email to uoft email

            }
            resp.setContentLength(message.length());
            PrintWriter out = resp.getWriter();
            out.println(message);
            out.close();
            out.flush();



        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
