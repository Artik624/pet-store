package pet_store;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 * Managed Bean class for view-pets.xhtml backend
 * @author Artiom Cooper
 */
@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class PetsBean {//

	private SessionManager session;
	private FacesContext facesContext;
	private ExternalContext externalContext;
	private User user = null;
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	private Pet viewedPet;
	private boolean showPet;
	private boolean isUser;
	private boolean adoptionRequested;
	private String message ="";
	private String selectCategory =" ";
	private String selectGender =" ";
	private String selectAgeCategory =" ";
	
	/**
	 * JSF constructor, checks if user logged in 
	 */
	@PostConstruct
	private void init() {
		setMessage("");
		facesContext = FacesContext.getCurrentInstance();
		externalContext = facesContext.getExternalContext();
		session = new SessionManager();
		user = (User)session.getAttribute("user");
		if(user == null) {
			isUser = false;
		}
		else if(user != null && (boolean)session.isUserLoggedIn()) {
			isUser = true;
		}
	}
	
	//Getters & Setters
	public Pet getViewedPet() {
		return viewedPet;
	}
	
	public void setViewedPet(Pet pet) {
		this.viewedPet = pet;
	}
	
	public String getSelectAgeCategory() {
		return selectAgeCategory;
	}

	public void setSelectAgeCategory(String selectAgeCategory) {
		this.selectAgeCategory = selectAgeCategory;
	}

	public String getSelectGender() {
		return selectGender;
	}

	public void setSelectGender(String selectGender) {
		this.selectGender = selectGender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean getAdoptionRequested() {
		return adoptionRequested;
	}

	public void setAdoptionRequested(boolean adoptionRequested) {
		this.adoptionRequested = adoptionRequested;
	}


	public User getUser() {
		return user;
	}

	public void setShowPet() {
		if(showPet)
			showPet = false;
		else
			showPet = true;
	}

	public boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(boolean isSession) {
			isUser = isSession;
	}
	
	public String getSelectCategory() {
		return selectCategory;
	}

	public void setSelectCategory(String selectCategory) {
		System.out.println("setting category : " + selectCategory);
		this.selectCategory = selectCategory;
	}
	
	public boolean getShowPet() {
		return showPet;
	}

	public void setShowPet(boolean showPet) {
		this.showPet = showPet;
	}

	/**
	 * Retrieves all pets from DB
	 * @return list of pets or null
	 */
	public List<Pet> getAllPets(){
		List<Pet> petsList;
		if(user == null)
			petsList = dbLink.getPets("");
		else
			petsList = dbLink.getPets(user.getId());
		return petsList;
	}
	
	/**
	 * Manages the data for the detailed view of a pet, gets all the data from DB for a pet
	 */
	public void viewPet() {
		setMessage("");
		showPet = true;
		Map<String,String> params = externalContext.getRequestParameterMap();
		String id = params.get("id");
		System.out.println(" ID in viewPet : " + id);
		viewedPet = dbLink.getPetByID(id);
	}
	
	/**
	 * Redirect the user to the request_adoption.xhtml page and sets a session attribute.
	 * Sets message if adoption request already exists
	 * @return path for redirection
	 */
	public String requestAdoption() {
		System.out.println("RequestAdoption() for : " +viewedPet.getName());
		if(!user.checkIfAdoptionReqeuested(viewedPet.getId())){
			setMessage("");
			System.out.println("In req adoption func pet : " + viewedPet.getId());
			session.setAttribute("petId", viewedPet.getId());
			return "request_adoption.xhtml?faces-redirect=true";
		}
		else {
			setMessage("You already requested adoption for this pet");
			return "";
		}
	}
	
	/**
	 * Retrieves all pet categories from DB
	 * @return categories list
	 */
	public List<String> getPetCategories(){
		List<String> categories = dbLink.getCategories();
		System.out.println(Arrays.toString(categories.toArray()));
		return categories;
	}
	

}
