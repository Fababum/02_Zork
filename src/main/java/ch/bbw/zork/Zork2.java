package ch.bbw.zork;

import java.io.IOException;
import java.io.File;

public class Zork2 {

	public static void main(String[] args) {
		try {
			// On Windows we try to open the game in a separate console window
			// so players get a dedicated terminal for gameplay. On other
			// platforms we simply start the game in the current console.
			String os = System.getProperty("os.name").toLowerCase();
			
			if (os.contains("win")) {
				// Build a command that opens a new cmd.exe and runs the
				// GameLauncher class. This keeps the UI separate from the
				// IDE/terminal that started the process.
				String javaHome = System.getProperty("java.home");
				String javaBin = javaHome + "\\bin\\java.exe";
				String classesPath = new File("target/classes").getAbsolutePath();
				
				ProcessBuilder pb = new ProcessBuilder(
					"cmd.exe", "/c", "start", 
					"Zork Escape Game",
					"cmd.exe", "/k",
					javaBin, "-cp", classesPath,
					"ch.bbw.zork.GameLauncher"
				);
				pb.directory(new File(System.getProperty("user.dir")));
				pb.start();
				System.out.println("===========================================");
				System.out.println("Game window opened in separate window!");
				System.out.println("You can now close this VS Code terminal.");
				System.out.println("===========================================");
			} else {
				// Non-Windows fallback: run the game in the current terminal
				Game zorkgame = new Game();
				zorkgame.play();
			}
		} catch (IOException e) {
			// If anything goes wrong while trying to open a new window we
			// simply fall back to running the game in the current console.
			System.err.println("Could not open new window. Starting in current console...");
			Game zorkgame = new Game();
			zorkgame.play();
		}
	}

}

