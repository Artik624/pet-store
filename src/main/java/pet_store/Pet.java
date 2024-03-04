package pet_store;

import java.io.File;

public class Pet {

	private static DbManager dbLink = DbManager.getDbManagerInstance();
	
	private String id;
	private String ownerId;
	private int categoryId;
	private String name;
	private boolean gender;
	private int age;
	private int weight;
	private int height;
	private int length;
	private String shortDescription;
	private String fullDescription;
	private String photo;
	
	
	
	public Pet(String ownerId, String category, String name, String gender, int age, int weight, int height, int length,
			String shortDescription, String fullDescription, String photo) {
		this.id = User.generateUUID();
		this.ownerId = ownerId;
		
		try {
			this.categoryId = getCategoryId(category);
		} catch (Exception e) {
			this.categoryId = 1;
			e.printStackTrace();
		}
		
		this.name = name;
		if(gender.equals("male")) {
			this.gender = true;
		}
		else {
			this.gender = false;
		}
		
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.length = length;
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		this.photo = photo;
	}
	
	

	public Pet(String id, String ownerId, int categoryId, String name, boolean gender, int age, int weight, int height,
			int length, String shortDescription, String fullDescription, String photo) {
		System.out.println("created pet ");
		this.id = id;
		this.ownerId = ownerId;
		this.categoryId = categoryId;
		this.name = name;
		this.gender = gender;
		this.age = age;
		this.weight = weight;
		this.height = height;
		this.length = length;
		this.shortDescription = shortDescription;
		this.fullDescription = fullDescription;
		this.photo = photo;
	}





	public String getId() {
		return id;
	}
	
	public String getOwnerId() {
		return ownerId;
	}



	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}



	public int getCategoryId() {
		return categoryId;
	}



	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}

	public boolean getGender() {
		return gender;
		
	}

	public String getGenderString() {
		if(gender)
			return "male";
		return "female";
	}



	public void setGender(boolean gender) {
		this.gender = gender;
	}



	public int getAge() {
		return age;
	}



	public void setAge(int age) {
		this.age = age;
	}



	public int getWeight() {
		return weight;
	}



	public void setWeight(int weight) {
		this.weight = weight;
	}



	public int getHeight() {
		return height;
	}



	public void setHeight(int height) {
		this.height = height;
	}



	public int getLength() {
		return length;
	}



	public void setLength(int length) {
		this.length = length;
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



	
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	private int getCategoryId(String category) throws Exception {
		int categoryId = dbLink.getPetCategoryId(category);
		if(categoryId != -1)
			return categoryId;
		throw new Exception("Exception : Category Not found in DB");
	}

	public boolean sendToDb() {
		if (dbLink.insertPet(this) == 1) 
			return true;
		return false;
	}
	
	public static void removePet(String petName, String ownerID) {
		System.out.println("Removing pet for : " + ownerID + " , " + petName);
		//String petPhotoPath = DbManager.convertPath(dbLink.getPetPhotoPath(petName, ownerID));
		//String petPhotoPath = DbManager.convertPath(dbLink.getPetPhotoPath(petName, ownerID),  petName,  ownerID);
		String petPhotoPath = DbManager.IMGS_FULL_PATH.resolve(dbLink.getPetPhotoPath(petName, ownerID)).toString();
		
		System.out.println("photo path in remove pet : " + petPhotoPath);
		File petPhoto = new File(petPhotoPath); 
	    if (petPhoto.delete() && dbLink.removePet(petName, ownerID) == 1) { 
	      System.out.println("Pet removed : " + petName +", " + ownerID);
	    } else {
	      System.out.println("Failed to delete the file.");
	    } 
			
	    
	 
		
	}
	


	
}
