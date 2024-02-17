package pet_store;

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
	private String shortDescritpion;
	private String fullDescription;
	private String photo;
	
	
	
	public Pet(String ownerId, String category, String name, String gender, int age, int weight, int height, int length,
			String shortDescritpion, String fullDescription, String photo) {
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
		this.shortDescritpion = shortDescritpion;
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



	public String getShortDescritpion() {
		return shortDescritpion;
	}



	public void setShortDescritpion(String shortDescritpion) {
		this.shortDescritpion = shortDescritpion;
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
		dbLink.removePet(petName, ownerID);
		System.out.println("Pet removed : " + petName +", " + ownerID);
			
		
	}
	


	
}
