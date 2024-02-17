package pet_store;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.UUID;
import javax.faces.bean.SessionScoped;


@SuppressWarnings("deprecation")
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
	
	

	public User(String id,
				String firstName, 
				String lastName, 
				String password, 
				String email, 
				int phone, 
				String city, 
				String street, 
				int streetNumber) throws Exception {
		
		
		this.id = generateUUID();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		
		this.password = User.getHashPassword(this.password);
		if(this.password == "" || !this.password.startsWith("$2a$"))
			throw new Exception("Error creating user");
		
		String s = String.format("User created with ID %s, name %s %s", id, firstName, lastName);
		System.out.println(s);
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
	
	protected static String generateUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

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
	
	public boolean sendToDB() {
		if (dbLink.insertUser(this) == 1) 
			return true;
		return false;
	}
	
	
	public static String getHashPassword(String password) {

		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		if(password != null && hashedPassword != null)
			return hashedPassword;
		return "";
	}
	
	public static boolean authenticatePassword(String password, String email) {
        
		String hashedPassword = dbLink.getHashedPassword(email);
		boolean result = BCrypt.checkpw(password, hashedPassword);
        return result;
	}
	
	public static boolean checkUserEmailExists(String email) {
		return dbLink.checkEmail(email);
		
	}
	
	public static User getUser(String email) {
		return dbLink.getUser(email);
	}
	
	public List<String> getPetCategories(){
		//System.out.println("geting categories in User ");
		return dbLink.getCategories();
	}
	
	public List<String> getPetsList(){
		return dbLink.getPetsByOwnerID(this.getId());
	}
	
	
}
