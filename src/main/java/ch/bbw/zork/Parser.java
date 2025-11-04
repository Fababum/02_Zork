package ch.bbw.zork;
/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Parser {

	private CommandWords validCommandWords;
	private InputStream inputStream;

	public Parser(InputStream inputStream) {
		this.inputStream = inputStream;
		this.validCommandWords = new CommandWords();
	}

	public Command getCommand() {
		String inputLine;

		System.out.print("> ");

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
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0], tokens[1]);
					} else {
						return new Command(null, tokens[1]);
					}
				case 1:
					if (validCommandWords.isCommand(tokens[0])) {
						return new Command(tokens[0]);
					} else {
						return new Command(null);
					}
				default:
					// More than 2 words - take first as command, rest as second word
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

	public String showCommands() {
		return validCommandWords.showAll();
	}
}
