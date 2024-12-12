package br.com.alunra.Screenmtach;

import br.com.alunra.Screenmtach.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmtachApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmtachApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
	}
}
