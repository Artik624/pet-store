package pet_store;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.servlet.http.HttpSession;

@SuppressWarnings("deprecation")
@ManagedBean
@ViewScoped
public class PetsBean {

	private FacesContext facesContext = FacesContext.getCurrentInstance();
	private ExternalContext externalContext = facesContext.getExternalContext();
	//private HttpSession session = (HttpSession) externalContext.getSession(false);
	private User user = null;
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	//private List<Pet> viewPet = null;
	private Pet pet = null;
	private boolean showPet = false;
	private boolean isUser = false;
	private boolean adoptionRequested = false;
	private String message ="";
	
	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	@PostConstruct
	private void init() {
		user = (User)SessionManager.getAttribute("user");
	}
	
	
	public boolean getAdoptionRequested() {
		System.out.println("getting aR : " +adoptionRequested);
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


	
	private String selectCategory =" ";
	public String getSelectCategory() {
		return selectCategory;
	}


	public void setSelectCategory(String selectCategory) {
		this.selectCategory = selectCategory;
	}


	public List<Pet> getAllPets(){
		System.out.println("Getting all Pets");
		List<Pet> petsList;
		if(user == null)
			petsList = dbLink.getPets("");
		else
			petsList = dbLink.getPets(user.getId());
		System.out.println("Pets Length : " + petsList.size());
		return petsList;
	}
	
	
	public void viewPet() {
		if(!showPet) {
			showPet = true;
			Map<String,String> params = externalContext.getRequestParameterMap();
			String id = params.get("id");
			System.out.println("Getting pet by ID: " + id);
			this.pet = dbLink.getPetByID(id);
		}
		
		
	}
	
	


//	public List<Pet> getViewPet() {
//		return viewPet;
//	}
//
//
//	public void setViewPet(List<Pet> viewPet) {
//		this.viewPet = viewPet;
//	}


	public boolean getShowPet() {
		return showPet;
	}


	public void setShowPet(boolean showPet) {
		this.showPet = showPet;
	}
	
	public String requestAdoption() {
		if(!user.checkIfAdoptionReqeuested(pet.getId())){
			System.out.println("In req adoption func pet : " + pet.getId());
			SessionManager.setAttribute("petId", pet.getId());
			return "request_adoption.xhtml?faces-redirect=true";
		}
		else {
			setMessage("You already requested adoption for this pet");
			return "";
		}
	}
	
	
	public List<String> getPetCategories(){
		System.out.println("getting categories in petsbean");
		List<String> categories = dbLink.getCategories();
		System.out.println(Arrays.toString(categories.toArray()));
		return categories;

	}
	
	public void checkSession() {
		try {
			boolean test = SessionManager.checkSessionExists();
			if(!test)
				SessionManager.createSession(user);
			System.out.println("PetsBean Check Session value: " + test);
			setIsUser(test);
		} catch (Exception e) {
			System.out.println("PetsBean Check Session not found");
			setIsUser(false);
		}
	}
}
