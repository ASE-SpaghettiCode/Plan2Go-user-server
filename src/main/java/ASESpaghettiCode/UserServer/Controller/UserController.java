package ASESpaghettiCode.UserServer.Controller;

<<<<<<< HEAD
import ASESpaghettiCode.UserServer.Model.Note;
=======
>>>>>>> develop
import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import ASESpaghettiCode.UserServer.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${NoteServerLocation}")
    private String NoteServerLocation;

    @Autowired
    private RestTemplate restTemplate;



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@RequestBody User user){
        userService.save(user);
    }


    @RequestMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String getUser(@PathVariable("userId") String userId) {
        return  "This is the user"+userId+"'s profile";
    }

    @RequestMapping("/{userId}/notes")
    @ResponseStatus(HttpStatus.OK)
    public List<Note> getUserNotes(@PathVariable("userId") String userId) {

        // get users info (including basic info, list of id of published notes, list of id of liked notes) with userId
        List<Integer> publisedNotesIDs = Arrays.asList(101, 102, 103);//only hardcoded for the example

        // For each noteId, call the Note Server(by default localhost:8082) and get details
        List<Note> publishedList;
        publishedList = publisedNotesIDs.stream().map(publisedNotesID->{
            Note note = restTemplate.getForObject(NoteServerLocation + publisedNotesID,Note.class);
            return note;
            }).collect((Collectors.toList()));

        return  publishedList;


=======
import java.util.Arrays;
import java.util.List;
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
>>>>>>> develop
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

//    // this template is used for sending restful request to travelNoteServer
//    @Value("${NoteServerLocation}")// can be set in resources/application.properties or Dockerfile or Dockercompose.yml
//    private String NoteServerLocation;
//
//    @Autowired //This Bean is injected in UserServerApplication
//    private RestTemplate restTemplate;
//
//    @RequestMapping("/{userId}/notes")
//    @ResponseStatus(HttpStatus.OK)
//    public List<Note> getUserNotes(@PathVariable("userId") String userId) {
//
//        // get users info (including basic info, list of id of published notes, list of id of liked notes) with userId
//        List<Integer> publisedNotesIDs = Arrays.asList(101, 102, 103);//only hardcoded for the example
//
//        // For each noteId, call the Note Server(by default localhost:8082) and get details
//        List<Note> publishedList;
//        publishedList = publisedNotesIDs.stream().map(publisedNotesID->{
//            Note note = restTemplate.getForObject(NoteServerLocation + publisedNotesID,Note.class);
//            return note;
//        }).collect((Collectors.toList()));
//
//        return  publishedList;
//    }
}
