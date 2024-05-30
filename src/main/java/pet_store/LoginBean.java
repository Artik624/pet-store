package pet_store;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Managed Bean class for the login page index.xhtml backend
 * @author Artiom Cooper
 */
@SuppressWarnings("deprecation")
@ManagedBean
@RequestScoped 
public class LoginBean {
	private String email;
	private String password;
	private boolean isEmailValid;
	private boolean isPasswordValid;
	private String loginMessage;
	private boolean isLoginValid;
	private SessionManager session;
	private User user;
	
	/**
	 * JSF Constructor , sets variables and checks if user is logged in then redirects to dashboard.
	 */
	@PostConstruct
	public void init() {
		
		session = new SessionManager();
		if(session != null && session.isUserLoggedIn() && ((User)session.getAttribute("user") != null)) {
				FacesContext context = FacesContext.getCurrentInstance();
				ExternalContext externalContext = context.getExternalContext();
				try {
					externalContext.redirect(externalContext.getRequestContextPath() + "/dashboard.xhtml");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		isEmailValid = false;
		isPasswordValid = false;
		isLoginValid = false;
	}
	
	//Getters & Setters
	public boolean getIsLoginValid() {
		return isLoginValid;
	}

	public void setIsLoginValid(boolean isLoginValid) {
		this.isLoginValid = isLoginValid;
	}

	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean getIsEmailValid() {
		return isEmailValid;
	}

	public boolean getIsPasswordValid() {
		return isPasswordValid;
	}
	//End of Getters and Setters 
	
	//Field Validators
	public void validateEmail(FacesContext context, UIComponent comp, Object val) {
		String email = (String)val;
		FacesMessage message;
		
		if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			isEmailValid = false;
			message = new FacesMessage("Invalid Email address");
		}
		
		
		else {
			session.logger.info("Email address:" +email+" PASSED validation");
			isEmailValid = true;
			message = new FacesMessage("");
			this.email = email.toLowerCase();
		}
		context.addMessage(comp.getClientId(context), message);
		
	}
	
	public void validatePassword(FacesContext context, UIComponent comp, Object val) {
		String password = (String)val;
		FacesMessage message;
		
		if(!User.authenticatePassword(password, email)) {
			isPasswordValid = false;
			message = new FacesMessage("Invalid password");
		}
		else {
			isPasswordValid = true;
			message = new FacesMessage("");
		}
		context.addMessage(comp.getClientId(context), message);
	}


	
	public String validateLogin() {
		
		if(User.checkUserEmailExists(email) && User.authenticatePassword(password, email)) {
			setIsLoginValid(true);
			setLoginMessage("");
			session.logger.info("User "+email+" logged in succesfully");
			user = User.getUser(email);
			session.logger.info("User ID: " + user.getId());
			session.logger.info("Setting session attribute user");
			
			session.setAttribute("user", user); 
			session.logger.info("Redirecting "+user.getFirstName()+" to dashboard.xhtml");
			return "dashboard.xhtml?faces-redirect=true";
			
		}
		else {
			setLoginMessage("Invalid Login details");
			setIsLoginValid(false);
			session.logger.warning("Login failed");
		}
		return "";
	}
	
	/**
	 * Backend for the Continue as Guest button , will redirect to a list of pets with limited options.
	 * If the user was logged in will logout and end session
	 * @return The redirect path
	 */
	public String continueAsGuest() {
		try {
			if(session.isUserLoggedIn()) {
				session.invalidateSession();
				return "view_pets.xhtml?faces-redirect=true";
			}
			
		} catch (Exception e) {
			System.err.println("Exception in continueAsGuest() ->" + e);
		}
		return "view_pets.xhtml?faces-redirect=true";
	}
}
