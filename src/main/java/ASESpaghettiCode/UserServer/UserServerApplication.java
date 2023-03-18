package ASESpaghettiCode.UserServer;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class UserServerApplication implements CommandLineRunner {
	@Autowired
	private UserRepository repository;

	public static void main(String[] args) {

		SpringApplication.run(UserServerApplication.class, args);
	}
	@Override
	public void run(String...args) throws Exception{
		repository.deleteAll();
		repository.save(new User("Alice","123456",""));
		repository.save(new User("Jessica","123456",""));

		System.out.println("-------------------------------");
		for (User user : repository.findAll()) {
			System.out.println(user);
		}
		System.out.println();
	}


}
