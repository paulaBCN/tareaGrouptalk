package eetac.upc.eetac.dsa.grouptalk;

import eetac.upc.eetac.dsa.grouptalk.dao.AuthTokenDAO;
import eetac.upc.eetac.dsa.grouptalk.dao.AuthTokenDAOImpl;
import eetac.upc.eetac.dsa.grouptalk.dao.UserDAO;
import eetac.upc.eetac.dsa.grouptalk.dao.UserDAOImpl;
import eetac.upc.eetac.dsa.grouptalk.entity.AuthToken;
import eetac.upc.eetac.dsa.grouptalk.entity.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.sql.SQLException;

/**
 * Created by pauli on 12/04/2016.
 */
@Path("login")
public class LoginResource {
    @Context
    final
    ThreadLocal<SecurityContext> securityContext = new ThreadLocal<>();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(GrouptalkMediaType.GROUPTALK_AUTH_TOKEN)
    public AuthToken login(@FormParam("login") String loginid, @FormParam("password") String password) {
        if(loginid == null || password == null)
            throw new BadRequestException("all parameters are mandatory");

        User user = null;
        AuthToken authToken = null;
        try{
            UserDAO userDAO = new UserDAOImpl();
            user = userDAO.obtener_UserByLoginid(loginid);
            if(user == null)
                throw new BadRequestException("loginid " + loginid + " not found.");
            if(!userDAO.check_Password(user.getId(), password))
                throw new BadRequestException("incorrect password");

            AuthTokenDAO authTokenDAO = new AuthTokenDAOImpl();
            authTokenDAO.deleteToken(user.getId());
            authToken = authTokenDAO.createAuthToken(user.getId());
        }catch(SQLException e){
            throw new InternalServerErrorException();
        }
}
