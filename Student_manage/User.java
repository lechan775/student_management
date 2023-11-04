package Student_manage;

public class User {
    private String username;
    private String password;
    private String personId;
    private String phoneNumber;

    public User() {
    }

    public User(String username, String password, String personId, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.personId = personId;
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
