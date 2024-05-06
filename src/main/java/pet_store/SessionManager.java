package pet_store;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessionManager {
    private static FacesContext facesContext = FacesContext.getCurrentInstance();
    private static ExternalContext externalContext = facesContext.getExternalContext();
    private static HttpSession session = (HttpSession) externalContext.getSession(false);
    private static final String USER_LOGGED_IN_ATTR = "userLoggedIn";

    public static String getUserLoggedInAttr() {
		return USER_LOGGED_IN_ATTR;
	}

	public static void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
    }

    public static Object getAttribute(String name) {
        return session.getAttribute(name);
    }

    public static void removeAttribute(String name) {
        session.removeAttribute(name);
    }

    public static void invalidateSession() {
    	try {
    		session.setAttribute("user", null);
    		session.setAttribute(USER_LOGGED_IN_ATTR, false);
    		System.out.println("logged out user");
			
		} catch (IllegalStateException e) {
			System.out.println(e);
		}
    }
    
    public static boolean checkSessionExists() throws IllegalStateException, Exception{
    	System.out.println("test sttribute : " + session.getAttribute(USER_LOGGED_IN_ATTR));
    	if((boolean)session.getAttribute(USER_LOGGED_IN_ATTR))
    		return true;
    	return false;
    }
    
	public static void createSession(User user) {
		try {
			if(user != null) {
				System.out.println("User found , setting session attribute to true");
				SessionManager.setAttribute(SessionManager.getUserLoggedInAttr(), true);
			}
			else {
				System.out.println("User not found, setting session attribute to false");
				SessionManager.setAttribute(SessionManager.getUserLoggedInAttr(), false);
			}
			
		} catch (Exception e) {
			System.out.println("SessionManager createSession -> " + e);
		}
	}
}

