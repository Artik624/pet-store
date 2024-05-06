package pet_store;

import java.io.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@ManagedBean
@SessionScoped
public class DashboardBean {

	
private FacesContext facesContext = FacesContext.getCurrentInstance();
private ExternalContext externalContext = facesContext.getExternalContext();
//	private HttpSession session = (HttpSession) externalContext.getSession(true);
	private User user;
	
	
	private String petName;
	private String petCategory;
	private String petGender;
	private int petAge;
	private int petWeight;
	private int petHeight;
	private int petLength;
	private String shortDescription;
	private String fullDescription;
	private Part petPhoto;
	private List<Pet> petsList;
	
	private int MIN_NAME_LENGTH = 2;
	private int MAX_NAME_LENGTH = 10;
	private int MIN_AGE = 0;
	private int MAX_AGE = 100;
	private int MIN_SIZE = 0;
	private int MAX_SIZE = 100;
	
	
	
	
	private List<AdoptionRequestWrapper> adoptionRequests;
	private List<String> petCategories;
	private List<String> gendersList;
	private List<Integer> sizeList;
	private boolean showAddPet;
	private boolean showViewRequests;
	private boolean showViewSentRequests;
	
	
	
	public boolean isShowViewSentRequests() {
		return showViewSentRequests;
	}

	public void setShowViewSentRequests(boolean showViewSentRequests) {
		this.showViewSentRequests = showViewSentRequests;
	}

	public boolean isShowViewRequests() {
		return showViewRequests;
	}

	public void setShowViewRequests(boolean showViewRequests) {
		this.showViewRequests = showViewRequests;
	}

	public void initDashboard() {
		petCategories = new ArrayList<>();
		gendersList = new ArrayList<>();
		gendersList.add("male");
		gendersList.add("female");
		sizeList = new ArrayList<>();
		
		for(int i = MIN_SIZE; i < MAX_SIZE; i++) {
			sizeList.add(i);
		}
		
		try {
			this.user = (User)SessionManager.getAttribute("user");
			System.out.println("User in dashboard : " + user.getFirstName());
		} catch (Exception e) {
			System.out.println("DashboardBean User Exception -> " + e);
		}
		petsList = (user != null ? user.getPetsList() : null);
	}
	
	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getPetCategory() {
		return petCategory;
	}

	public void setPetCategory(String petCategory) {
		this.petCategory = petCategory;
	}

	public String getPetGender() {
		return petGender;
	}

	public void setPetGender(String petGender) {
		this.petGender = petGender;
	}

	public int getPetAge() {
		return petAge;
	}

	public void setPetAge(int petAge) {
		this.petAge = petAge;
	}

	public int getPetWeight() {
		return petWeight;
	}

	public void setPetWeight(int petWeight) {
		this.petWeight = petWeight;
	}

	public int getPetHeight() {
		return petHeight;
	}

	public void setPetHeight(int petHeight) {
		this.petHeight = petHeight;
	}

	public int getPetLength() {
		return petLength;
	}

	public void setPetLength(int petLength) {
		this.petLength = petLength;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public Part getPetPhoto() {
		return petPhoto;
	}

	public void setPetPhoto(Part petPhoto) {
		this.petPhoto = petPhoto;
	}

	public void createSession() {
		try {
			if(user != null) {
				System.out.println("User found , setting session attribute to true");
				SessionManager.setAttribute(SessionManager.getUserLoggedInAttr(), true);
			}
			else {
				System.out.println("User not found, setting session attribute to false");
				SessionManager.setAttribute(SessionManager.getUserLoggedInAttr(), false);
			}
			
		} catch (Exception e) {
			System.out.println("DashboardBean createSession -> " + e);
		}
	}
	
	
	public String getFirstName() {
		createSession();
		if((boolean)SessionManager.getAttribute("userLoggedIn")) {
			return user.getFirstName();
		}
		return "";
	}
	
	public boolean isShowAddPet() {
		System.out.println("showAddPet : " + showAddPet);
		return showAddPet;
	}

	public void setShowAddPet(boolean showAddPet) {
		System.out.println("setting showAddPet to: " + showAddPet);
		this.showAddPet = showAddPet;
	}

	public void addPet() {
		if(isShowAddPet()) {
			setShowAddPet(false);
		}
		else {
			setShowAddPet(true);
			if(isShowViewRequests())
				setShowViewRequests(false);
			if(isShowViewSentRequests())
				setShowViewSentRequests(false);
		}
    }
	
	public void viewRequests() {
		if(isShowViewRequests()) {
			setShowViewRequests(false);
		}
		else {
			setShowViewRequests(true);
			if(isShowViewSentRequests())
				setShowViewSentRequests(false);
			if(isShowAddPet())
				setShowAddPet(false);
			
		}
	}
	
	public void viewSentRequests() {
		if(isShowViewSentRequests()) {
			setShowViewSentRequests(false);
		}
		else {
			setShowViewSentRequests(true);
			if(isShowAddPet())
				setShowAddPet(false);
			if(isShowViewRequests())
				setShowViewRequests(false);
			
		}
	}
	
	public List<String> getCategoriesOptions(){
		if((boolean)SessionManager.getAttribute("userLoggedIn")) {
			petCategories = new ArrayList<String>(user.getPetCategories());
			return petCategories;
		}
		else {
			return null;
		}
	}
	
	public List<AdoptionRequestWrapper> getAdoptionRequests(){
		if((boolean)SessionManager.getAttribute("userLoggedIn")) {
			adoptionRequests = new ArrayList<AdoptionRequestWrapper>(user.getAdoptionRequests());
			if(adoptionRequests.size() > 0) {
			System.out.println("test getAdoptionRequests() -> " + adoptionRequests.get(0).getPetName() );
			return adoptionRequests;
			}
			System.out.println("getAdoptionRequests() return NULL ");
			return null;
		}
		else {
			return null;
		}
	}
	
	public List<AdoptionRequestWrapper> getSentRequests(){
		if((boolean)SessionManager.getAttribute("userLoggedIn")) {
			adoptionRequests = new ArrayList<AdoptionRequestWrapper>(user.getMyAdoptionRequests());
			if(adoptionRequests.size() > 0) {
			System.out.println("test getSentRequests() -> " + adoptionRequests.get(0).getPetName() );
			return adoptionRequests;
			}
			System.out.println("getSentRequests() return NULL ");
			return null;
		}
		else {
			return null;
		}
	}
	
	public List<String> getGendersOptions(){
		return gendersList;
	}
	
	public List<Integer> getSizeOptions(){
		return sizeList;
	}
	
	public void addNewPet() {
		System.out.println("test");
//		System.out.println("Name: " + getPetName() +
//				"\nGender: " + getPetGender()+
//				"\nCategory: " + getPetCategory()+
//				"\nage: "+getPetAge()+
//				"\nweight: "+getPetWeight()+
//				"\nheight: "+getPetHeight()+
//				"\nShort desc : " + getShortDescription()+
//				"\nFull desc : " + getFullDescription()+
//				"\nphoto Path : " + uploadPhoto());
		
		String photoPath = uploadPhoto();
		System.out.println("Photo path : "  + photoPath);
		if(getPetName() != null && getShortDescription() != null && getFullDescription() != null) {
			Pet newPet = new Pet(user.getId(), 
					getPetCategory(), 
					getPetName(), 
					getPetGender(), 
					getPetAge(), 
					getPetWeight(), 
					getPetHeight(), 
					getPetLength(), 
					getShortDescription(), 
					getFullDescription(), 
					photoPath);
			
			newPet.sendToDb();
			
		}
		
		setShortDescription("");
		setFullDescription("");
		setShowAddPet(false);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/dashboard.xhtml");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
	}
	
	
//	private boolean isSessionActive() {
//		if(user == null) {
//			System.out.println("user is null");
//			try {
//				
//				externalContext.redirect("index.xhtml?faces-redirect=true");
//				return false;
//			} catch (IOException e) {
//				System.out.println(e);
//				e.printStackTrace();
//			}
//		}
//		return true;
//	}
	
	public boolean getTest() {
		System.out.println("Checking user : " + (user != null));
		if (user != null) {
			return true;
		}
		return false;
	}
	
	public void petNameValidator(FacesContext context, UIComponent comp, Object val) {
		String petName = (String)val;
		FacesMessage message;
		
		if(!petName.matches("^[a-zA-Z]+")) {
			message = new FacesMessage("Name can only contain letters");
		}
		else if(petName.length() > MAX_NAME_LENGTH || petName.length() < MIN_NAME_LENGTH) {
			
			message = new FacesMessage(String.format("Name cannot be longer than %d and shorter than %d Character", MAX_NAME_LENGTH, MIN_NAME_LENGTH));
		}
		else{
			
			message = new FacesMessage("OK");
			this.petName = petName;
		}
		context.addMessage(comp.getClientId(context), message);
	}
	
	public void petAgeValidator(FacesContext context, UIComponent comp, Object val) {
		String petAge = (String)val;
		FacesMessage message;
		
		if(!petAge.matches("^[0-9]+")) {
			message = new FacesMessage("Age can only contain numbers");
		}
		else {
			int age = Integer.parseInt(petAge);
			if(age > MAX_AGE || age < MIN_AGE) {
				
				message = new FacesMessage(String.format("Age cannot be more than %d and less than %d Character", MAX_AGE, MIN_AGE));
			}
			else{
				
				message = new FacesMessage("OK");
				this.petAge = age;
			}
			
		}
			
		context.addMessage(comp.getClientId(context), message);
	}
	
	public String uploadPhoto() {
		String userId = user.getId();
		String userResourcePath = DbManager.IMGS_FULL_PATH.resolve(userId).toString();
		String petPhotoRelativePath = userId + "\\" + (petName+".jpg");
		String petPhotoFullPath = DbManager.IMGS_FULL_PATH.resolve(userId).resolve(petName+".jpg").toString();
		File dir = new File(userResourcePath);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		System.out.println("uploading file : " + petPhotoFullPath);
        try (InputStream input = petPhoto.getInputStream();
             OutputStream output = new FileOutputStream(petPhotoFullPath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return petPhotoRelativePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
	
	public List<Pet> getPetsList(){
		petsList = (user != null ? user.getPetsList() : null);
		return petsList;
	}
	
	public int getNumberOfPets() {
		return petsList.size();
	}
	
	
	public void removePet() {
		Map<String,String> params = externalContext.getRequestParameterMap();
		String petName = params.get("petName");
		
		Pet.removePet(petName, user.getId());
	}
	
	public String logout() {
		
        SessionManager.invalidateSession();
		return "/index.xhtml?faces-redirect=true";
	}
	
	
}

