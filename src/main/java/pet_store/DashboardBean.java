package pet_store;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 * Managed Bean class for the dashboard.xhtml backend
 * @author Artiom Cooper
 */
@SuppressWarnings("deprecation")
@ManagedBean
@SessionScoped
public class DashboardBean {

	
	private FacesContext facesContext;
	private ExternalContext externalContext;
	private User user;
	private String userName;

	private String petName;
	private String petCategory;
	private String petGender;
	private String newCategory;
	

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
	private int MIN_SIZE = 0;
	private int MAX_SIZE = 100;
	
	
	
	private SessionManager session;
	private List<AdoptionRequestWrapper> adoptionRequests;
	private List<String> petCategories;
	private List<String> gendersList;
	private List<Integer> sizeList;
	private boolean showAddPet;
	private boolean showViewRequests;
	private boolean showViewSentRequests;
	private boolean showAddCategory;
	private String message;
	

	/**
	 * Acts as constructor for the JSF FW. Sets up variables and checks a user is logged in, otherwise redirects to main page
	 */
	@PostConstruct
	public void init() {
		message = "";
		newCategory = "";
		showAddPet = false;
		showViewRequests = false;
		showViewSentRequests = false;
		showAddCategory = false;
		petCategories = new ArrayList<>();
		gendersList = new ArrayList<>();
		gendersList.add("male");
		gendersList.add("female");
		sizeList = new ArrayList<>();
		session = new SessionManager();
		facesContext = session.getFacesContext();
		externalContext = session.getExternalContext();
		
		if(session != null) {
			System.out.println("Session detected in dashboard");
			user = (User)session.getAttribute("user");
			if(user != null) {
				System.out.println("User detected in dashboard: " +user.getFirstName()+", "+user.getLastName());
				session.createSession(user);
				userName = ""+user.getFirstName()+" "+ user.getLastName();
				for(int i = MIN_SIZE; i < MAX_SIZE; i++) {
					sizeList.add(i);
				}
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
	}
	
	@PreDestroy
	public void destory() {
		user = null;
		session = null;
	}
	
	//Getters & Setters
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNewCategory() {
		return newCategory;
	}
	
	public void setNewCategory(String newCategory) {
		this.newCategory = newCategory;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isShowAddCategory() {
		return showAddCategory;
	}
	
	public void setShowAddCategory(boolean showAddCategory) {
		this.showAddCategory = showAddCategory;
	}
	
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
	
	public List<Pet> getPetsList(){
		petsList = (user != null ? user.getPetsList() : null);
		return petsList;
	}
	
	public int getNumberOfPets() {
		return petsList.size();
	}
	
	public boolean isShowAddPet() {
		return showAddPet;
	}

	public void setShowAddPet(boolean showAddPet) {
		this.showAddPet = showAddPet;
	}
	
	public List<String> getGendersOptions(){
		return gendersList;
	}
	
	public List<Integer> getSizeOptions(){
		return sizeList;
	}
	
	//End of Getters & Setters
	
	
	/**
	 * Manages the buttons text values when Add Category Button is clicked
	 */
	public void addCategory() {
		if(isShowAddCategory())
			setShowAddCategory(false);
		else
			setShowAddCategory(true);
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
	
	/**
	 * Manages the buttons text values when Received Requests Button is clicked
	 */
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
	
	/**
	 * Manages the buttons text values when Sent Requests Button is clicked
	 */
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
	
	/**
	 * Retrieve the pet categories from DB
	 * @return list of categories
	 */
	public List<String> getCategoriesOptions(){
		if((boolean)session.getAttribute("userLoggedIn")) {
			petCategories = new ArrayList<String>(user.getPetCategories());
			return petCategories;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Retrieves the received adoption requests from DB
	 * @return List of requests or null
	 */
	public List<AdoptionRequestWrapper> getAdoptionRequests(){
		if((boolean)session.getAttribute("userLoggedIn")) {
			adoptionRequests = new ArrayList<AdoptionRequestWrapper>(user.getAdoptionRequests());
			if(adoptionRequests.size() > 0) {
				return adoptionRequests;
			}
			System.out.println("DashboardBean.getAdoptionRequests() returns NULL ");
			return null;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Retrieves adoption requests sent by User
	 * @return list of requests or null
	 */
	public List<AdoptionRequestWrapper> getSentRequests(){
		if((boolean)session.getAttribute("userLoggedIn")) {
			adoptionRequests = new ArrayList<AdoptionRequestWrapper>(user.getMyAdoptionRequests());
			if(adoptionRequests.size() > 0) {
				return adoptionRequests;
			}
			System.out.println("DashboardBean.getSentRequests() return NULL ");
			return null;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Adds new pet to DB with values entered by user 
	 */
	public void addNewPet() {
		String photoPath = uploadPhoto();
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
            System.out.println("Exception in DashboardBean.addNewPet() -> " + e);
        }
	}
	

	/**
	 * Validator To check that the pet name is valid and unique for the user.
	 * Sets an appropriate message
	 * @param context
	 * @param comp
	 * @param val
	 */
	public void petNameValidator(FacesContext context, UIComponent comp, Object val) {
		String petName = (String)val;
		FacesMessage message;
		List<Pet> myPets = user.getPetsList();
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
		
		for(Pet pet : myPets) {
			if(petName.equals(pet.getName())) {
				message = new FacesMessage("You already have a pet with that name");
			}
		}
		
		context.addMessage(comp.getClientId(context), message);
	}
	
	/**
	 * uploads a photo of the pet to a specific path 
	 * @return photo path or empty string
	 */
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
            System.out.println("Exception in DashboardBean.uploadPhoto()-> " + e);
            return "";
        }
    }
	
	/**
	 * Removes a pet from DB
	 */
	public void removePet() {
		Map<String,String> params = externalContext.getRequestParameterMap();
		String petId = params.get("petId");
		Pet.removePet(petId, user.getId());
	}

	/**
	 * Adds a new category to DB , checks that input is valid and sets appropriate message
	 */
	public void submitCategory() {
		if(!newCategory.isBlank() && !newCategory.isEmpty() && newCategory.matches("[a-zA-Z]+")) {
			if(user.addNewCategory(newCategory)) {
				setMessage("Category added : " + newCategory);
				setNewCategory("");
				
			}
			else {
				setMessage("Failed to add category : " + newCategory);
				setNewCategory("");
			}
			
		}
		else {
			setMessage("New category cant be empty");
			setNewCategory("");
		}
	}
	
	/**
	 * Helper method to logout user and invalidate session
	 */
	public void logout() {
		session.invalidateSession();
	}
	
	/**
	 * Removes an adoption request sent by the logged in user
	 */
	public void cancelSentAdoptionRequest() {
		Map<String,String> params = externalContext.getRequestParameterMap();
		String id = params.get("id");
		user.removeSentRequest(Pet.getPetById(id));
	}
	
	
}

