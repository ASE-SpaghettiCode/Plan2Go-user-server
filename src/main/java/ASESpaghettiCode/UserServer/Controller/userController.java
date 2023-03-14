package ASESpaghettiCode.UserServer.Controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class userController {
    @RequestMapping("/{userId}")
    public String getUser(@PathVariable("userId") String userId) {
        return "User server is working."+ "This is "+userId;
    }
}
