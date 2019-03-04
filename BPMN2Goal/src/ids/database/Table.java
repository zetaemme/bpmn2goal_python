package ids.database;

import ids.model.Entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public abstract class Table {
	private final String database = "ids_workflow";//"workflow";
	private final String user = "ids_root";// "workflow";
	private final String password = "root";//"workflow";

	private Connection connection;
	
	public Table() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+database,user,password);
			//connection = DriverManager.getConnection("jdbc:mysql://194.119.214.121:3306/"+database,user,password);
		} catch (ClassNotFoundException e) {
			System.out.println("error class");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("error database");
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnection() {
		return connection;
	}

	protected abstract String getTableName();
	protected abstract Entity fillEntity(ResultSet set) throws SQLException;
	protected abstract String getInsertString(Entity entity);

	public void deleteOneByID(int id) throws SQLException {
		String query = "DELETE FROM `"+getTableName()+"` WHERE `id` = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, id);
		//System.out.println(statement.toString());
		int rows = statement.executeUpdate();
		//System.out.println("deleted "+rows+" rows");
	}

	public Entity findOneByID(int id) throws SQLException {
		String query = "SELECT * FROM "+getTableName()+" WHERE id = '"+id+"'";
		ResultSet resultSet = getResultSet(query);
		return getOne(resultSet);
	}

	public Entity findOneBy(String column, String filter) throws SQLException {
		Entity response = null;
		Connection conn = getConnection();
		if (conn != null) {
			PreparedStatement st = conn.prepareStatement("SELECT * FROM "+getTableName()+" WHERE "+column+"=?");
			st.setString(1,filter);
			ResultSet resultSet = st.executeQuery();
			response = getOne(resultSet);
		}
		return response;
	}

	public Entity findOneBy(String column1, String filter1,String column2, String filter2) throws SQLException {
		PreparedStatement st = getConnection().prepareStatement(
				"SELECT * FROM " + getTableName() + " WHERE " + column1
						+ "=? AND " + column2 + "=?");
		st.setString(1, filter1);
		st.setString(2, filter2);
		ResultSet resultSet = st.executeQuery();
		return getOne(resultSet);
	}

	public Entity findOneBy(String column1, String filter1,String column2, String filter2,String column3, String filter3) throws SQLException {
		PreparedStatement st = getConnection().prepareStatement(
				"SELECT * FROM " + getTableName() + " WHERE " + column1
						+ "=? AND " + column2 + "=? AND " + column3 + "=?");
		st.setString(1, filter1);
		st.setString(2, filter2);
		st.setString(3, filter3);
		ResultSet resultSet = st.executeQuery();
		return getOne(resultSet);
	}
	
	public Entity findOneBy(String column1, String filter1,String column2, String filter2,String column3, String filter3,String column4, String filter4) throws SQLException {
		PreparedStatement st = getConnection().prepareStatement(
				"SELECT * FROM " + getTableName() + " WHERE " + column1
						+ "=? AND " + column2 + "=? AND " + column3 + "=? AND " + column4 + "=?");
		st.setString(1, filter1);
		st.setString(2, filter2);
		st.setString(3, filter3);
		st.setString(4, filter4);
		ResultSet resultSet = st.executeQuery();
		return getOne(resultSet);
	}

	public List<Entity> getAll() throws SQLException {
		PreparedStatement st = getConnection().prepareStatement("SELECT * FROM "+getTableName() );
		ResultSet resultSet = st.executeQuery();
		LinkedList<Entity> results = new LinkedList<Entity>();
		while (resultSet.next()) {
			Entity elem = fillEntity(resultSet);
			results.add(elem);
		}		
		return results;
	}

	public List<Entity> getAllWhere(String column, String filter) throws SQLException {
		PreparedStatement st = getConnection().prepareStatement("SELECT * FROM "+getTableName()+" WHERE "+column+"=?");
		st.setString(1,filter);
		ResultSet resultSet = st.executeQuery();
		LinkedList<Entity> results = new LinkedList<Entity>();
		while (resultSet.next()) {
			Entity elem = fillEntity(resultSet);
			results.add(elem);
		}		
		return results;
	}

	public void insertElement(Entity entity) throws SQLException {		
		String update_string = getInsertString(entity);
		Statement statement = getConnection().createStatement();
		statement.executeUpdate(update_string);
	}	

	private ResultSet getResultSet(String query) {
		ResultSet resultSet=null;
		Statement statement;
		try {
			Connection conn = getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	private Entity getOne(ResultSet resultSet) throws SQLException {
		Entity response = null;
		if (resultSet != null)	{
			resultSet.last();
			if (resultSet.getRow()>0) {
				resultSet.first();
				response = fillEntity(resultSet);
			}
		}
		return response;
	}

}
