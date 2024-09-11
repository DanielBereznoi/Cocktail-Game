package com.ridango.game;

import java.util.Scanner;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ridango.game.dto.SessionDataDto;
import com.ridango.game.service.GameService;

import com.ridango.game.entity.Highscore;

@SpringBootApplication
public class CocktailGameApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	@Autowired
	private Environment environment;

	@Autowired
	private GameService gameService;

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (environment.getActiveProfiles().length == 0) {
			System.out.println("\n\nWelcome to Cocktail Game!");
			System.out.println("Insert your name to start playing:\n");
			String playerName = scanner.nextLine();
			SessionDataDto session = gameService.register(playerName);
			System.out.println("\nNow to guessin'");
			int currentAttemptsLeft = 0;
			while (session.getAttemptsLeft() > 0) {
				currentAttemptsLeft = session.getAttemptsLeft();
				System.out.println("\nName of cocktail: " + session.getCurrentCocktailName().replace("", " ").trim());
				System.out.println("Attempts left: " + session.getAttemptsLeft());
				System.out.println("Instructions: " + session.getCurrentRecipe());
				String category = session.getCategory();
				String[] ingredients = session.getIngredients().split(",");
				String glass = session.getGlass();
				String pictureUrl = session.getPictureURL();
				if (session.getAttemptsLeft() < 5) {
					System.out.println("\nAdditional Clues: ");
					System.out.println("Ingredients: " + String.join(", ", ingredients));
					System.out.println("Drink category: " + category);
					if (!glass.isEmpty())
						System.out.println("Drink glass type: " + glass);
					if (!pictureUrl.isEmpty())
						System.out.println("Drink picture url: " + pictureUrl);
				}
				System.out.println("\nInsert cocktail name. (With special characters)");
				String answer = scanner.nextLine();
				session = gameService.reactToAnswer(session, answer);
				if (session.getAttemptsLeft() == 0) {
					Highscore highscore = gameService.endGame(session);
					if (highscore.getScore() == session.getScore()
							&& highscore.getPlayerName().equals(session.getName())) {
						System.out.println("\nCongratulations! You beat the highscore!");
						System.out.println(
								"Da new highscore: " + highscore.getScore() + ", " + "by " + highscore.getPlayerName());
					} else {
						System.out.println("\nNo attempts left");
						System.out.println("Your score: " + session.getScore());
						System.out.println(
								"The highscore: " + highscore.getScore() + ", " + "by " + highscore.getPlayerName());
					}
					break;
				}
				if (session.getAttemptsLeft() == 5) {
					System.out.println("\nYour answer is correct. To the next round.");
					System.out.println("Your current score: " + session.getScore());
				}
				if (session.getAttemptsLeft() < currentAttemptsLeft) System.out.println("Wrong guess! Attempts left: " + session.getAttemptsLeft());
				
			}
		}
		scanner.close();
	}
}
