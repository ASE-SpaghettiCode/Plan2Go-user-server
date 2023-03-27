package ASESpaghettiCode.UserServer.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
<<<<<<< HEAD
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@Document
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String userName;

    private String password;

    private String email;

    private String gender;

    private String city;

    private LocalDateTime createdTime;

    public User(String userName, String password, String email, String gender, String city) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.city = city;
        this.createdTime = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
=======
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User{
    @Id
    public String userId;
    public String username;
    public String password;
    public String token;
    public String intro;
    public String imageLink;

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
>>>>>>> develop
    }

    public void setPassword(String password) {
        this.password = password;
    }

<<<<<<< HEAD
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
=======
    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIntro(){
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", password=" + password+'\''+
                ", intro=" + intro +'}';
>>>>>>> develop
    }
}
