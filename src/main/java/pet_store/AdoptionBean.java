package pet_store;


import java.io.IOException;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;

import javax.annotation.PostConstruct;

import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
//import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;


/**
 * Managed Bean class for the JSF FW. Manages the request_adoption.xhtml backend
 * @author Artiom Cooper
 */
@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class AdoptionBean {
	private FacesContext facesContext;
	private ExternalContext externalContext;
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	private SessionManager session;
	private String petID;
	private String adoptionMessage ;
	private String adoptionRequestResponse = "";
	private User user;
	private Pet pet;
	private User owner;
	

	/**
	 * Constructor for the Bean, instantiates variables and checks if a session and a user is logged in.
	 * If no user is found, redirects the user to the main page.
	 */
	@PostConstruct
	public void init() {
		adoptionMessage = "Send message to owner";
		session = new SessionManager();
		facesContext = session.getFacesContext();
		externalContext = session.getExternalContext();
		if(session != null) {
			System.out.println("Session detected ");
			user = (User)session.getAttribute("user");
			petID = (String)session.getAttribute("petId");
			if(user != null) {
				System.out.println("User detected: " +user.getFirstName()+", "+user.getLastName());
				getPetDetails(petID);
				getOwnerDetails(petID);
				
			}
			else {
				System.out.println("User is null in dashbnoard");
				try {
					externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
					
				} catch (Exception e) {
					System.out.println("Exception in Dashboard init()-> " + e);
				}			
			}
		}
		else {
			System.out.println("No SESSION FOUND");			
		}
		petID = (String)session.getAttribute("petId");
		user = (User)session.getAttribute("user");
		getPetDetails(petID);
		getOwnerDetails(petID);
	}
	
	//Getters & Setters
	public String getAdoptionRequestResponse() {
		return adoptionRequestResponse;
	}
	
	public void setAdoptionRequestResponse(String adoptionRequestResponse) {
		
		this.adoptionRequestResponse = adoptionRequestResponse;
	}
	
	public String getAdoptionMessage() {
		return adoptionMessage;
	}
	
	public void setAdoptionMessage(String adoptionMessage) {
		this.adoptionMessage = adoptionMessage;
	}


	public String getPetID() {
		return petID;
	}

	public void setPetID(String petID) {
		this.petID = petID;
	}

	public Pet getPet() {
		return pet;
	}
	
	
	public User getOwner() {
		return owner;
	}
	//End of Getters & Setters
	
	/**
	 * Retrieve pet details from DB by pet Id and set as class variable.
	 * @param petId -- Pet id to get from DB
	 */
	private void getPetDetails(String petId) {
		try {
			pet = dbLink.getPetByID(petId);
		} catch (NullPointerException e) {
			System.out.println("Exception in AdoptionBean.getPetDetails() -> " + e);
		}
	}
	
	/**
	 * Retrieve user details by pet ID and set as class variable.
	 * @param petId -- the pet Id for which to get the user data
	 */
	private void getOwnerDetails(String petId) {
		try {
			owner = (dbLink.getOwnerByPetID(petId));
			
		} catch (NullPointerException e) {
			System.out.println("Exception in AdoptionBean.getOwnerDetails()-> " + e);
		}
	}
	
	/**
	 * Send adoption request message to DB
	 * Will check message content and if adoption request already exists.
	 * And sets a message indicating the result of the action.
	 */
	public void sendAdoptionMessage() {
		if (!adoptionMessage.isBlank() && !adoptionMessage.equals("Send message to owner") && !user.checkIfAdoptionReqeuested(petID)) {
			try {
				if(dbLink.sendAdoptionRequestToDB(petID, user.getId(), owner.getId(), adoptionMessage))
					adoptionRequestResponse="Message Sent to owner";
				else
					adoptionRequestResponse="Error: failed to send message to owner";
				
			} catch (Exception e) {
				System.out.println("Exception in sendAdoptionMessage() : -> " + e );
				adoptionRequestResponse="ERROR: Message was not sent ";
			}
		}
		else if(user.checkIfAdoptionReqeuested(petID)) {
			adoptionRequestResponse="ERROR: You already sent an adoption request for this pet ";
		}
		else {
			adoptionRequestResponse="ERROR: Must send a message";
		}
		
	}
	
	/**
	 * Helper method to redirect the user to main page index.xhtml
	 */
	public void redirectToIndex() {
		try {
        	facesContext = FacesContext.getCurrentInstance();
    		externalContext = facesContext.getExternalContext();
			externalContext.redirect(externalContext.getRequestContextPath() + "/index.xhtml");
		} catch (IOException e) {
			
			System.out.println("Exception in redirectToIndex()-> " + e);
		}
	}

}
