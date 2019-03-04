package ids.database;

import ids.model.ContextEntity;
import ids.model.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContextTable extends Table {

	@Override
	protected String getTableName() {
		return "adw_context";
	}

	@Override
	protected Entity fillEntity(ResultSet set) throws SQLException {
		ContextEntity response = new ContextEntity();
		set.first();
		
		response.setId( set.getInt("id") );
		response.setCreated( set.getDate("created") );
		response.setComplete( set.getBoolean("complete") );	
		return response;
	}

	@Override
	protected String getInsertString(Entity entity) {
		String update = "INSERT INTO "+getTableName()+" ( complete ) VALUES ( false )";
		return update;
	}

}
