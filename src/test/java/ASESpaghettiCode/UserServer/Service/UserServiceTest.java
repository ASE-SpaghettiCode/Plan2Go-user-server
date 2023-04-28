package ASESpaghettiCode.UserServer.Service;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import ASESpaghettiCode.UserServer.Websocket.NotificationRepository;
import ASESpaghettiCode.UserServer.Websocket.NotificationService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.junit.jupiter.api.Test;
import org.bson.Document;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final MongoCollection notesCollection = mock(MongoCollection.class);
    private final NotificationService notificationService = mock(NotificationService.class);
    MongoCursor cursor = mock(MongoCursor.class);
    FindIterable<Document> iterable = mock(FindIterable.class);

    private final UserService userService = new UserService(userRepository,notificationService);

    User user1 = new User("name1", "password1", "1");
    User user2 = new User("name2", "password2", "2");
    User user3 = new User("name3", "password3", "3");

    @Test
    void getUsersTest() {
        List<User> allUsers = Arrays.asList(user1, user2, user3);
        when(userRepository.findAll()).thenReturn(allUsers);

        assertEquals(userService.getUsers(), allUsers);
    }

    @Test
    void createUserTest_Success() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(user1);

        assertEquals(userService.createUser(user1), user1);
    }

    @Test
    void createUserTest_Fail() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(user1);

        assertThrows(ResponseStatusException.class, () -> {
            userService.createUser(user1);
        });
    }

    @Test
    void loginUserTest_Success() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(user1);

        assertEquals(userService.loginUser(user1), user1);
    }

    @Test
    void loginUserTest_InvalidUsername() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user1));
    }

    @Test
    void loginUserTest_WrongPassword() {
        User user1WithWrongPassword = new User("username1", "wrongpassword", "1");
        when(userRepository.findByUsername(any(String.class))).thenReturn(user1);

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(user1WithWrongPassword));
    }

    @Test
    void getUserByIdTest_Success() {
        when(userRepository.findByUserId(any(String.class))).thenReturn(user1);

        assertEquals(userService.getUserById("1"), user1);
    }

    @Test
    void getUserByIdTest_Fail() {
        when(userRepository.findByUserId(any(String.class))).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> userService.getUserById("1"));
    }

    @Test
    void logoutUserTest() {
        User logoutUser = new User("logoutUser", "123", "4");
        userService.logoutUser(logoutUser);
        assertEquals("", logoutUser.getToken());
    }

    @Test
    void editUserTest_Success_SameUsername() {
        User userInput = new User("username1", "password1", "1");
        userInput.setIntro("My intro");
        userInput.setImageLink("new image link");
        userInput.setUserId("1");

        when(userRepository.existsById(any(String.class))).thenReturn(true);
        when(userRepository.findByUserId(any(String.class))).thenReturn(user1);
        when(userRepository.save(any(User.class))).thenReturn(userInput);

        userService.editUser(userInput);

        verify(userRepository, times(1)).existsById(any(String.class));
        verify(userRepository, times(1)).findByUserId(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void editUserTest_Success_DifferentValidUsername() {
        User userInput = new User("newUsername1", "password1", "1");
        userInput.setIntro("My intro");
        userInput.setImageLink("new image link");
        userInput.setUserId("1");

        when(userRepository.existsById(any(String.class))).thenReturn(true);
        when(userRepository.findByUserId(any(String.class))).thenReturn(user1);
        when(userRepository.findByUsername(any(String.class))).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(userInput);

        userService.editUser(userInput);

        verify(userRepository, times(1)).existsById(any(String.class));
        verify(userRepository, times(1)).findByUserId(any(String.class));
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void editUserTest_Fail_InvalidId() {
        when(userRepository.existsById(any())).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userService.editUser(user1));
    }

    @Test
    void editUserTest_Fail_UsernameExist() {
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.findByUserId(any(String.class))).thenReturn(user1);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user2);

        assertThrows(ResponseStatusException.class, () -> userService.editUser(user1));
    }

    @Test
    void editUserPassword_Success() {
        User userInput = new User("username1", "password1", "1");
        userInput.setIntro("My intro");
        userInput.setImageLink("new image link");
        userInput.setUserId("1");

        when(userRepository.existsById(any(String.class))).thenReturn(true);
        when(userRepository.findByUserId(any(String.class))).thenReturn(user1);
        when(userRepository.save(any(User.class))).thenReturn(userInput);

        userService.editUserPassword(userInput);

        verify(userRepository, times(1)).existsById(any(String.class));
        verify(userRepository, times(1)).findByUserId(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void editUserPassword_Fail() {
        when(userRepository.existsById(any())).thenReturn(true);
        when(userRepository.findByUserId(any(String.class))).thenReturn(user1);
        when(userRepository.findByUsername(any(String.class))).thenReturn(user2);

        assertThrows(ResponseStatusException.class, () -> userService.editUserPassword(user1));
    }

    @Test
    void userFollowsUserTest_Success() {
        List<String> followList = new ArrayList<>();
        user1.setUserId("1");
        user1.setFollowings(followList);
        user2.setUserId("2");
        user2.setFollowers(followList);

        when(userRepository.findById(any(String.class)))
                .thenReturn(java.util.Optional.ofNullable(user1))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(userRepository.save(any(User.class))).thenReturn(user2).thenReturn(user1);

        assertTrue(userService.userFollowsUser("1","2"));
    }

    @Test
    void userFollowsUserTest_Fail() {
        when(userRepository.findById(any(String.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.userFollowsUser("1","1"));
    }

    @Test
    void userUnfollowsUserTest_Success() {
        List<String> followingList = new ArrayList<>();
        followingList.add("2");
        List<String> followerList = new ArrayList<>();
        followerList.add("1");
        user1.setUserId("1");
        user1.setFollowings(followingList);
        user2.setUserId("2");
        user2.setFollowers(followerList);

        when(userRepository.findById(any(String.class)))
                .thenReturn(java.util.Optional.ofNullable(user1))
                .thenReturn(java.util.Optional.ofNullable(user2));
        when(userRepository.save(any(User.class))).thenReturn(user2).thenReturn(user1);

        assertTrue(userService.userUnfollowsUser("1","2"));
    }

    @Test
    void userUnfollowsUserTest_Fail() {
        when(userRepository.findById(any(String.class)))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.userFollowsUser("1","1"));
    }

    @Test
    void user1FollowUser2Test() {
        List<String> followList = new ArrayList<>();
        user1.setUserId("1");
        user1.setFollowings(followList);
        user2.setUserId("2");
        user2.setFollowers(followList);

        when(userRepository.findById(any(String.class)))
                .thenReturn(java.util.Optional.ofNullable(user1))
                .thenReturn(java.util.Optional.ofNullable(user2));

        assertFalse(userService.user1FollowUser2("1","2"));
    }

    @Test
    void getFollowersByIdTest() {
        List<String> followerList = new ArrayList<>();
        followerList.add("1");
        user1.setFollowers(followerList);
        List<User> user1FollowerList = List.of(user2);

        when(userRepository.findById(any(String.class))).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findByUserId(any(String.class))).thenReturn(user2);

        assertEquals(user1FollowerList, userService.getFollowersById("1"));
    }

    @Test
    void getFollowingsByIdTest() {
        List<String> followingList = new ArrayList<>();
        followingList.add("1");
        user1.setFollowings(followingList);
        List<User> user1FollowingList = List.of(user2);

        when(userRepository.findById(any(String.class))).thenReturn(java.util.Optional.ofNullable(user1));
        when(userRepository.findByUserId(any(String.class))).thenReturn(user2);

        assertEquals(user1FollowingList, userService.getFollowingsById("1"));
    }

    @Test
    void getLikedNotesTest() {
        Document doc = new Document();
        doc.append("_id", "1");

        when(notesCollection.find(any(Document.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true).thenReturn(false);
        when(cursor.next()).thenReturn(doc);

        List<String> likedNotes = userService.getLikedNotes("1");
        assertEquals(0,likedNotes.size());
    }

}
