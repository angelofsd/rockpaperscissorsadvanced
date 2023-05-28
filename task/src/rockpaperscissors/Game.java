package rockpaperscissors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Game {
    private String input; // User's current input
    private final Scanner kb; // Scanner for user input
    private String[] options; // List of options for game (rock, paper, scissors, etc.)
    private int score; // Current score
    private final Map<String, Integer> ratingData; // Mapping of user's rating from file

    public Game() {
        this.input = "";
        this.kb = new Scanner(System.in);
        this.score = 0;
        this.ratingData = new HashMap<>();
    }

    // Main function to start and play the game
    public void intro() {
        String name = greeting(); // Get user's name
        loadRating(name); // Load user's rating

        // Ask user for game options
        System.out.print("Enter game options separated by commas or press enter for default options: ");
        String optionsInput = kb.nextLine().trim();

        // If no options given, use default, otherwise split the options given by user
        if (optionsInput.isEmpty()) {
            options = new String[]{"rock", "paper", "scissors"};
        } else {
            options = optionsInput.split(",");
        }

        System.out.println("Okay, let's start");
        play(name); // Start playing the game
        System.out.println("Bye!");
    }

    // Load rating from file
    private void loadRating(String name) {
        File file = new File("rating.txt");
        try (Scanner scanner = new Scanner(file)) {
            // Read each line from the file and add it to the ratingData map
            while (scanner.hasNext()) {
                String n = scanner.next();
                int s = scanner.nextInt();
                ratingData.put(n, s);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // If the user's name is in the ratingData map, set their score to their saved score
        if (ratingData.containsKey(name)) {
            score = ratingData.get(name);
        }
    }

    // Main function for playing the game
    private void play(String name) {
        // Continue playing until user enters "!exit"
        while (!this.input.equals("!exit")) {
            this.input = kb.nextLine().trim().toLowerCase();

            if (this.input.equals("!exit")) {
                break;
            }

            if (this.input.equals("!rating")) {
                // If user enters "!rating", output their current score
                System.out.println("Your rating: " + score);
            } else if (Arrays.asList(options).contains(this.input)) {
                // If user enters a valid option, randomly pick an option for the computer and determine the result
                String rc = getRandom();

                // In case of a draw
                if (this.input.equals(rc)) {
                    score += 50; // Add 50 points to score
                    System.out.printf("There is a draw (%s)%n", this.input);
                } else {
                    // If not a draw, compare the user's choice and the computer's choice
                    int result = compare(this.input, rc);

                    // User wins
                    if (result > 0) {
                        score += 100; // Add 100 points to score
                        System.out.printf("Well done. The computer chose %s and failed%n", rc);
                    } else { // User loses
                        System.out.printf("Sorry, but the computer chose %s%n", rc);
                    }
                }
            } else { // Invalid input
                System.out.println("Invalid input");
            }
        }
    }

    // Function to ask for user's name
    private String greeting() {
        System.out.print("Enter your name: ");
        String name = kb.nextLine();
        System.out.println("Hello, " + name);
        return name;
    }

    // Function to randomly pick an option for the computer
    private String getRandom() {
        Random random = new Random();
        int randChoice = random.nextInt(options.length);
        return options[randChoice];
    }

    // Function to compare the user's choice and the computer's choice
    private int compare(String ch, String rc) {
        // Get indices of user's choice and computer's choice in the options list
        int indexCh = Arrays.asList(options).indexOf(ch);
        int indexRc = Arrays.asList(options).indexOf(rc);
        int len = options.length;

        // Determine who wins based on their relative positions in the list
        if ((indexCh - indexRc + len) % len > len / 2) {
            return 1; // User's choice wins
        } else {
            return -1; // Computer's choice wins
        }
    }
}
