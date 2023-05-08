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
public class UserServerApplication implements CommandLineRunner {

	@Autowired
	private UserRepository repository;

//	@Bean //this bean is for sending Restful request to travelNoteServer
//	public RestTemplate getRestTemplate(){
//		return new RestTemplate();
//	}

	@Bean
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServerApplication.class, args);
	}

	@Override
	public void run(String...args) throws Exception{
		repository.deleteAll();
		repository.save(new User("Alice","123456",""));
		repository.save(new User("Jessica","123456",""));

		User userForTest = new User("test","123456","");
		userForTest.setUserId("1");
		repository.save(userForTest);

		System.out.println("-------------------------------");
		for (User user : repository.findAll()) {
			System.out.println(user);
		}
		System.out.println();
	}


}
