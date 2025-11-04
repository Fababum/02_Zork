package ch.bbw.zork;

import java.util.ArrayList;

/**
 * Class Game - the main class of the "Zork" game.
 *
 * Author:  Michael Kolling, 1.1, March 2000
 * refactoring: Rinaldo Lanza, September 2020
 */

public class Game {

	private Parser parser;
	private Room currentRoom;
	private Room empfangshalle, flurEG, flurOG, bibliothek, bueroChef, tresorRaum;
	private Room sicherheitsraum, ueberwachungsraum, cafeteria, kueche, lagerraum;
	private Room keller, heizungskeller, versteck, dachboden, aussenbereich;
	private ArrayList<Item> inventory;
	private int movesLeft;
	private static final int MAX_MOVES_PER_ROUND = 2;

	public Game() {

		parser = new Parser(System.in);
		inventory = new ArrayList<>();
		movesLeft = MAX_MOVES_PER_ROUND;

		// create rooms
		empfangshalle = new Room("Reception Hall - large entrance hall with reception desk");
		flurEG = new Room("Ground Floor Corridor - main corridor on the ground floor");
		flurOG = new Room("Upper Floor Corridor - corridor on the upper floor");
		bibliothek = new Room("Library - large library with many hiding places");
		bueroChef = new Room("Boss Office - boss's office with valuable items");
		tresorRaum = new Room("Vault Room - heavily secured room with main vault (requires key)");
		sicherheitsraum = new Room("Security Room - room with security systems");
		ueberwachungsraum = new Room("Surveillance Room - room with surveillance monitors");
		cafeteria = new Room("Cafeteria - employee cafeteria");
		kueche = new Room("Kitchen - kitchen with utensils");
		lagerraum = new Room("Storage Room - storage room full of boxes and cabinets");
		keller = new Room("Cellar - dark cellar area");
		heizungskeller = new Room("Boiler Room - boiler room with many pipes");
		versteck = new Room("Hideout - secret hideout in the cellar");
		dachboden = new Room("Attic - dusty attic (requires key)");
		aussenbereich = new Room("Outside Area - area outside the building");

		// initialise room exits (north, east, south, west)
		// Ebene Dachboden
		dachboden.setExits(null, null, bibliothek, null);
		
		// Ebene OG
		bibliothek.setExits(null, flurOG, keller, null);
		flurOG.setExits(null, bueroChef, flurEG, bibliothek);
		bueroChef.setExits(null, tresorRaum, null, flurOG);
		tresorRaum.setExits(null, null, sicherheitsraum, bueroChef);
		
		// Ebene Empfangshalle/Sicherheitsraum
		empfangshalle.setExits(null, null, cafeteria, flurEG);
		sicherheitsraum.setExits(null, null, ueberwachungsraum, tresorRaum);
		
		// Ebene EG
		keller.setExits(bibliothek, flurEG, heizungskeller, null);
		flurEG.setExits(flurOG, empfangshalle, kueche, keller);
		cafeteria.setExits(empfangshalle, ueberwachungsraum, aussenbereich, kueche);
		ueberwachungsraum.setExits(sicherheitsraum, null, null, cafeteria);
		
		// Ebene Küche/Heizungskeller
		heizungskeller.setExits(keller, kueche, versteck, null);
		kueche.setExits(flurEG, cafeteria, lagerraum, heizungskeller);
		
		// Ebene Keller unten
		versteck.setExits(heizungskeller, lagerraum, null, null);
		lagerraum.setExits(kueche, aussenbereich, null, versteck);
		aussenbereich.setExits(null, null, null, lagerraum);

		currentRoom = empfangshalle; // start game in Empfangshalle
		
		// add hideables to all rooms
		empfangshalle.addHideable(new Hideable("desk", "Large reception desk with space underneath"));
		flurEG.addHideable(new Hideable("closet", "Large storage closet"));
		flurOG.addHideable(new Hideable("curtains", "Floor-to-ceiling heavy curtains"));
		bibliothek.addHideable(new Hideable("bookshelf", "Tall bookshelf with space behind it"));
		bueroChef.addHideable(new Hideable("wardrobe", "Large wooden wardrobe"));
		tresorRaum.addHideable(new Hideable("vault", "Space behind the large vault door"));
		sicherheitsraum.addHideable(new Hideable("cabinet", "Large equipment cabinet"));
		ueberwachungsraum.addHideable(new Hideable("desk", "Control desk with space underneath"));
		cafeteria.addHideable(new Hideable("counter", "Long counter with space behind it"));
		kueche.addHideable(new Hideable("pantry", "Walk-in storage pantry"));
		lagerraum.addHideable(new Hideable("boxes", "Large stack of boxes"));
		keller.addHideable(new Hideable("corner", "Dark shadowy corner"));
		heizungskeller.addHideable(new Hideable("pipes", "Behind the large heating pipes"));
		versteck.addHideable(new Hideable("alcove", "Hidden wall alcove"));
		dachboden.addHideable(new Hideable("trunk", "Large old trunk - big enough to fit inside"));
		aussenbereich.addHideable(new Hideable("bushes", "Dense bushes"));
		
		// add searchables to rooms (some with items)
		empfangshalle.addSearchable(new Searchable("drawer", "Reception desk drawer", null));
		flurEG.addSearchable(new Searchable("shelf", "Dusty shelf", null));
		flurOG.addSearchable(new Searchable("cabinet", "Wall cabinet", null));
		bibliothek.addSearchable(new Searchable("books", "Old books on shelf", null));
		bueroChef.addSearchable(new Searchable("drawer", "Boss's desk drawer", new Item("key", "Small brass key")));
		tresorRaum.addSearchable(new Searchable("safe", "Emergency safe", null));
		sicherheitsraum.addSearchable(new Searchable("locker", "Security locker", null));
		sicherheitsraum.addSearchable(new Searchable("vent", "Air vent in the wall - looks sealed", null, true));
		ueberwachungsraum.addSearchable(new Searchable("desk", "Control desk", null));
		cafeteria.addSearchable(new Searchable("cabinet", "Kitchen cabinet", null));
		kueche.addSearchable(new Searchable("drawer", "Kitchen drawer", new Item("crowbar", "A heavy crowbar - useful for breaking things")));
		lagerraum.addSearchable(new Searchable("crate", "Wooden crate", null));
		keller.addSearchable(new Searchable("shelves", "Metal shelves", null));
		heizungskeller.addSearchable(new Searchable("toolbox", "Old toolbox", null));
		versteck.addSearchable(new Searchable("chest", "Hidden chest", null));
		dachboden.addSearchable(new Searchable("boxes", "Dusty boxes", null));
		aussenbereich.addSearchable(new Searchable("bin", "Trash bin", null));
	}


	/**
	 *  Main play routine.  Loops until end of play.
	 */
	public void play() {
		printWelcome();

		// Enter the main command loop.  Here we repeatedly read commands and
		// execute them until the game is over.
		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Thank you for playing.  Good bye.");
	}

	private void printWelcome() {
		System.out.println();
		System.out.println("Welcome to Zork!");
		System.out.println("Zork is a simple adventure game.");
		System.out.println();
		System.out.println("=== COMMANDS ===");
		System.out.println("Movement (costs 1 action):");
		System.out.println("  go <direction>     - Move to another room (north/south/east/west)");
		System.out.println("  hide <object>      - Hide in an object");
		System.out.println("  search <object>    - Search an object for items");
		System.out.println();
		System.out.println("Free actions (no cost):");
		System.out.println("  take <item>        - Pick up an item");
		System.out.println("  inventory          - Show your items");
		System.out.println("  help               - Show help");
		System.out.println("  quit               - Exit the game");
		System.out.println();
		System.out.println("You have " + MAX_MOVES_PER_ROUND + " actions per round!");
		System.out.println("================");
		System.out.println();
		System.out.println(currentRoom.longDescription());
		System.out.println();
		System.out.println("Actions remaining: " + movesLeft);
	}

	private boolean processCommand(Command command) {
		if (command.isUnknown()) {
			System.out.println("I don't know what you mean...");
			return false;
		}

		String commandWord = command.getCommandWord();
		if (commandWord.equals("help")) {
			printHelp();

		} else if (commandWord.equals("go")) {
			if (movesLeft > 0) {
				goRoom(command);
				movesLeft--;
				showMovesLeft();
			} else {
				System.out.println("No actions left this round!");
				resetRound();
			}

		} else if (commandWord.equals("take")) {
			takeItem(command);
			
		} else if (commandWord.equals("inventory")) {
			showInventory();
			
		} else if (commandWord.equals("hide")) {
			if (movesLeft > 0) {
				hideInRoom(command);
				movesLeft--;
				showMovesLeft();
			} else {
				System.out.println("No actions left this round!");
				resetRound();
			}
			
		} else if (commandWord.equals("search")) {
			if (movesLeft > 0) {
				searchObject(command);
				movesLeft--;
				showMovesLeft();
			} else {
				System.out.println("No actions left this round!");
				resetRound();
			}

		} else if (commandWord.equals("quit")) {
			if (command.hasSecondWord()) {
				System.out.println("Quit what?");
			} else {
				return true; // signal that we want to quit
			}
		}
		return false;
	}

	private void printHelp() {
		System.out.println("You are lost. You are alone. You wander");
		System.out.println("around in a mysterious building.");
		System.out.println();
		System.out.println("Your available commands are:");
		System.out.println("  " + parser.showCommands());
	}

	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Go where?");
		} else {

			String direction = command.getSecondWord();

			// Try to leave current room.
			Room nextRoom = currentRoom.nextRoom(direction);

			if (nextRoom == null)
				System.out.println("There is no door!");
			else {
				currentRoom = nextRoom;
				System.out.println(currentRoom.longDescription());
			}
		}
	}
	
	private void takeItem(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Take what?");
			return;
		}
		
		String itemName = command.getSecondWord();
		Item item = currentRoom.removeItem(itemName);
		
		if (item == null) {
			System.out.println("There is no " + itemName + " here!");
		} else {
			inventory.add(item);
			System.out.println("You picked up the " + item.getName() + ".");
		}
	}
	
	private void showInventory() {
		if (inventory.isEmpty()) {
			System.out.println("You are carrying nothing.");
		} else {
			System.out.println("You are carrying:");
			for (Item item : inventory) {
				System.out.println("  " + item.getName() + " - " + item.getDescription());
			}
		}
	}
	
	private void hideInRoom(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Hide where?");
			return;
		}
		
		String hideableName = command.getSecondWord();
		Hideable hideable = currentRoom.getHideable(hideableName);
		
		if (hideable == null) {
			System.out.println("You cannot hide in " + hideableName + " here!");
		} else {
			System.out.println("You hide in the " + hideable.getName() + " - " + hideable.getDescription());
			System.out.println("You feel safer here...");
		}
	}
	
	private void searchObject(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Search what?");
			return;
		}
		
		String searchableName = command.getSecondWord();
		Searchable searchable = currentRoom.getSearchable(searchableName);
		
		if (searchable == null) {
			System.out.println("You cannot search " + searchableName + " here!");
		} else {
			// Special handling for vent
			if (searchable.isVent()) {
				handleVent(searchable);
				return;
			}
			
			if (searchable.isSearched()) {
				System.out.println("You already searched the " + searchable.getName() + ". Nothing else here.");
			} else {
				searchable.setSearched(true);
				System.out.println("You search the " + searchable.getName() + "...");
				
				Item item = searchable.getItem();
				if (item != null) {
					inventory.add(item);
					System.out.println("You found: " + item.getName() + " - " + item.getDescription());
				} else {
					System.out.println("You find nothing useful.");
				}
			}
		}
	}
	
	private void handleVent(Searchable vent) {
		if (currentRoom != sicherheitsraum) {
			System.out.println("There is no vent here.");
			return;
		}
		
		boolean hasCrowbar = false;
		for (Item item : inventory) {
			if (item.getName().equalsIgnoreCase("crowbar")) {
				hasCrowbar = true;
				break;
			}
		}
		
		if (!hasCrowbar) {
			System.out.println("The vent is sealed shut with metal screws. You need something to break it open.");
		} else {
			System.out.println("You use the crowbar to pry open the vent cover...");
			System.out.println("The vent is big enough to crawl through!");
			System.out.println("You crawl through the vent shaft...");
			currentRoom = tresorRaum;
			System.out.println();
			System.out.println(currentRoom.longDescription());
		}
	}
	
	private void showMovesLeft() {
		if (movesLeft == 0) {
			System.out.println();
			System.out.println("=== NEW ROUND ===");
			movesLeft = MAX_MOVES_PER_ROUND;
		}
		System.out.println("Actions remaining: " + movesLeft);
	}
	
	private void resetRound() {
		System.out.println();
		System.out.println("=== NEW ROUND ===");
		movesLeft = MAX_MOVES_PER_ROUND;
		System.out.println("Actions remaining: " + movesLeft);
		System.out.println();
	}
}
