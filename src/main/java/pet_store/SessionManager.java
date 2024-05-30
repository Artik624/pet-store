package pet_store;
import java.io.IOException;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * SessionManager --- Responsible for handling various user sessions connecting to the web app.
 * The underlying session is provided by the JSF and Apache Tomcat frameworks
 * @author Artiom Cooper
 */
public class SessionManager {
	private static final String USER_LOGGED_IN_ATTR = "userLoggedIn";//Session Attribute, used as flag to check if user logged in.
    private FacesContext facesContext;
    private ExternalContext externalContext;
    private HttpSession session;
    protected Logger logger;
    
    /**
     * Constructor for SessionMnager, instantiates some session values as required by JSF 
     */
    public SessionManager() {
    	logger = Logger.getLogger(SessionManager.class.getName());
    	facesContext = FacesContext.getCurrentInstance();
    	externalContext = facesContext.getExternalContext();
    	session = (HttpSession) externalContext.getSession(false);
    }
    
    //Getters & Setters
    public FacesContext getFacesContext() {
		return facesContext;
	}


	public void setFacesContext(FacesContext facesContext) {
		this.facesContext = facesContext;
	}


	public ExternalContext getExternalContext() {
		return externalContext;
	}


	public void setExternalContext(ExternalContext externalContext) {
		this.externalContext = externalContext;
	}


	public HttpSession getSession() {
		return session;
	}


	public void setSession(HttpSession session) {
		this.session = session;
	}

    public static String getUserLoggedInAttr() {
		return USER_LOGGED_IN_ATTR;
	}
    
    //End Getters & Setters
    
    /**
     * Check for the value of session attribute "USER_LOGGED_IN_ATTR"
     * @return False is attribute is false or null, otherwise true.
     */
    public boolean isUserLoggedIn() {
    	if(getAttribute(USER_LOGGED_IN_ATTR) == null) {
    		return false;
    	}
    	else {
    		return (boolean)getAttribute(USER_LOGGED_IN_ATTR);
    	}
    }

    /**
     * Helper Method to set a session attribute
     * @param name The name of the attribute
     * @param value The value of the attribute
     */
	public void setAttribute(String name, Object value) {
		if(value instanceof User)  {
			User u = (User)value;
			logger.info("Setting Session attribute-> "+name+" : "+u.getFirstName()+", " + u.getLastName());
		}
		else if(value instanceof Boolean) {
			logger.info("Setting Session attribute-> "+name+" : "+(boolean)value);
		}
		else {
			logger.info("Setting Session attribute-> "+name);
		}
        session.setAttribute(name, value);
    }

	/**
	 * Helper method to get the value of a session attribute
	 * @param name The name of the Attribute
	 * @return The value of the attribute
	 */
    public Object getAttribute(String name) {
    	if(session.getAttribute(name) instanceof User) {
    		User u = (User)session.getAttribute(name);//for logging purposes checks if the attribute is of certain type
    		logger.info("Retrieveing Session attribute:"+name+": "+u.getFirstName()+", " + u.getLastName());
    	}
		else {
			logger.info("Retrieveing Session attribute-> "+name+" : "+session.getAttribute(name));
		}
        return session.getAttribute(name);
    }

    /**
     * Removes an attribute from a session
     * @param name The name of the attribute to remove
     */
    public void removeAttribute(String name) {
    	logger.info("Removing Session attribute:"+name);
        session.removeAttribute(name);
    }

    /**
     * Method invalidates the session and redirects the user to the main page
     */
    public void invalidateSession() {
    	try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            

            if (session != null) {
                session.setAttribute("user", null);
                session.setAttribute(USER_LOGGED_IN_ATTR, false);
                session.invalidate();
                logger.info("Invalidating Session -> Setting attribute \"user\" to NULL, and \"userLoggedIn\" attribute to FALSE");
                
                // Redirect to index page
                externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
            }
        } catch (Exception e) {
            logger.severe("Exception in SessionManager.invalidateSession() -> " + e);
        }
    }
    
    /**
     * Sets the user_logged_in attrbute to true if a user object is found.
     * @param user The user instance if successful login
     */
	public void createSession(User user) {
		try {
			if(user != null) {
				logger.info("User found , setting session attribute \"userLoggedIn\" to TRUE");
				setAttribute(getUserLoggedInAttr(), true);
			}
			else {
				logger.info("User not found , setting session attribute \"userLoggedIn\" to false");
				setAttribute(getUserLoggedInAttr(), false);
			}
			
		} catch (Exception e) {
			logger.severe("Exception in SessionManager.createSession()-> "+e);
		}
	}
	
	/**
	 * Helper method used to redirect user to main page
	 */
	public void redirectToIndex() {
		try {
        	
			externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
		} catch (IOException e) {
			System.out.println("Exception in redirectToIndex()-> " + e);
		}
	}


}

