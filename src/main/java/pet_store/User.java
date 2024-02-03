package pet_store;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.UUID;

//import org.hibernate.annotations.Entity;
//import javax.persistence.Entity;



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
	
	private static DbManager dbLink = DbManager.getDbManagerINstance();
	
	

	public User(String firstName, 
				String lastName, 
				String password, 
				String email, 
				int phone, 
				String city, 
				String street, 
				int streetNumber) {
		
		
		this.id = generateUUID();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = User.getHashPassword(password);
		this.phone = phone;
		this.email = email;
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
		
		
		String s = String.format("User created with ID %s, name %s %s", id, firstName, lastName);
		System.out.println(s);
	}
	
	private String generateUUID() {
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
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        return hashedPassword;
	}
	
	public static boolean checkHashedPassword(String password, String hashedPassword) {
		
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);
        return result.verified;
	}
	
	public static boolean checkUserEmailExists(String email) {
		return dbLink.checkEmail(email);
		
	}
	
	
	
}
