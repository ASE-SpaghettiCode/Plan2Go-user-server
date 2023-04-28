package ASESpaghettiCode.UserServer.Service;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;

import ASESpaghettiCode.UserServer.Websocket.NotificationModel.Notification;
import ASESpaghettiCode.UserServer.Websocket.NotificationService;
import com.mongodb.client.*;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import org.slf4j.Logger;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private MongoClient mongoClient;
    private MongoCollection<Document> notesCollection;
    private NotificationService notificationService;


    @Autowired
    public UserService(UserRepository userRepository,NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        //connect to the mongodb running on the same machine as your java application
        MongoClient mongoClient=MongoClients.create();
        MongoDatabase database=mongoClient.getDatabase("spaghetticode");
        notesCollection = database.getCollection("note");
    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }
    // register
    public User createUser(User newUser) {
        String defaultImage="https://res.cloudinary.com/drlkip0yc/image/upload/v1679279539/fake-profile-photo_qess5v.jpg";
        newUser.setToken(UUID.randomUUID().toString());
        List<String> followers = new ArrayList<>();
        List<String> followings = new ArrayList<>();
        checkIfUserExists(newUser);
        newUser.setFollowers(followers);
        newUser.setFollowings(followings);
        newUser.setImageLink(defaultImage);
        newUser = userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage, "username", "is"));
        }
    }

    // login
    public User loginUser(User user) {
        user = checkIfPasswordWrong(user);
        user.setToken(UUID.randomUUID().toString());

        return user;
    }

    private User checkIfPasswordWrong(User userToBeLoggedIn) {

        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username may not exist!!");
        }
        else if (!userByUsername.getPassword().equals(userToBeLoggedIn.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password Incorrect!");
        }
        else {
            return userByUsername;
        }
    }

    public User getUserById(String userId) {
        Optional<User> checkUser = Optional.ofNullable(userRepository.findByUserId(userId));
        if (checkUser.isPresent()) {
            return checkUser.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!");
        }
    }

    // logout
    public void logoutUser(User user) {
        user.setToken("");
    }

    //edit profile
    public void editUser(User userInput){
        if(!userRepository.existsById(userInput.getUserId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user does not exists");
        }

        User editeduser=getUserById(userInput.getUserId());

        if(userInput.getUsername().equals(editeduser.getUsername())){
            editeduser.setIntro(userInput.getIntro());
//            editeduser.setPassword(userInput.getPassword());
            editeduser.setImageLink(userInput.getImageLink());
        }else if(userRepository.findByUsername(userInput.getUsername())==null){
            editeduser.setUsername(userInput.getUsername());
            editeduser.setIntro(userInput.getIntro());
//            editeduser.setPassword(userInput.getPassword());
            editeduser.setImageLink(userInput.getImageLink());
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"username exists");
        }

        userRepository.save(editeduser);

    }

    public void editUserPassword(User userInput){
        if(!userRepository.existsById(userInput.getUserId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user does not exists");
        }

        User editeduser=getUserById(userInput.getUserId());
        editeduser.setPassword(userInput.getPassword());

        userRepository.save(editeduser);
    }

    public boolean userFollowsUser(String userId1, String userId2) {
        Optional<User> user1 = userRepository.findById(userId1);
        Optional<User> user2 = userRepository.findById(userId2);
        if (user1.isEmpty() || user2.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not found!");
        }
        if (!user2.get().getFollowers().contains(userId1) && !user1.get().getFollowings().contains(userId2)) {
            user2.get().addFollowers(userId1);
            user1.get().addFollowings(userId2);
        }
        userRepository.save(user2.get());
        userRepository.save(user1.get());

        //set notification to user1
        Notification notification = new Notification();
        notification.setActorId(userId1);
        notification.setActorName(user1.get().username);
        notification.setMethod("follow");
        notification.setOwnerId(userId2);
        notificationService.create(notification);
        return true;
    }

    public boolean userUnfollowsUser(String userId1, String userId2) {
        Optional<User> user1 = userRepository.findById(userId1);
        Optional<User> user2 = userRepository.findById(userId2);
        if (user1.isEmpty() || user2.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not found!");
        }
        if (user2.get().getFollowers().contains(userId1) && user1.get().getFollowings().contains(userId2)) {
            user2.get().removeFollowers(userId1);
            user1.get().removeFollowings(userId2);
        }
        userRepository.save(user2.get());
        userRepository.save(user1.get());
        return true;
    }

    public boolean user1FollowUser2(String userId1, String userId2) {
        Optional<User> user1 = userRepository.findById(userId1);
        Optional<User> user2 = userRepository.findById(userId2);
        if (user1.isEmpty() || user2.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not found!");
        }
        return user1.get().getFollowings().contains(userId2);
    }

    public List<User> getFollowersById(String userId) {
        Optional<User> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not found!");
        }
        List<User> result = new ArrayList<>();
        for (String id : targetUser.get().getFollowers()) {
            User follower = userRepository.findByUserId(id);
            result.add(follower);
        }
        return result;
    }

    public List<User> getFollowingsById(String userId) {
        Optional<User> targetUser = userRepository.findById(userId);
        if (targetUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is not found!");
        }
        List<User> result = new ArrayList<>();
        for (String id : targetUser.get().getFollowings()) {
            User follower = userRepository.findByUserId(id);
            result.add(follower);
        }
        return result;
    }

    public List<String> getLikedNotes(String userId){
        List<String> likedNotes = new ArrayList<>();
        Document query =new Document("likedUsers",userId);

        FindIterable<Document> results = notesCollection.find(query);
        for(Document doc : results){
            String noteId = doc.getObjectId("_id").toString();
            likedNotes.add(noteId);
        }
        return likedNotes;
    }
}
