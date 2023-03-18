package ASESpaghettiCode.UserServer.Model;

import org.springframework.data.annotation.Id;

public class User{
    @Id
    public String userId;
    public String username;
    public String password;
    public String token;

    public User(String username, String password, String token){
        this.password=password;
        this.username=username;
        this.token=token;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername(){
        return username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password=" + password+'}';
    }
}
