package luccaschmitz.github.recipebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RecipeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeBackendApplication.class, args);
	}

}
