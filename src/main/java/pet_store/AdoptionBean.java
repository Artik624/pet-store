package pet_store;

import java.awt.event.ActionEvent;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
//import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;



@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class AdoptionBean {
	//private FacesContext facesContext = FacesContext.getCurrentInstance();
	//private ExternalContext externalContext = facesContext.getExternalContext();
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	
	private String petID;
	private String adoptionMessage = "Send message to owner";
	private String adoptionRequestResponse = "";
	private User user;
	
	public String getAdoptionRequestResponse() {
		return adoptionRequestResponse;
	}

	public void setAdoptionRequestResponse(String adoptionRequestResponse) {
		this.adoptionRequestResponse = adoptionRequestResponse;
	}

	private Pet pet;
	private User owner;
	
	@PostConstruct
	public void init() {
		petID = (String)SessionManager.getAttribute("petId");
		user = (User)SessionManager.getAttribute("user");
		getPetDetails(petID);
		getOwnerDetails(petID);
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
	
	private void getPetDetails(String petId) {
		System.out.println("Geting Pet Details");
		try {
			pet = dbLink.getPetByID(petId);
			System.out.println("pet name : " + pet.getName());
		} catch (NullPointerException e) {
			System.out.println(e + " in AdoptionBean.viewPet");
		}
		
	}
	
	private void getOwnerDetails(String petId) {
		System.out.println("Geting Owner Details");
		try {
			owner = (dbLink.getOwnerByPetID(petId));
			System.out.println("name : " + owner.getFirstName());
			
		} catch (NullPointerException e) {
			System.out.println(e +" at vewOwner at AdoptionBean ");
		}
		
	}
	
	public String sendAdoptionMessage() {
		System.out.println("adoptionMessage :" + adoptionMessage);
		if (!adoptionMessage.isBlank() && !adoptionMessage.equals("Send message to owner")) {
			System.out.println("in condition");
			try {
				System.out.println("pet_id : " + pet.getId());
				System.out.println("requester_id : " + user.getId());
				System.out.println("owner_id : " + owner.getId());
				System.out.println("message : " + adoptionMessage);
				adoptionRequestResponse="Message Sent to owner";
			    dbLink.sendAdoptionRequestToDB(petID, user.getId(), owner.getId(), adoptionMessage);
				return "";
				//return "view_pets.xhtml?faces-redirect=true";
				
			} catch (Exception e) {
				System.out.println("Exception in sendAdoptionMessage() : -> " + e );
				adoptionRequestResponse="ERROR: Message waas not sent ";
				return "";
				
			}
		}else {
			adoptionRequestResponse="ERROR: Must send a message";
			return "";
			
		}
		
	}

}
