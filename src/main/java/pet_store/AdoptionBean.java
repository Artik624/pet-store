package pet_store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@RequestScoped
public class AdoptionBean {
	private FacesContext facesContext = FacesContext.getCurrentInstance();
	private ExternalContext externalContext = facesContext.getExternalContext();
	private HttpSession session = (HttpSession) externalContext.getSession(false);
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	
	private String petID;
	private boolean showPetPanel = false;
	private List<User> owner = null;
	private List<Pet> viewPet = null;
	
	
	public boolean getShowPetPanel() {
		System.out.println("getting show pet : " + showPetPanel);
		return showPetPanel;
	}


	public void setShowPetPanel(boolean showPet) {
		this.showPetPanel = showPet;
	}

	public String getPetID() {
		return petID;
	}

	public void setPetID(String petID) {
		this.petID = petID;
	}

	
	public void viewOwner() {
		System.out.println("Get owner");
		Map<String,String> params = externalContext.getRequestParameterMap();
		String id = params.get("id");
		System.out.println(" petId in viewOwner " + id);
		try {
			owner = new ArrayList<User>();
			owner.add(dbLink.getOwnerByPetID(id));
			System.out.println("name : " + owner.get(0).getFirstName());
			
		} catch (NullPointerException e) {
			System.out.println(e +" at vewOwner at AdoptionBean ");
		}
		
		
		
	}
	
	public List<User> getOwner() {
		return owner;
	}

	public void setOwner(List<User> owner) {
		this.owner = owner;
	}

	public List<Pet> getViewPet() {
		System.out.println("getting pet in adpotion : " + viewPet.toString());
		return viewPet;
	}
	
	public Pet seePet() {
		return getViewPet().get(0);
	}

	public void setViewPet(List<Pet> pet) {
		this.viewPet = pet;
	}

	public void viewPetDetails() {
		System.out.println("Get Pet");
		Map<String,String> params = externalContext.getRequestParameterMap();
		String id = params.get("id");
		System.out.println(" petId2 in viewPet " + id);
		try {
			setViewPet(dbLink.getPetByID(id));
			System.out.println("pet name : " + viewPet.get(0).getName() );
			setShowPetPanel(true);
			
		} catch (NullPointerException e) {
			setShowPetPanel(false);
			System.out.println(e + " in AdoptionBean.viewPet");
		}
		
	}
}
