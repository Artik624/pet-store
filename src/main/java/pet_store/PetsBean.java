package pet_store;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
public class PetsBean {

	private FacesContext facesContext = FacesContext.getCurrentInstance();
	private ExternalContext externalContext = facesContext.getExternalContext();
	private HttpSession session = (HttpSession) externalContext.getSession(false);
	private User user = (User)session.getAttribute("user");
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	private List<Pet> viewPet = null;
	
	private boolean showPet = false;
	
	public List<Pet> getAllPets(){
		 
		List<Pet> petsList = dbLink.getPets();
		System.out.println("Pets Length : " + petsList.size());
		return petsList;
	}
	
	
	public void viewPet() {
		System.out.println("view pet");
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
	
}
