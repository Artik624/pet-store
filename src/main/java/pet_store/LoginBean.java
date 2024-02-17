package pet_store;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpSession;


@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class LoginBean {
	private String email;
	private String password;
	private boolean isEmailValid = false;
	private boolean isPasswordValid = false;
	private String loginMessage;
	private boolean isLoginValid = false;
	
	private User user;
	
	
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

	public LoginBean(){}

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
	
	public void validateEmail(FacesContext context, UIComponent comp, Object val) {
		String email = (String)val;
		FacesMessage message;
		
		if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			isEmailValid = false;
			message = new FacesMessage("Invalid Email address");
		}
		
		
		else {
			isEmailValid = true;
			message = new FacesMessage("OK");
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
			message = new FacesMessage("OK");
		}
		context.addMessage(comp.getClientId(context), message);
	}

	public boolean getIsEmailValid() {
		return isEmailValid;
	}

	public boolean getIsPasswordValid() {
		return isPasswordValid;
	}
	
	public String validateLogin() {

		if(User.checkUserEmailExists(email) && User.authenticatePassword(password, email)) {
			setIsLoginValid(true);
			//setLoginMessage("Success");
			System.out.println("Login successful");
			user = User.getUser(email);
			System.out.println("Test get user : id : "+ user.getId());
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			HttpSession session = (HttpSession) externalContext.getSession(false);
			session.setAttribute("user", user);
			return "dashboard.xhtml?faces-redirect=true";
			
		}
		else {
			setLoginMessage("Invalid Login details");
			setIsLoginValid(false);
			System.out.println("Login failed");
			user = null;
			return "";
		}
		
	}
	
	
	
}
