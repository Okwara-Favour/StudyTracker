package DatabaseModder;

public class User
{
	private int id;
	private String profileName;

    public User(int id, String profileName) {
        this.id = id;
        this.profileName = profileName;
    }

    public int getId() {
        return id;
    }

    public String getProfileName() {
        return profileName;
    }
    
    public String toString()
    {
    	return "User has id: " + id + " and profile name: " + profileName;
    }
}