package ch.bbw.zork;

import java.io.IOException;
import java.io.File;

public class Zork2 {

	public static void main(String[] args) {
		try {
			// Open new console window on Windows
			String os = System.getProperty("os.name").toLowerCase();
			
			if (os.contains("win")) {
				// Windows - open new independent cmd window
				String javaHome = System.getProperty("java.home");
				String javaBin = javaHome + "\\bin\\java.exe";
				
				// Get absolute path to classes directory
				String classesPath = new File("target/classes").getAbsolutePath();
				
				ProcessBuilder pb = new ProcessBuilder(
					"cmd.exe", "/c", "start", 
					"Zork Escape Game",
					"cmd.exe", "/k",
					javaBin, "-cp", classesPath,
					"ch.bbw.zork.GameLauncher"
				);
				
				// Start in the project directory
				pb.directory(new File(System.getProperty("user.dir")));
				pb.start();
				
				System.out.println("===========================================");
				System.out.println("Game window opened in separate window!");
				System.out.println("You can now close this VS Code terminal.");
				System.out.println("===========================================");
			} else {
				// For non-Windows systems, just run normally
				Game zorkgame = new Game();
				zorkgame.play();
			}
		} catch (IOException e) {
			System.err.println("Could not open new window. Starting in current console...");
			Game zorkgame = new Game();
			zorkgame.play();
		}
	}

}

