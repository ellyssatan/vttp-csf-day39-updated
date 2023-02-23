package vttp_csf.day39.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class User {
    
    private int userId;
	private String name;
	private String email;

	public void setUserId(int userId) { this.userId = userId; }
	public int getUserId() {  return this.userId; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() {  return this.email; }

	public void setName(String name) { this.name = name; }
	public String getName() {  return this.name; }

    public static User create(SqlRowSet rs) {
        User u = new User();
        u.setUserId(rs.getInt("id"));
        u.setName("%s %s".formatted(rs.getString("first_name"), rs.getString("last_name")));
        u.setEmail(rs.getString("email_address"));

        return u;
    }
}
