package pet_store;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class RegisterBean {

	private final int MAX_NAME_LENGTH = 10;
	private final int MIN_NAME_LENGTH = 2;
	private final int MAX_PASS_LENGTH = 12;
	private final int MIN_PASS_LENGTH = 6;
	private final String VALID_CHARS = "!@#$%&";
	
	
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private int phone;
	private String city;
	private String street;
	private int streetNumber;
	
	private boolean isFirstNameValid;
	private boolean isLastNameValid;
	private boolean isPasswordValid;
	private boolean isPhoneValid;
	private boolean isEmailValid;
	private boolean isCityValid;
	private boolean isStreetValid;
	private boolean isStreetNumberValid;
	
	private String registerMessage = "xxxxx";
	
	public String getRegisterMessage() {
		return registerMessage;
	}

	public void setRegisterMessage(String registerMessage) {
		this.registerMessage = registerMessage;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassord() {
		return password;
	}
	public void setPassord(String passord) {
		this.password = passord;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getStreetNumber() {
		return streetNumber;
	}
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
	
	public boolean getIsFirstNameValid() {
		return isFirstNameValid;
	}

	public boolean getIsLastNameValid() {
		return isLastNameValid;
	}
	public boolean getIsPasswordValid() {
		return isPasswordValid;
	}
	public boolean getIsPhoneValid() {
		return isPhoneValid;
	}
	public boolean getIsEmailValid() {
		return isEmailValid;
	}
	public boolean getIsCityValid() {
		return isCityValid;
	}
	public boolean getIsStreetValid() {
		return isStreetValid;
	}
	public boolean getIsStreetNumberValid() {
		return isStreetNumberValid;
	}
	
	public boolean test = true;
	
	public void validateFirstName(FacesContext context, UIComponent comp, Object val)  {
		String firstName = (String)val;
		FacesMessage message;

		if(!firstName.matches("^[a-zA-Z]+")) {
			isFirstNameValid = false;
			message = new FacesMessage("First Name can only contain letters");
		}
		else if(firstName.length() > MAX_NAME_LENGTH || firstName.length() < MIN_NAME_LENGTH) {
			isFirstNameValid = false;
			message = new FacesMessage(String.format("Name cannot be shorter than %d and longer than %d Character", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
		}
		else{
			isFirstNameValid = true;
			message = new FacesMessage("OK");
			this.firstName = firstName;
		}
		context.addMessage(comp.getClientId(context), message);
	}
	
	
	public void validateLastName(FacesContext context, UIComponent comp, Object val) {
		String lastName = (String)val;
		FacesMessage message;
		
		if(!lastName.matches("^[a-zA-Z]+")) {
			isLastNameValid = false;
			message = new FacesMessage("Last Name can only contain letters");
		}
		else if(lastName.length() > MAX_NAME_LENGTH || lastName.length() < 2) {
			isLastNameValid = false;
			message = new FacesMessage(String.format("Name cannot be longer than %d and shorter than %d Character", MIN_NAME_LENGTH, MAX_NAME_LENGTH));
		}
		else{
			isLastNameValid = true;
			message = new FacesMessage("OK");
			this.lastName = lastName;
		}
		context.addMessage(comp.getClientId(context), message);
	}
	
	public void validatePassword(FacesContext context, UIComponent comp, Object val) {
		String password = (String)val;
		FacesMessage message;

		if(!password.matches("^(?=.*?[A-Za-z])(?=.*?[0-9])(?=.*?[!@#$%&]).*$")) {
			isPasswordValid = false;
			message = new FacesMessage(String.format("Password must contain letters, and at least 1 number and a special character such as %s", VALID_CHARS));
			
		}
		
		else if(password.length() > MAX_PASS_LENGTH || password.length()  < MIN_PASS_LENGTH) {
			isPasswordValid = false;
			message = new FacesMessage(String.format("Password cannot be longer than %d and shorter than %d characters", MAX_PASS_LENGTH, MIN_PASS_LENGTH));
		}
		else{
			isPasswordValid = true;
			this.password = password;
			message = new FacesMessage("Good");
			this.password = password;
			
		}
		context.addMessage(comp.getClientId(context), message);
		
	}
	
	public void validateEmail(FacesContext context, UIComponent comp, Object val) {
		String email = (String)val;
		FacesMessage message;
		if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			isEmailValid = false;
			message = new FacesMessage("Invalid Email address");
		}
		else  {
			if(!User.checkUserEmailExists(email)) {
				isEmailValid = true;
				message = new FacesMessage("OK");
				this.email = email;
				
			}
			else {
				isEmailValid = false;
				message = new FacesMessage("This email is already registered");
			}
		}
		context.addMessage(comp.getClientId(context), message);
	}
	
	public void validatePhone(FacesContext context, UIComponent comp, Object val) {
		String phone = (String)val;
		FacesMessage message;
		if(!phone.matches("^\\d{10}$")) {
			isPhoneValid = false;
			message = new FacesMessage("Invalid phone number, must contain up to 10 numbers");
		}
		else {
			isPhoneValid = true;
			message = new FacesMessage("OK");
			this.phone = Integer.parseInt(phone);
		}
		context.addMessage(comp.getClientId(context), message);
	}
	
	
	public void validateCity(FacesContext context, UIComponent comp, Object val) {
		String city = (String)val;
		FacesMessage message;
		if(!city.matches("^[a-zA-Z]+$")) {
			isCityValid = false;
			message = new FacesMessage("Invalid city, must contain only letters");
		}
		else {
			isCityValid = true;
			message = new FacesMessage("OK");
			this.city = city;
		}
		context.addMessage(comp.getClientId(context), message);
	}
	
	public void validateStreet(FacesContext context, UIComponent comp, Object val) {
		String street = (String)val;
		FacesMessage message;
		if(!street.matches("^[a-zA-Z]+$")) {
			isStreetValid = false;
			message = new FacesMessage("Invalid Street, must contain only letters");
		}
		else {
			isStreetValid = true;
			message = new FacesMessage("OK");
			this.street = street;
		}
		context.addMessage(comp.getClientId(context), message);
	
	}
	
	public void validateStreetNumber(FacesContext context, UIComponent comp, Object val) {
		String streetNum = (String)val;
		FacesMessage message;
		if(!streetNum.matches("^[0-9]+$")) {
			isStreetNumberValid = false;
			message = new FacesMessage("Invalid street Number, must contain only numbers");
		}
		else {
			isStreetNumberValid = true;
			message = new FacesMessage("OK");
			this.streetNumber = Integer.parseInt(streetNum);
		}
		context.addMessage(comp.getClientId(context), message);
	
	}

	
	public String createUser() {
		if(isFirstNameValid && isLastNameValid && isPasswordValid && isEmailValid && isPhoneValid && isCityValid && isStreetValid & isStreetNumberValid) {
			System.out.println("Registering User");
			
			User user = new User(firstName, lastName, firstName, email, phone, city, street, streetNumber); 
			if (user.sendToDB()) {
				setRegisterMessage("Success, User Registered, you will be redirected to main page for log in ");
				
				return "index.xhtml?faces-redirect=true";
			}
			else {
				setRegisterMessage("Error");
				return "index.xhtml?faces-redirect=true";
			}
				
			
			
		}
		return null;
	}


}

