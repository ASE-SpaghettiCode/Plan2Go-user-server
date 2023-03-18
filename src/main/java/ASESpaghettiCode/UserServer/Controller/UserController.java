package ASESpaghettiCode.UserServer.Controller;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import ASESpaghettiCode.UserServer.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;



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



}
