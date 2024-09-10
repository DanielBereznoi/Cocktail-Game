package com.ridango.game;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("!test")
public class CocktailGameApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	@Override public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Welcome to Cocktail Game!");
			System.out.println("Start playing");
		}
	}
}
