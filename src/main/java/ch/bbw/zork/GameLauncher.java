package ch.bbw.zork;

public class GameLauncher {

	public static void main(String[] args) {
		// Simple launcher: create the game and start the play loop.
		// Kept tiny so it can be invoked directly by a ProcessBuilder
		// or from an IDE.
		Game zorkgame = new Game();
		zorkgame.play();
	}

}
