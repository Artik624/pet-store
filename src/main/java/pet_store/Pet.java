package pet_store;

import java.io.File;

/**
 * Pet object class used for getters and DB connection encapsulation
 * @author Artiom Cooper
 */
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
	
	
	/**
	 * Constructor with generated ID
	 * @param ownerId
	 * @param category
	 * @param name
	 * @param gender
	 * @param age
	 * @param weight
	 * @param height
	 * @param length
	 * @param shortDescription
	 * @param fullDescription
	 * @param photo
	 */
	public Pet(String ownerId, String category, String name, String gender, int age, int weight, int height, int length,
			String shortDescription, String fullDescription, String photo) {
		this.id = User.generateUUID();//generates a UUID for the pet 
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
	
	
	/**
	 * Constructor without generated ID, note that the signature is different
	 * @param id
	 * @param ownerId
	 * @param categoryId
	 * @param name
	 * @param gender
	 * @param age
	 * @param weight
	 * @param height
	 * @param length
	 * @param shortDescription
	 * @param fullDescription
	 * @param photo
	 */
	public Pet(String id, String ownerId, int categoryId, String name, boolean gender, int age, int weight, int height,
			int length, String shortDescription, String fullDescription, String photo) {
		
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

	//Getters & Setters
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
	//End of Getters & Setters
	
	/**
	 * Gets category name from DB by Category ID
	 * @return Category name
	 */
	public String getCategory() {
		return dbLink.getPetCategoryById(this.getCategoryId());
	}
	
	/**
	 * Gets category ID from DB
	 * @param category
	 * @return
	 * @throws Exception--if category not found in DB
	 */
	private int getCategoryId(String category) throws Exception {
		int categoryId = dbLink.getPetCategoryId(category);
		if(categoryId != -1)
			return categoryId;
		throw new Exception("Exception : Category Not found in DB");
	}

	/**
	 * Send a pet object to DB for insertion to pets table
	 * @return true if operation successful
	 */
	public boolean sendToDb() {
		if (dbLink.insertPet(this) == 1) 
			return true;
		return false;
	}
	
	/**
	 * Removes a pet from DB and related photo
	 * @param petId
	 * @param ownerID
	 */
	public static void removePet(String petId, String ownerID) {
		Pet pet = dbLink.getPetByID(petId);
		System.out.println("Removing pet for : " + ownerID + " , " + pet.getName());
		String petPhotoPath = DbManager.IMGS_FULL_PATH.resolve(dbLink.getPetPhotoPath(pet.getName(), ownerID)).toString();
		
		System.out.println("photo path in remove pet : " + petPhotoPath);
		File petPhoto = new File(petPhotoPath); 
	    if (dbLink.removePet(pet) > 0) { 
	      System.out.println("Pet removed from DB : " + pet.getName() +", " + ownerID);
	      if(petPhoto.delete()) {
	    	  System.out.println("Successfully deleted pet photo.");
	      }else{
		      System.out.println("Failed to delete the file.");
		  } 
	    }else {
	    	System.out.println("Failed to remove pet.");
	    }	
	}
	
	/**
	 * Gets pet from DB by Pet ID
	 * @param Id of the pet
	 * @return the pet with the requested ID
	 */
	public static Pet getPetById(String Id) {
		return dbLink.getPetByID(Id);
	}
	


	
}
