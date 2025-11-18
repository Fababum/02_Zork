package ch.bbw.zork;

/**
 * Class Command - represents a parsed player command.
 *
 * This small helper class stores a command word like "go" and an optional
 * second word like "north". The parser builds one of these for every
 * line the player types. The rest of the game only needs to ask this
 * object whether the command was recognized and to retrieve the words.
 *
 * author: Michael Kolling version: 1.0 date: July 1999
 * refactoring: Rinaldo Lanza, September 2020
 */

public class Command {

	private String commandWord;
	private String secondWord;

	public Command(String commandWord) {
		this(commandWord, null);
	}
	public Command(String commandWord, String secondWord) {
		this.commandWord = commandWord;
		this.secondWord = secondWord;
	}

	public String getCommandWord() {
		// Return the first word of the command (may be null if unknown)
		return commandWord;
	}
	public String getSecondWord() {
		// Return the rest of the command (e.g. the target or direction)
		return secondWord;
	}
	public boolean isUnknown() {
		// A command is unknown when the parser couldn't match the first word
		return (commandWord == null);
	}
	public boolean hasSecondWord() {
		// True when the command has a meaningful second word
		return (secondWord != null);
	}
}
