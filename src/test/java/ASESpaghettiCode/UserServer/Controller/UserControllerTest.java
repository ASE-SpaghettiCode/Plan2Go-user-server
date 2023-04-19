package ASESpaghettiCode.UserServer.Controller;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import ASESpaghettiCode.UserServer.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllUsersTest() throws Exception {
        User user = new User("username", "123", "1");
        List<User> allUsers = List.of(user);

        given(userService.getUsers()).willReturn(allUsers);

        MockHttpServletRequestBuilder getRequest = get("/users")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())))
                .andExpect(jsonPath("$[0].token", is(user.getToken())));
    }

    @Test
    void createUserTest() throws Exception {
        User user = new User("username", "123", "1");

        given(userService.createUser(Mockito.any()))
                .willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    @Test
    void createUserConflictTest() throws Exception {
        User user = new User("username", "123", "1");
        given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    void longinTest() throws Exception {
        User user = new User("username", "123", "1");
        given(userService.loginUser(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/users/checking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    @Test
    void logoutTest() throws Exception {
        doNothing().when(userService).logoutUser(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/users/checking/1");

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void getUserTest() throws Exception {
        User user = new User("username", "123", "1");

        given(userService.getUserById(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/users/1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.password", is(user.getPassword())))
                .andExpect(jsonPath("$.token", is(user.getToken())));
    }

    @Test
    void editUserTest() throws Exception {
        User user = new User("username", "123", "1");

        doNothing().when(userService).editUser(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void editUserPasswordTest() throws Exception {
        User user = new User("username", "123", "1");

        doNothing().when(userService).editUser(Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/users/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    void userFollowTest() throws Exception {
        given(userService.userFollowsUser(Mockito.any(String.class), Mockito.any(String.class))).willReturn(true);

        MockHttpServletRequestBuilder postRequest = post("/users/1/follows/users/1");

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void userUnfollowTest() throws Exception {
        given(userService.userUnfollowsUser(Mockito.any(String.class), Mockito.any(String.class))).willReturn(true);

        MockHttpServletRequestBuilder deleteRequest = delete("/users/1/follows/users/1");

        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void user1FollowUser2Test() throws Exception {
        given(userService.user1FollowUser2(Mockito.any(String.class), Mockito.any(String.class))).willReturn(true);

        MockHttpServletRequestBuilder getRequest = get("/users/1/follows/1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void getFollowersTest() throws Exception {
        User user = new User("username", "123", "1");
        List<User> followersList = List.of(user);
        given(userService.getFollowersById(Mockito.any(String.class))).willReturn(followersList);

        MockHttpServletRequestBuilder getRequest = get("/users/1/followers");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())))
                .andExpect(jsonPath("$[0].token", is(user.getToken())));
    }

    @Test
    void getFollowingTest() throws Exception {
        User user = new User("username", "123", "1");
        List<User> followingList = List.of(user);
        given(userService.getFollowingsById(Mockito.any(String.class))).willReturn(followingList);

        MockHttpServletRequestBuilder getRequest = get("/users/1/followings");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user.getPassword())))
                .andExpect(jsonPath("$[0].token", is(user.getToken())));
    }

    @Test
    void getLikedList() throws Exception{
        List<String> likedList = Arrays.asList("1","2","3");

        given(userService.getLikedNotes(Mockito.any(String.class))).willReturn(likedList);

        MockHttpServletRequestBuilder getRequest = get("/users/1/likes");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$",is(likedList)));
    }

    private String asJsonString(final Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }
}
