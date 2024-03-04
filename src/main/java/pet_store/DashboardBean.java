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
	private HttpSession session = (HttpSession) externalContext.getSession(false);
	private User user = (User)session.getAttribute("user");
	
	
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
	private List<Pet> petsList = (user != null ? user.getPetsList() : null);
	
	private int MIN_NAME_LENGTH = 2;
	private int MAX_NAME_LENGTH = 10;
	private int MIN_AGE = 0;
	private int MAX_AGE = 100;
	private int MIN_SIZE = 0;
	private int MAX_SIZE = 100;
	
	
	
	
	private List<String> petCategories;
	private List<String> gendersList;
	private List<Integer> sizeList;
	private boolean showAddPet = false;
	
	
	public DashboardBean() {
		petCategories = new ArrayList<>();
		gendersList = new ArrayList<>();
		gendersList.add("male");
		gendersList.add("female");
		sizeList = new ArrayList<>();
		for(int i = MIN_SIZE; i < MAX_SIZE; i++) {
			sizeList.add(i);
		}
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

	
	
	public String getFirstName() {
		if(isSessionActive()) {
			return user.getFirstName();
		}
		return "";
	}
	
	public boolean isShowAddPet() {
		return showAddPet;
	}

	public void setShowAddPet(boolean showAddPet) {
		this.showAddPet = showAddPet;
	}

	public void addPet() {
        setShowAddPet(true);
    }
	
	public List<String> getCategoriesOptions(){
		if(isSessionActive()) {
			petCategories = new ArrayList<String>(user.getPetCategories());
			return petCategories;
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
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        try {
            externalContext.redirect(externalContext.getRequestContextPath() + "/dashboard.xhtml");
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception
        }
	}
	
	
	private boolean isSessionActive() {
		if(user == null) {
			System.out.println("user is null");
			try {
				externalContext.redirect("index.xhtml?faces-redirect=true");
				return false;
			} catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		return true;
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
	
	
	
}

