package ids.database;

import ids.model.ContextEntity;
import ids.model.Entity;
import ids.model.JobEntity;
import ids.model.UserEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JobTable extends Table {

	@Override
	protected String getTableName() {
		return "adw_job";
	}

	@Override
	protected Entity fillEntity(ResultSet set) throws SQLException {
		JobEntity response = new JobEntity();
		//set.first();
		
		response.setId( set.getInt("id") );
		response.setService( set.getString("service"));
		response.setLabel( set.getString("label"));
		response.setUser( retrieveUser( set.getInt("user_ref")) );
		response.setContext( retrieveContext( set.getInt("session_ref")) );	
		response.setAgent( set.getString("owner_agent"));
		response.setCreated( set.getDate("created") );
		
		return response;
	}

	@Override
	protected String getInsertString(Entity entity) {
		JobEntity elem = (JobEntity) entity;
		
		String context = "";
		String values = "('"+elem.getService()+"','"+elem.getLabel()+"',"+elem.getUser().getId()+",'"+elem.getAgent()+"')";
		String fields = " service, label, user_ref, owner_agent ";

		if (elem.getContext() != null) {
			context = ""+elem.getContext().getId();
			values = "('"+elem.getService()+"','"+elem.getLabel()+"',"+elem.getUser().getId()+","+context+",'"+elem.getAgent()+"')";
			fields = " service, label, user_ref, session_ref, owner_agent ";
		}
		
		
		String update = "INSERT INTO "+getTableName()+" ( "+fields+" ) VALUES "+values;
		//System.out.println(update);
		
		return update;
	}

	
	private UserEntity retrieveUser(int ref) {
		UserTable user_table = new UserTable();
		UserEntity response = null;
		try {
			response = (UserEntity) user_table.findOneByID(ref);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}

	private ContextEntity retrieveContext(int ref) {
		ContextTable context_table = new ContextTable();
		ContextEntity response = null;
		try {
			response = (ContextEntity) context_table.findOneByID(ref);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return response;
	}

}
