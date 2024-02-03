package pet_store;
import java.sql.*;

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
	
	public static synchronized DbManager getDbManagerINstance() {
		if(dbManagerInstance == null) {
			dbManagerInstance = new DbManager();
		}
		
		return dbManagerInstance;
	}
		
	protected int insertUser(User newUser) {
		System.out.println("in InsertUser");
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
		System.out.println("Checking DB for email: " + email);
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
		System.out.println(email + " found ? : " + emailFound);
		return emailFound;
		
	}
}
