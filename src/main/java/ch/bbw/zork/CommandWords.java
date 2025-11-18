package ch.bbw.zork;

/*
 * author:  Michael Kolling, Version: 1.0, Date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.util.Arrays;
import java.util.List;


/**
 * Helper class that knows all valid command words in the game.
 *
 * The parser uses this class to check whether the first word typed by
 * the player is a recognized command. The list is small and fixed for
 * this game, and we expose a helper to print them to the user.
 */
public class CommandWords {

	// List of recognized commands. If you add a new command to the game,
	// also add it here so the parser accepts it.
	private List<String> validCommands = Arrays.asList("go", "quit", "help", "inventory", "hide", "search", "pickup", "map", "use" );

	/**
	 * Check whether a word is a valid command.
	 *
	 * @param commandWord the word to check
	 * @return true if this word is one of the known commands
	 */
	public boolean isCommand(String commandWord) {
		// We could use contains(), but stream is expressive and fine here.
		return validCommands.stream()
				.filter( c -> c.equals(commandWord) )
				.count()>0;
	}

	/**
	 * Return a space-separated list of all known commands for help output.
	 */
	public String showAll() {
		return String.join(" ", validCommands);
	}

}





