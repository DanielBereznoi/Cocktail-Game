package com.ridango.game;

import java.util.Scanner;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}
	
	@Autowired
	private Environment environment;

	@Override public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (!environment.getActiveProfiles()[0].equals("test")) {
			System.out.println("Welcome to Cocktail Game!");
			System.out.println("Start playing");
			String answer = scanner.nextLine();
		}
	}
}
