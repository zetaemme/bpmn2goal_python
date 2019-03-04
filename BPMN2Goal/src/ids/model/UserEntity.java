package ids.model;

public class UserEntity extends Entity {

	private int id;
	
	private String username;
	
	private String email;

	private int role_ref;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRole_ref() {
		return role_ref;
	}

	public void setRole_ref(int role_ref) {
		this.role_ref = role_ref;
	}
	
	
}
