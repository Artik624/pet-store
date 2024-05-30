package pet_store;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * DbManager class responsible for handling all connections to the Database.
 * The class has only one instance that is shared across the program.
 * The instance is managed by the Java synchronization framework.
 * The class implements AutoClosable to manage the connection to the DB.
 * @author Artiom Cooper
 */
public class DbManager implements AutoCloseable {
	
	private final String DB_ADDRESS = "jdbc:postgresql://localhost:5432/pet_store";
	private final String DB_MANAGER_TYPE = "postgres";
	private final String usersTable = "users";
	private final String petsTable = "pets";
	private final String categoriesTable = "categories";
	private final String adoptionsTable = "adoption_requests";
	private final String[] TABLES = {usersTable, categoriesTable, petsTable, adoptionsTable};
	
	private static DbManager dbManagerInstance;
	private static Connection con;
	
	//Paths for storing images in the project
	protected static final Path SRC_FULL_PATH = Paths.get("C:", "Users", "artiu", "eclipse-workspace", "pet_store", "src", "main");
	protected static final Path IMGS_FULL_PATH = Paths.get("C:", "Users", "artiu", "eclipse-workspace", "pet_store", "src", "main", "webapp","resources", "images");
	protected static final Path IMGS_RLTV_PATH = Paths.get(".").resolve("resources").resolve("images");
	
	private Logger logger = Logger.getLogger(DbManager.class.getName());
	
	/**
	 * Constructor, establishes a connection to the DBServer and checks if the required tables exist.
	 * If a table isn't found , it will create it.
	 * @throws Exception---If One of the methods to generate a table fails, meaning a table is missing in the DB
	 */
	private DbManager() throws Exception  {
		try {
			System.out.println("Checking DB connection Health");
			con = DriverManager.getConnection(DB_ADDRESS, DB_MANAGER_TYPE, null);
			DatabaseMetaData dbMetaData = con.getMetaData();
			ResultSet rs = null;
			for(String tableName : TABLES) {
				
				rs = dbMetaData.getTables(null, null, tableName, new String[] {"TABLE"});
				if (rs.next()) {
					System.out.println("Table " + tableName + " exists.");
				} else {
					
					System.out.println("Table " +tableName + " does not exist.");
					if(!generateMissingTables(tableName))
						throw new Exception("DB Table not found Exception");
				}
				rs = null;
			}
			System.out.println("DB Connection healthy");
		} catch (SQLException e) {
			logger.severe("Exception in DbManager constructor -> " + e);
			throw new Exception("DB Connection Failed Exception");
		}
	}
	

	/**
	 * Implementation of the AutoClosable interface, will close the connection once no references exist.
	 */
	@Override
	public void close() throws SQLException {
		if (con != null && ! con.isClosed()) {
            con.close();
        }
	}
	
	/**
	 * Responsible for distributing the DBManager instance concurrently
	 * @return DbManager object
	 */
	public static synchronized DbManager getDbManagerInstance() {
		if(dbManagerInstance == null) {
			try {
				dbManagerInstance = new DbManager();
				
			} catch (Exception e) {
				System.out.println("SERVER ERROR : DB Error -> " + e);
			}
		}
		return dbManagerInstance;
	}
		
	/**
	 * Helper method to generate missing tables in the DB
	 * @param missingTable --- The name of the missing table
	 * @return true if createUsersTable() returns true
	 */
	private boolean generateMissingTables(String missingTable) {
		boolean creationResult;
		switch (missingTable) {
		case usersTable: {
			creationResult = createUsersTable(missingTable);
			break;
			
		}
		case petsTable: {
			creationResult = createPetsTable(missingTable);
			break;
		}
		case categoriesTable: {
			creationResult = createCategoriesTable(missingTable);
			break;
		}
		case adoptionsTable: {
			creationResult = createAdoptionsTable(missingTable);
			break;
		}
		default:
			creationResult = false;;
		}
		return creationResult;
	}
	
	/**
	 * Helper method to execute the create tables statement.
	 * @param sqlStatement---The statement to execute
	 * @return true if statement execution didn't throw an exception
	 */
	private boolean executeStatement(String sqlStatement) {
		Statement statement = null;
		boolean result = false;
		try {
			statement = DbManager.con.createStatement();
			statement.execute(sqlStatement);
			System.out.println("Statement executed successfully.");
			result = true;
			
		} 
		catch (SQLException e) {
			System.out.println("ERROR executing sqlStatement :\n" + sqlStatement + "\nException -> " + e);
			result =  false;
		} 
		finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					System.out.println("ERROR closing statement -> " + e);
				}
			}
		}
		return result;
	}
	
	/**
	 * Executes the SQL statement for users table
	 * @param tableName the users table name
	 * @return true if executed without exceptions
	 */
	private boolean createUsersTable(String tableName) {
		String createTableSQL = "CREATE TABLE " + tableName  +
				"("+
				"id TEXT PRIMARY KEY, " +
				"first_name TEXT NOT NULL, " +
				"last_name TEXT NOT NULL, " +
				"password TEXT NOT NULL, " +
				"email TEXT NOT NULL, " +
				"phone integer NOT NULL, " +
				"city TEXT NOT NULL, " +
				"street TEXT NOT NULL, " +
				"street_number integer NOT NULL"
				+ ");";
		return executeStatement(createTableSQL);
	}
	
	/**
	 * Executes the SQL statement for pets table
	 * @param tableName the users table name
	 * @return true if executed without exceptions
	 */
	private boolean createPetsTable(String tableName) {
		
		String createTableSQL = "CREATE TABLE " + tableName +
				"(" +
				"id TEXT PRIMARY KEY, " +
				"owner_id TEXT NOT NULL, " +
				"category_id INTEGER NOT NULL, " +
				"name TEXT NOT NULL, " +
				"gender BOOLEAN NOT NULL, " +
				"age INTEGER NOT NULL, " +
				"weight INTEGER NOT NULL, " +
				"height INTEGER NOT NULL, " +
				"length INTEGER NOT NULL, " +
				"short_description TEXT NOT NULL, " +
				"full_description TEXT NOT NULL, " +
				"photo TEXT NOT NULL, " +
				"FOREIGN KEY (owner_id) REFERENCES users(id), " +
				"FOREIGN KEY (category_id) REFERENCES categories(category_id) " +
				");";
		return executeStatement(createTableSQL);
	}
	
	/**
	 * Executes the SQL statement for categories table
	 * @param tableName the users table name
	 * @return true if executed without exceptions
	 */
	private boolean createCategoriesTable(String tableName) {
		String createTableSQL = "CREATE TABLE " + tableName  +
				"("+
				"category_id integer PRIMARY KEY, " +
				"category TEXT NOT NULL"
				+ ");";
		return executeStatement(createTableSQL);
	}
	
	/**
	 * Executes the SQL statement for adoptions table
	 * @param tableName the users table name
	 * @return true if executed without exceptions
	 */
	private boolean createAdoptionsTable(String tableName) {
		String createTableSQL = "CREATE TABLE " + tableName +
				"(" +
				"pet_id TEXT, " +
				"requester_id TEXT, " +
				"owner_id TEXT, " +
				"message TEXT NOT NULL, " +
				"PRIMARY KEY (pet_id, requester_id), " +
				"FOREIGN KEY (owner_id) REFERENCES users(id) " +
				");";
		return executeStatement(createTableSQL);
	}
	
	/**
	 * Executes the SQL statement to insert a new user to the DB
	 * @param newUser The user object to insert
	 * @return number of rows affected in the DB
	 */
	protected int insertUser(User newUser) {
		String insertUserSql = "INSERT INTO \"users\" (first_name, last_name, password, email, phone, city, street, street_number, id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int rowsAffected = 0;
		try (PreparedStatement statement = con.prepareStatement(insertUserSql))
		{
			  statement.setString(1, newUser.getFirstName());
	          statement.setString(2, newUser.getLastName());
	          statement.setString(3, newUser.getPassword());
	          statement.setString(4, newUser.getEmail());
	          statement.setInt(5, newUser.getPhone());
	          statement.setString(6, newUser.getCity());
	          statement.setString(7, newUser.getStreet());
	          statement.setInt(8, newUser.getStreetNumber());
	          statement.setString(9, newUser.getId());
          
	          rowsAffected = statement.executeUpdate();
          
		} catch (Exception e) {
			System.out.println("Exception in DbManager.inserUser()-> " +e);
			
		}
		return rowsAffected;
		
	}
	
	/**
	 * Method that checks if an email already exists in the DB, i.e a user already registered
	 * @param email--the email to compare against
	 * @return true if a row with the email exists in the DB
	 */
	protected boolean checkEmail(String email) {
		String checkEmailSql = "SELECT EXISTS(SELECT 1 FROM users WHERE email=?)";
		
		boolean emailFound = false;
		try (PreparedStatement statement = con.prepareStatement(checkEmailSql))
		{
		  statement.setString(1, email);
		  ResultSet resultSet = statement.executeQuery();
		  if (resultSet.next()) {
              emailFound = resultSet.getBoolean(1);
              
          }
          
		} catch (Exception e) {
			System.out.println("Exception in DbManager.checkEmail.()-> " +e);
			
		}
		return emailFound;
		
	}
	
	/**
	 * Retrieves the hashed password for a specific email from the users table
	 * @param email--the email for which to get the password
	 * @return the hashed password is found or an empty string if no value found
	 */
	protected String getHashedPassword(String email) {
		String getPasswordSql = "SELECT password FROM users WHERE email=?";
		if(checkEmail(email)) {
			try(PreparedStatement statement = con.prepareStatement(getPasswordSql)){
				statement.setString(1, email);
				ResultSet resultSet = statement.executeQuery();
				if (resultSet.next()) {
		              String hashedPass = resultSet.getString(1);
		              return hashedPass;
		        }
				else {
					System.out.println("Error: HashedPassword not found");
					return "";
				}
			}
			catch (Exception e) {
				System.out.println("Exception in DbManager.getHashedPassword()-> " + e);
			}
		}
		return "";
	}
	
	/**
	 * Gets the columns of a row for a specific email from the users table
	 * @param email---the email for the lookup in the table
	 * @return if found , a User object that corresponds to the retrieved data,  otherwise null
	 */
	protected User getUser(String email) {
		String getUserDataSql = "SELECT * FROM users WHERE email=?";
		try(PreparedStatement statement = con.prepareStatement(getUserDataSql)){
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String uuid = resultSet.getString(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				int phone = resultSet.getInt(6);
				String city = resultSet.getString(7);
				String street = resultSet.getString(8);
				int streetNumber = resultSet.getInt(9);
				
				return new User(uuid, firstName, lastName, email,  phone, city, street, streetNumber);
	              
			}
		}
		catch (Exception e){
			System.out.println("Exception in DbManager.getUser() -> " + e);
		}
		return null;
	}
	
	/***
	 * Retrieves all categories from the categories table
	 * @return A list of categories or null if not categories found
	 */
	protected List<String> getCategories() {
		String getCategoriesSql = "SELECT category FROM categories";
		List<String> categories = new ArrayList<String>();
		try(PreparedStatement statement = con.prepareStatement(getCategoriesSql)){
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				categories.add(resultSet.getString(1));
            }
			return categories;
		}
		catch (Exception e) {
			System.err.println("Exception in DbManager.getCategories() -> " + e);
		}
		return null;
	}
	
	/**
	 * Retrieve a category ID based on category name
	 * @param category--the category for which to get the ID
	 * @return ID for specific category, or -1 if not found
	 */
	protected int getPetCategoryId(String category) {
		String getCategoryIdSql = "SELECT \"category_id\" FROM \"categories\" WHERE category=?" ;
		try(PreparedStatement statement = con.prepareStatement(getCategoryIdSql)){
			statement.setString(1, category);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
	              int categoryId = resultSet.getInt(1);
	              return categoryId;
	        }
			else {
				System.out.println("ERROR in DbManager.getPetCategoryId() , No categories found");
				return -1;
			}
		}
		catch (Exception e){
			System.err.println("Exception in get DbManager.getPetCategoryId()-> " + e);
			return -1;
		}
	}

	/**
	 * Method to retrieve a category by ID
	 * @param id--ID for which to get category
	 * @return category or an empty string if not found
	 */
	protected String getPetCategoryById(int id) {
		String getCategorySql = "SELECT \"category\" FROM \"categories\" WHERE category_id=?" ;
		try(PreparedStatement statement = con.prepareStatement(getCategorySql)){
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
	              String category = resultSet.getString(1);
	              return category;
	        }
			else {
				System.out.println("ERROR in DbManager.getPetCategoryById() -> category Id not found");
				return "";
			}
		}
		catch (Exception e){
			System.err.println("Exception in DbManager.getPetCategoryById()-> " + e);
			return "";
		}
	}
	
	/**
	 * Method to insert a pet to the DB pets table
	 * @param newPet -- the pet object to insert
	 * @return number of rows affect in the DB
	 */
	protected int insertPet(Pet newPet) {
		String insertPetSql = "INSERT INTO \"pets\" (id, owner_id, category_id, name, age, gender, weight, height, length, short_description, full_description, photo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int rowsAffected = 0;
		try (PreparedStatement statement = con.prepareStatement(insertPetSql))
		{
			  statement.setString(1, newPet.getId());
	          statement.setString(2, newPet.getOwnerId());
	          statement.setInt(3, newPet.getCategoryId());
	          statement.setString(4, newPet.getName());
	          statement.setInt(5, newPet.getAge());
	          statement.setBoolean(6, newPet.getGender());
	          statement.setInt(7, newPet.getWeight());
	          statement.setInt(8, newPet.getHeight());
	          statement.setInt(9, newPet.getLength());
	          statement.setString(10, newPet.getShortDescription());
	          statement.setString(11, newPet.getFullDescription());
	          statement.setString(12, newPet.getPhoto());
          
	          rowsAffected = statement.executeUpdate();
          
		} catch (Exception e) {
			logger.severe("Exception in DbManager.insertPet() -> " + e);
		}
		return rowsAffected;
	}
	
	/**
	 * Retrieve all pets for a specific owner by ID
	 * @param owner_id -- owner ID for which to get pets
	 * @return -- a list of pets if found or null
	 */
	protected List<Pet> getPetsByOwnerID(String owner_id){
		List<Pet> pets = new ArrayList<Pet>();
		String getPetSql = "SELECT * FROM pets WHERE owner_id=?";
		try(PreparedStatement statement = con.prepareStatement(getPetSql)){
			statement.setString(1, owner_id);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				
				Pet p = new Pet(resultSet.getString("id"), resultSet.getString("owner_id"), resultSet.getInt("category_id"), resultSet.getString("name"), 
	            		  resultSet.getBoolean("gender"), resultSet.getInt("age"), resultSet.getInt("weight"), resultSet.getInt("height"), resultSet.getInt("length"),
	            		  resultSet.getString("short_description"), resultSet.getString("full_description"), resultSet.getString("photo")) ;
	              pets.add(p);
	        }
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.getPetsByOwnerID() -> " + e);
			return null;
		}
		return pets;
	}
	
	/**
	 * Retrieve all pets in the db, but exclude the owner when not null
	 * @param user_id -- the user id to exclude 
	 * @return list of pets or null
	 */
	protected List<Pet> getPets(String user_id){
		List<Pet> pets = new ArrayList<Pet>();
		String getPetsSql = "SELECT * FROM pets WHERE owner_id!=?";
		try(PreparedStatement statement = con.prepareStatement(getPetsSql)){
			statement.setString(1, user_id);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
				
				Pet p = new Pet(resultSet.getString("id"), resultSet.getString("owner_id"), resultSet.getInt("category_id"), resultSet.getString("name"), 
	            		  resultSet.getBoolean("gender"), resultSet.getInt("age"), resultSet.getInt("weight"), resultSet.getInt("height"), resultSet.getInt("length"),
	            		  resultSet.getString("short_description"), resultSet.getString("full_description"), resultSet.getString("photo")) ;
	              pets.add(p);
	        }
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.getPets() -> " + e);
			return null;
		}
		return pets;
	}
	
	/**
	 * Remove a pet from pets table
	 * @param pet --- the pet to remove from the table
	 * @return number of rows affected in the DB
	 */
	protected int removePet(Pet pet) {
		String removePetSql = "DELETE FROM pets WHERE id=? ";
		int petsRowsAffected = 0;
		try(PreparedStatement statement = con.prepareStatement(removePetSql)){
			statement.setString(1, pet.getId());
			petsRowsAffected = statement.executeUpdate();
			System.out.println("Removed petID from pets table: " + pet.getId() + ", rows affected : " + petsRowsAffected);
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.removePet():removePetSql -> " + e);
		}
		removePetFromAdoptions(pet);
		return petsRowsAffected;
	}
	
	/**
	 * Remove a pet from adoption_requests table
	 * @param pet -- the pet to remove
	 * @return number of rows affected in the DB
	 */
	protected int removePetFromAdoptions(Pet pet) {
		String removeAdoptionSql = "DELETE FROM adoption_requests WHERE pet_id=? ";
		int adoptionsRowsAffected = 0;
		
		try(PreparedStatement statement = con.prepareStatement(removeAdoptionSql)){
			statement.setString(1, pet.getId());
			//statement.setString(2, petName);
			adoptionsRowsAffected = statement.executeUpdate();
			System.out.println("Removed petID from adoption_requests table: " + pet.getId() + ", rows affected : " + adoptionsRowsAffected);
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.removePetFromAdoptions() -> " + e);
		}
		
		return adoptionsRowsAffected;
	}
	
	/**
	 * Remove a row from adoptions_request table for a specific requester and pet
	 * @param requester -- The requester to adopt
	 * @param pet -- the pet requested to adopt
	 * @return number of rows affected in the DB
	 */
	protected int removeAdoptionRequest(User requester, Pet pet) {
		String removeAdoptionSql = "DELETE FROM adoption_requests WHERE requester_id=? AND pet_id=? ";
		int adoptionsRowsAffected = 0;
		
		try(PreparedStatement statement = con.prepareStatement(removeAdoptionSql)){
			statement.setString(1, requester.getId());
			statement.setString(2, pet.getId());
			//statement.setString(2, petName);
			adoptionsRowsAffected = statement.executeUpdate();
			System.out.println("Removed request from adoption_requests table: " + pet.getId() + ", rows affected : " + adoptionsRowsAffected);
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.removeAdoptionRequest() -> " + e);
		}
		
		return adoptionsRowsAffected;
	}
	
	
	/**
	 * Retrieve a pet row from pets table by ID
	 * @param id -- The pet Id to retrieve
	 * @return pet object or null
	 */
	protected Pet getPetByID(String id) {
		String getPetSql = "SELECT * FROM pets WHERE id=?";
		//List<Pet> pet = new ArrayList<Pet>();
		try(PreparedStatement statement = con.prepareStatement(getPetSql)){
			statement.setString(1, id);
			ResultSet resultSet = statement.executeQuery();
			if(resultSet.next()) {
				Pet pet = new Pet(resultSet.getString("id"), resultSet.getString("owner_id"), resultSet.getInt("category_id"), resultSet.getString("name"), 
	            		  resultSet.getBoolean("gender"), resultSet.getInt("age"), resultSet.getInt("weight"), resultSet.getInt("height"), resultSet.getInt("length"),
	            		  resultSet.getString("short_description"), resultSet.getString("full_description"), resultSet.getString("photo")) ;
	           System.out.println("Found pet with ID : " + id);
				return pet;
			}
			
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.getPetByID()  -> " + e);
		}
		return null;
	}
	
	
	/**
	 * Retrieve the photo file path of a pet by pet name and owner Id 
	 * @param petName --- the pet name 
	 * @param ownerID --- the owner id
	 * @return path string or empty string
	 */
	protected String getPetPhotoPath(String petName, String ownerID) {
		String getPetPhotoPathSql = "SELECT photo FROM pets WHERE owner_id=? AND name=?";
		String photoPath ="";
		try(PreparedStatement statement = con.prepareStatement(getPetPhotoPathSql)){
			statement.setString(1, ownerID);
			statement.setString(2, petName);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				photoPath = resultSet.getString(1);
			}
			return photoPath;
			
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.getPetPhotoPath() ->" + e);
			return photoPath;
			
		}
	}
	
	/**
	 * retrieve owner details by pet id
	 * @param petId -- the pet Id for which to get the owner
	 * @return owner of the pet or null if not found
	 */
	protected User getOwnerByPetID(String petId) {
		String getOwner = "select * from users "
				+ "where id=(select owner_id from pets where id=?)";
		try(PreparedStatement statement = con.prepareStatement(getOwner)){
			statement.setString(1, petId);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String uuid = resultSet.getString(1);
				String firstName = resultSet.getString(2);
				String lastName = resultSet.getString(3);
				String email = resultSet.getString(5);
				int phone = resultSet.getInt(6);
				String city = resultSet.getString(7);
				String street = resultSet.getString(8);
				int streetNumber = resultSet.getInt(9);
				
				return new User(uuid, firstName, lastName, email,  phone, city, street, streetNumber);
	              
			}
		}
		catch (Exception e){
			logger.severe("Exception in DbManager.getOwnerByPetID() ->" + e);
		}
		return null;
	}
	
	/**
	 * Add adoption request to adoption_request table
	 * @param petId -- pet ID to adopt
	 * @param requesterId -- The user ID that requests an adoption
	 * @param ownerId -- the user Id of the pet
	 * @param messageToOwner -- Message from requester to owner
	 * @return -- true if successfully added a row
	 */
	protected boolean sendAdoptionRequestToDB(String petId, String requesterId, String ownerId, String messageToOwner) {
		String insertAdoptionRequestSql = "INSERT INTO adoption_requests (pet_id, requester_id, owner_id, message) " +
                "VALUES (?, ?, ?, ?)";
		
		int rowsAffected = 0;
		try (PreparedStatement statement = con.prepareStatement(insertAdoptionRequestSql))
		{
			  statement.setString(1, petId);
	          statement.setString(2, requesterId);
	          statement.setString(3, ownerId);
	          statement.setString(4, messageToOwner);
	          
          
	          rowsAffected = statement.executeUpdate();
	          if(rowsAffected > 0)
	        	  return true;
          
		} catch (Exception e) {
			logger.severe("Exception in sendAdoptionRequestToDB() -> " + e);
			
		}
		return false;
	}
	
	/**
	 * Retrieve adoption requests for owner
	 * @param ownerId -- user ID for owner
	 * @return -- A list of requests or null
	 */
	protected List<AdoptionRequestWrapper> getAdoptionRequestsForUser(String ownerId) {
		List<AdoptionRequestWrapper> adoptionRequestsList = new ArrayList<>();
		String getAdoptionRequestsSql =  "SELECT " +
			    "    pets.name AS pet_name, " +
			    "    CONCAT(users.first_name, ' ', users.last_name) AS requester_name, " +
			    "    CONCAT(users.street_number, ' ', users.street, ' ', users.city) AS requester_address, " +
			    "    users.phone AS requester_phone, " +
			    "    adoption_requests.message AS request_message " +
			    "FROM " +
			    "    adoption_requests " +
			    "JOIN " +
			    "    users ON adoption_requests.requester_id = users.id " +
			    "JOIN " +
			    "    pets ON adoption_requests.pet_id = pets.id " +
			    "WHERE " +
			    "    adoption_requests.owner_id = ?;";
		
		try(PreparedStatement statement = con.prepareStatement(getAdoptionRequestsSql)){
			statement.setString(1, ownerId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String pet_name = resultSet.getString(1);
				String requester_name = resultSet.getString(2);
				String requester_address = resultSet.getString(3);
				int requester_phone = resultSet.getInt(4);
				String requester_message = resultSet.getString(5);
				AdoptionRequestWrapper request = new AdoptionRequestWrapper(pet_name, requester_name, requester_address,requester_phone, requester_message);
	            adoptionRequestsList.add(request);
			}
		}
		catch (Exception e){
			logger.severe("Exception in getAdoptionRequestsForUser() -> " + e);
		}
		return adoptionRequestsList;
	}
	
	/**
	 * Retrieve adoption requests by user via user ID
	 * @param requesterId -- the user ID which requested to adopt
	 * @return -- a list of requests or null
	 */
	protected List<AdoptionRequestWrapper> getAdoptionRequestsByUser(String requesterId) {
		List<AdoptionRequestWrapper> myAdoptionRequestsList = new ArrayList<>();
		String getMyAdoptionRequestsSql =  "SELECT " +
				"    pets.name AS pet_name, " +
				"    CONCAT(users.first_name, ' ', users.last_name) AS owner_name, " +
				"    CONCAT(users.street_number, ' ', users.street, ' ', users.city) AS owner_address, " +
				"    users.phone AS owner_phone, " +
				"    adoption_requests.message AS request_message, " +
				"	 pets.id AS pet_id	" +
				"FROM " +
				"    adoption_requests " +
				"JOIN " +
				"    users ON adoption_requests.owner_id = users.id " +
				"JOIN " +
				"    pets ON adoption_requests.pet_id = pets.id " +
				"WHERE " +
				"    adoption_requests.requester_id = ?;";
		
		try(PreparedStatement statement = con.prepareStatement(getMyAdoptionRequestsSql)){
			
			statement.setString(1, requesterId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String pet_name = resultSet.getString(1);
				String owner_name = resultSet.getString(2);
				String owner_address = resultSet.getString(3);
				int owner_phone = resultSet.getInt(4);
				String requester_message = resultSet.getString(5);
				String pet_id = resultSet.getString(6);
				AdoptionRequestWrapper request = new AdoptionRequestWrapper(pet_name, owner_name, owner_phone, owner_address, requester_message, pet_id);
				myAdoptionRequestsList.add(request);
				
				
			}
		}
		catch (Exception e){
			logger.severe("Exception in getAdoptionRequestsByUser() -> " + e);
		}
		return myAdoptionRequestsList;
	}
	
	/**
	 * Checks if an adoption request already exists for a pet by the user
	 * @param pet_id -- pet ID to adopt
	 * @param requester_id -- user id which made a request
	 * @return true if a result was found
	 */
	protected boolean isAdoptionRequestExists(String pet_id, String requester_id) {
		String checkRequestExistsSql = "SELECT * from adoption_requests WHERE pet_id=? and requester_id=?";
		try(PreparedStatement statement = con.prepareStatement(checkRequestExistsSql)){
			
			statement.setString(1, pet_id);
			statement.setString(2, requester_id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		}
		catch (Exception e){
			logger.severe("Exception in isAdoptionRequestExists() -> " + e);
		}
		
		return false;
	}
	
	/**
	 * Add a category to categories table
	 * @param category -- category to add 
	 * @return number of rows affected
	 */
	protected int addNewCategoryToDb(String category) {
		List<String> categories = getCategories();
		int rowsAffected = 0;
		if(!categories.contains(category)) {
			String insertCategorySql = "INSERT INTO categories (category_id, category) VALUES(?,?)";
			try (PreparedStatement statement = con.prepareStatement(insertCategorySql))
			{
				  statement.setInt(1, categories.size()+1);
		          statement.setString(2, category.toLowerCase());
		          rowsAffected = statement.executeUpdate();
	          
			} catch (Exception e) {
				logger.severe("Exception in addNewCategoryToDb() -> " + e);
				
			}
		}
		return rowsAffected;
		
	}
	
	
	
}
