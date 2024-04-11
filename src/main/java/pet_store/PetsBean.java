package pet_store;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class PetsBean {

	private FacesContext facesContext = FacesContext.getCurrentInstance();
	private ExternalContext externalContext = facesContext.getExternalContext();
	private HttpSession session = (HttpSession) externalContext.getSession(false);
	private User user = (User)session.getAttribute("user");
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	private List<Pet> viewPet = null;
	private boolean showPet = false;
	private boolean isUser = false;
	private boolean adoptionRequested = false;
	
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


	public void setUser() {
		this.user = (User)session.getAttribute("user");
	}


	public boolean getIsUser() {
		setIsUser();
		return isUser;
	}


	public void setIsUser() {
		if(getUser() == null) {
			isUser = false;
		}
		else {
			isUser = true;
		}
	}


	
	private String selectCategory ="";
	public String getSelectCategory() {
		return selectCategory;
	}


	public void setSelectCategory(String selectCategory) {
		this.selectCategory = selectCategory;
	}


	public List<Pet> getAllPets(){
		System.out.println("Getting all Pets");
		List<Pet> petsList = dbLink.getPets();
		System.out.println("Pets Length : " + petsList.size());
		return petsList;
	}
	
	
	public void viewPet() {
		if(!showPet) {
			showPet = true;
			Map<String,String> params = externalContext.getRequestParameterMap();
			String id = params.get("id");
			System.out.println("Getting pet by ID: " + id);
			viewPet=  dbLink.getPetByID(id);
		}
		else {
			showPet = false;
		}
		
	}
	
	


	public List<Pet> getViewPet() {
		return viewPet;
	}


	public void setViewPet(List<Pet> viewPet) {
		this.viewPet = viewPet;
	}


	public boolean getShowPet() {
		return showPet;
	}


	public void setShowPet(boolean showPet) {
		this.showPet = showPet;
	}
	
	public void test() {
		System.out.println("requesting adoption : " + adoptionRequested);
		setShowPet(true);
		setAdoptionRequested(true);
		Map<String,String> params = externalContext.getRequestParameterMap();
		String id = params.get("id");
		System.out.println("Getting pet by ID in test: " + id);
		
	}
}
