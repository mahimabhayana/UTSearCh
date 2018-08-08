package GoogleSignIn;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import Database.SQLiteJDBC;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    // session duration in days
    private static final int SESSION_DURATION = 1;
    // connect to the database
    public static final SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    /**
     * This method intercepts a post request and authenticates a google user
     * @param req the http request
     * @param resp the http response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");

        try {
            String idToken = req.getParameter("id_token");
            GoogleIdToken.Payload payLoad = IdTokenVerifierAndParser.getPayload(idToken);
            // get sub, unique to all users
            String firstName = (String) payLoad.get("given_name");
            String lastName = (String) payLoad.get("family_name");
            String email = payLoad.getEmail();
            String sub = payLoad.getSubject();
            // print user name and email
            System.out.println("User email: " + email);
            System.out.println("subject: " + sub );
            System.out.println("first name: " +firstName);
            System.out.println("last name: " + lastName);
            // get sub, unique to all users
            String message;
            // check if account exists, if not create one
            if(!db.accountExists(sub)) {
                // send a response text indicating account does not exist
                message = "-1";
//                // account does not exists
//                // insert into db
//                db.addAccount(sub, firstName,lastName,email);
//                // create account object
//                users.put(sub, new Account(firstName, lastName, email, sub));
//                // print out all accounts
            } else {
                // if session exists for user, delete it
                // get current date time
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiryDate = now.plusDays(SESSION_DURATION);
                // send expiry date with email to create session
                String userToken = tokenGenerator();
                db.createSession(sub, expiryDate.toString().substring(0, expiryDate.toString().indexOf('.')), userToken);
                //db.sessionValid(sub,  now.toString().substring(0, now.toString().indexOf('.')));

                // response text
                message = userToken;
            }

            resp.setContentLength(message.length());
            PrintWriter out = resp.getWriter();
            out.println(message);
            out.close();
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * A method to generate a unique alphanumeric string
     * @return a unique String
     */
    public static String tokenGenerator() {
        String token = UUID.randomUUID().toString();
        return token.replaceAll("-", "");
    }
}

