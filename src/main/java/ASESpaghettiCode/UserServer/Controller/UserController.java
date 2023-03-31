package ASESpaghettiCode.UserServer.Controller;

import ASESpaghettiCode.UserServer.Model.Note;
import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import ASESpaghettiCode.UserServer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;



@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    UserController(UserRepository userRepository, UserService userService){
        this.userRepository = userRepository;
        this.userService=userService;
    }
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<User> getAllUsers(){
        List<User> users = userService.getUsers();
        return users;
    }

    //register
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User createUser(@RequestBody User user){
        User createdUser = userService.createUser(user);
        return createdUser;
    }

    //login
    @PostMapping("/users/checking")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User loginUser(@RequestBody User user){
        User loggedInUser=userService.loginUser(user);
        return loggedInUser;
    }

    //logout
    @PutMapping("/users/checking/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logoutUser(@PathVariable String userId){
        User user = userService.getUserById(userId);
        userService.logoutUser(user);
    }
    //get user
    @GetMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public User user (@PathVariable("userId") String userId) {
        User user = userService.getUserById(userId);
        return user;
    }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editUser(@PathVariable("userId") String userId, @RequestBody User user){
        userService.editUser(user);
    }

    @PostMapping("users/{userId1}/follows/users/{userId2}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userFollowsUser(@PathVariable String userId1, @PathVariable String userId2) {
        userService.userFollowsUser(userId1, userId2);
    }

    @DeleteMapping("users/{userId1}/follows/users/{userId2}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void userUnfollowsUser(@PathVariable String userId1, @PathVariable String userId2) {
        userService.userUnfollowsUser(userId1, userId2);
    }

    @GetMapping("users/{userId}/followers")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getFollwers(@PathVariable String userId) {
        return userService.getFollowersById(userId);
    }

    @GetMapping("users/{userId}/followings")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getFollwings(@PathVariable String userId) {
        return userService.getFollowingsById(userId);
    }
}
