package ids.database;

import ids.model.Entity;
import ids.model.EntryPointEntity;
import ids.model.RoleEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntryPointTable extends Table {

	@Override
	protected String getTableName() {
		return "adw_entry";
	}

	@Override
	protected Entity fillEntity(ResultSet set) throws SQLException {
		EntryPointEntity response = new EntryPointEntity();
		//set.first();
		
		response.setId( set.getInt("id") );
		response.setService( set.getString("service"));
		response.setLabel( set.getString("label"));
		response.setRole( retrieveRole( set.getInt("role_ref")) );
		response.setAgent( set.getString("owner_agent"));
		response.setCreated( set.getDate("created") );
		response.setSession( set.getString("session") );
		
		return response;
	}

	@Override
	protected String getInsertString(Entity entity) {
		EntryPointEntity elem = (EntryPointEntity) entity;
		
		String values = "('"+elem.getService()+"','"+elem.getLabel()+"','"+elem.getRole().getId()+"','"+elem.getAgent()+"','"+elem.getSession()+"')";
		String fields = " service, label, role_ref, owner_agent, session ";
		
		String update = "INSERT INTO "+getTableName()+" ( "+fields+" ) VALUES "+values;
		
		return update;
	}

	
	private RoleEntity retrieveRole(int ref) {
		RoleTable role_table = new RoleTable();
		RoleEntity response = null;
		try {
			response = (RoleEntity) role_table.findOneByID(ref);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}

}
