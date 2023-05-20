package ASESpaghettiCode.UserServer;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;


@SpringBootApplication
public class UserServerApplication {

	@Autowired
	private UserRepository repository;

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServerApplication.class, args);
	}


}
