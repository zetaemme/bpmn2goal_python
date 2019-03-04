package ids.database;

import ids.model.Entity;
import ids.model.RoleEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleTable extends Table {

	@Override
	protected String getTableName() {
		return "adw_role";
	}

	@Override
	protected Entity fillEntity(ResultSet set) throws SQLException {
		RoleEntity response = new RoleEntity();
		
		response.setId( set.getInt("id") );
		response.setName( set.getString("name") );
		response.setAgent( set.getString("agent") );	
		return response;
	}

	@Override
	protected String getInsertString(Entity entity) {
		RoleEntity elem = (RoleEntity) entity;
		String update = "INSERT INTO "+getTableName()+" (name, agent) VALUES ('"+elem.getName()+"' , '"+elem.getAgent()+"' )";
		return update;
	}

}
