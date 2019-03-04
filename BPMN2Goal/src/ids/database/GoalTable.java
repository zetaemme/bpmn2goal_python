package ids.database;

import ids.model.Entity;
import ids.model.GoalEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalTable extends Table {

	@Override
	protected String getTableName() {
		return "adw_goal";
	}

	@Override
	protected Entity fillEntity(ResultSet set) throws SQLException {
		GoalEntity response = new GoalEntity();
		
		response.setId( set.getInt("id") );
		response.setName( set.getString("name") );
		response.setPack( set.getString("pack") );
		response.setGoalDescription( set.getString("goal_description") );
		return response;
	}

	@Override
	protected String getInsertString(Entity entity) {
		GoalEntity elem = (GoalEntity) entity;
		String update = "INSERT INTO "+getTableName()+" (pack,name,goal_description) VALUES ('"+elem.getPack()+"' , '"+elem.getName()+"' , '" +elem.getGoalDescription()+ "')";
		return update;
	}

}
