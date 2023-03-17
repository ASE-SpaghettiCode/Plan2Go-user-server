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


    }
}
