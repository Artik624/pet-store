package pet_store;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public  class DbManager implements AutoCloseable {
	
	private static DbManager dbManagerInstance;
	private static Connection con;
	
	private DbManager()  {
		try {
			con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pet_store", "postgres", null);
			
		} catch (SQLException e) {
			System.out.println("Db Construction exception : " + e);
		}
	}

	@Override
	public void close() throws SQLException {
		if (con != null && ! con.isClosed()) {
            con.close();
        }
	}
	
	public static synchronized DbManager getDbManagerInstance() {
		if(dbManagerInstance == null) {
			dbManagerInstance = new DbManager();
		}
		
		return dbManagerInstance;
	}
		
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
        System.out.println("Affected rows : " + rowsAffected);
        // Checking the number of rows affected
          
          
		} catch (Exception e) {
			System.out.println(e);
			
		}
		return rowsAffected;
		
	}
	
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
			System.out.println(e);
			
		}
		return emailFound;
		
	}
	
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
					System.out.println("Error pass not found");
					return "";
				}
			}
			catch (Exception e) {
				System.out.println("Exception in getHashedPassword: " + e);
			}
		}
		return "";
	}
	
	protected User getUser(String email) {
		String getUserDataSql = "SELECT * FROM users WHERE email=?";
		try(PreparedStatement statement = con.prepareStatement(getUserDataSql)){
			statement.setString(1, email);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
	              //String hashedPass = resultSet.getString(1);
				String uuid = resultSet.getString(8);
				String firstName = resultSet.getString(1);
				String lastName = resultSet.getString(2);
				int phone = resultSet.getInt(4);
				String city = resultSet.getString(5);
				String street = resultSet.getString(6);
				int streetNumber = resultSet.getInt(7);
				
				return new User(uuid, firstName, lastName, email,  phone, city, street, streetNumber);
	              
			}
		}
		catch (Exception e){
			
		}
		return null;
	}
	
	protected List<String> getCategories() {
		//System.out.println("geting categories in DB ");
		String getCategoriesSql = "SELECT category FROM categories";
		List<String> categories = new ArrayList<String>();
		try(PreparedStatement statement = con.prepareStatement(getCategoriesSql)){
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				//System.out.println("category added ");
				categories.add(resultSet.getString(1));
            }
			//System.out.println(Arrays.toString(categories.toArray()));
			return categories;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
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
				System.out.println("ERROR category not found");
				return -1;
			}
		}
		catch (Exception e){
			System.out.println("Exception in get category ID : " + e);
			return -1;
		}
		
		
		
	}
	
	protected int insertPet(Pet newPet) {
		String insertPetSql = "INSERT INTO \"pets\" (id, owner_id, category, name, gender, weight, height, length, short_description, full_description, photo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		int rowsAffected = 0;
		try (PreparedStatement statement = con.prepareStatement(insertPetSql))
		{
			  statement.setString(1, newPet.getId());
	          statement.setString(2, newPet.getOwnerId());
	          statement.setInt(3, newPet.getCategoryId());
	          statement.setString(4, newPet.getName());
	          statement.setBoolean(5, newPet.getGender());
	          statement.setInt(6, newPet.getWeight());
	          statement.setInt(7, newPet.getHeight());
	          statement.setInt(8, newPet.getLength());
	          statement.setString(9, newPet.getShortDescritpion());
	          statement.setString(10, newPet.getFullDescription());
	          statement.setString(11, newPet.getPhoto());
          
          rowsAffected = statement.executeUpdate();
        System.out.println("Affected rows : " + rowsAffected);
        // Checking the number of rows affected
          
          
		} catch (Exception e) {
			System.out.println(e);
			
		}
		return rowsAffected;
	}
	
	protected List<String> getPetsByOwnerID(String owner_id){
		List<String> pets = new ArrayList<String>();
		String getPetSql = "SELECT name FROM \"pets\" WHERE owner_id=?";
		try(PreparedStatement statement = con.prepareStatement(getPetSql)){
			statement.setString(1, owner_id);
			ResultSet resultSet = statement.executeQuery();
			while(resultSet.next()) {
	              pets.add(resultSet.getString(1));
	              
	        }
		}
		catch (Exception e){
			System.out.println("Exception in get category ID : " + e);
			return null;
		}
		return pets;
	}
	
	protected void removePet(String petName, String ownerID) {
		String removePetSql = "DELETE FROM \"pets\" WHERE owner_id=? AND name=?";
		int rowsAffected = 0;
		try(PreparedStatement statement = con.prepareStatement(removePetSql)){
			statement.setString(1, ownerID);
			statement.setString(2, petName);
			statement.executeQuery();
			
		}
		catch (Exception e){
			System.out.println("Exception in Remove Pet  : " + e);
			
		}
	}
}
