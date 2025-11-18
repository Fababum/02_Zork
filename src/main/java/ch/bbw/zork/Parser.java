package ch.bbw.zork;
/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Parser {

	// Helper that knows which words are valid commands
	private CommandWords validCommandWords;
	// Input stream the parser reads from (usually System.in)
	private InputStream inputStream;

	/**
	 * Create a parser that reads commands from the given input stream.
	 * This keeps the parser testable (we can provide any InputStream).
	 */
	public Parser(InputStream inputStream) {
		this.inputStream = inputStream;
		this.validCommandWords = new CommandWords();
	}

	/**
	 * Read a line from the input, split it into words and return a
	 * Command object. If the first word is not a known command we return
	 * a Command with a null command word so the game can handle it as
	 * an unknown command.
	 */
	public Command getCommand() {
		String inputLine;

		System.out.print("> "); // prompt

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.inputStream));
		try {
			inputLine = bufferedReader.readLine();
			
			// Handle null input (EOF)
			if (inputLine == null) {
				return new Command(null);
			}
			
			// Trim whitespace and handle empty input
			inputLine = inputLine.trim();
			if (inputLine.isEmpty()) {
				return new Command(null);
			}

			String[] tokens = inputLine.split("\\s+");
			switch(tokens.length) {
				case 2:
					// Two words: command + argument
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0], tokens[1]);
					} else {
						// Unknown first word but we still return the second word
						return new Command(null, tokens[1]);
					}
				case 1:
					// Single word line
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0]);
					} else {
						return new Command(null);
					}
				default:
					// More than 2 words: treat first as command and second as argument
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0], tokens[1]);
					} else {
						return new Command(null);
					}
			}
		} catch (java.io.IOException exc) {
			System.out.println("There was an error during reading: " + exc.getMessage());
		}
		return new Command(null);
	}

	/**
	 * Return a string with all available commands (for help output).
	 */
	public String showCommands() {
		return validCommandWords.showAll();
	}
}
