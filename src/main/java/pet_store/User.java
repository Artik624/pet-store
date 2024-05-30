package pet_store;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Represents the user object , mainly for getters and DB encapsulation
 * @author Artiom Cooper
 */
public class User {
	private String id;
	private String firstName;
	private String lastName;
	private String password; 
	private String email; 
	private int phone; 
	private String city; 
	private String street; 
	private int streetNumber;
	
	private static DbManager dbLink = DbManager.getDbManagerInstance();
	private Logger logger = Logger.getLogger(DbManager.class.getName());
	
	/**
	 * Constructor
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param email
	 * @param phone
	 * @param city
	 * @param street
	 * @param streetNumber
	 * @throws Exception
	 */
	public User(String id,
				String firstName, 
				String lastName, 
				String password, 
				String email, 
				int phone, 
				String city, 
				String street, 
				int streetNumber) throws Exception {
		
		
		this.id = generateUUID();//Generates a UUID for the user
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		this.password = User.getHashPassword(this.password);
		logger.info("Creating new User");
		if(this.password == "" || !this.password.startsWith("$2a$"))
			throw new Exception("Error creating user");
		
		logger.info(String.format("User created with ID %s, name %s %s", id, firstName, lastName));
	}
	
	public User(String uuid,
			String firstName, 
			String lastName,  
			String email, 
			int phone, 
			String city, 
			String street, 
			int streetNumber) {
		
		this.id = uuid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
		this.email = email;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		
	}
	/**
	 * Generates a UUID
	 * @return UUID
	 */
	protected static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	//Getters & Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
	//End of Getters & Setters
	
	/**
	 * Sends User to DB class to insert into users table
	 * @return true if operation was successful
	 */
	public boolean sendToDB() {
		if (dbLink.insertUser(this) == 1) 
			return true;
		return false;
	}
	
	/**
	 * Generates a hashed password with BCrypt
	 * @param password
	 * @return hashed password or empty String
	 */
	public static String getHashPassword(String password) {
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		if(password != null && hashedPassword != null)
			return hashedPassword;
		return "";
	}
	
	/**
	 * Checks if the provided password from the users authenticates with the hashed password
	 * @param password
	 * @param email
	 * @return true if password match
	 */
	public static boolean authenticatePassword(String password, String email) {
		String hashedPassword = dbLink.getHashedPassword(email);
		boolean result = BCrypt.checkpw(password, hashedPassword);
        return result;
	}
	
	
	//Wrapper methods to perform DB operations for the User
	public static boolean checkUserEmailExists(String email) {
		return dbLink.checkEmail(email);
	}
	
	public static User getUser(String email) {
		return dbLink.getUser(email);
	}
	
	public List<String> getPetCategories(){
		return dbLink.getCategories();
	}
	
	public List<Pet> getPetsList(){
		return dbLink.getPetsByOwnerID(this.getId());
	}
	
	public List<AdoptionRequestWrapper> getAdoptionRequests(){
		return dbLink.getAdoptionRequestsForUser(id);
	}
	
	public List<AdoptionRequestWrapper> getMyAdoptionRequests(){
		return dbLink.getAdoptionRequestsByUser(id);
	}
	
	public boolean checkIfAdoptionReqeuested(String pet_id) {
		return dbLink.isAdoptionRequestExists(pet_id, id);
	}
	
	public boolean addNewCategory(String category) {
		if(dbLink.addNewCategoryToDb(category) == 1)
			return true;
		return false;
	}
	
	public void removeSentRequest(Pet pet) {
		dbLink.removeAdoptionRequest(this, pet);
	}
	
	
}
