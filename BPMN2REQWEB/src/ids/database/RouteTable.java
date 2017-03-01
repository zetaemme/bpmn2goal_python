package ids.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import ids.model.Entity;
import ids.model.RouteEntity;

public class RouteTable extends Table {
	
	public RouteTable() {
		super();
	}

	@Override
	protected String getTableName() {
		return "adw_route";
	}

	@Override
	protected Entity fillEntity(ResultSet set) throws SQLException {
		RouteEntity response = new RouteEntity();
		set.first();
		
		response.setId( set.getInt("id") );
		response.setAgent( set.getString("agent") );
		response.setService_path( set.getString("service") );	
		return response;
	}

	@Override
	protected String getInsertString(Entity entity) {
		RouteEntity elem = (RouteEntity) entity;
		String update = "INSERT INTO "+getTableName()+" (service_path, agent) VALUES ('"+elem.getService_path()+"' , '"+elem.getAgent()+"' )";
		return update;
	}
	
}
