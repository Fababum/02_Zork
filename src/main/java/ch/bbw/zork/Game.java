package ch.bbw.zork;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Game - the main game logic for Zork Escape Game.
 *
 * This class builds the world (rooms, items, searchable objects),
 * handles the player's input loop, tracks inventory, score and the
 * guard enemy. Methods are small and descriptive so the main play
 * loop simply reads a Command and delegates to the appropriate
 * handler.
 *
 * Author:  Michael Kolling, 1.1, March 2000
 * refactoring: Rinaldo Lanza, September 2020
 */
public class Game {

	// Input parser for reading player commands from the console
	private Parser parser;
	// The room the player is currently in
	private Room currentRoom;
	// Rooms used in the game world. Variable names follow the
	// original German identifiers but the room descriptions are in English.
	private Room empfangshalle, flurEG, flurOG, bibliothek, bueroChef, tresorRaum;
	private Room sicherheitsraum, ueberwachungsraum, cafeteria, kueche, lagerraum;
	private Room keller, heizungskeller, versteck, dachboden, aussenbereich;
	private ArrayList<Item> inventory;
	private int movesLeft;
	private static final int MAX_MOVES_PER_ROUND = 2;
	private boolean tresorDoorOpen = false; // Track if the vault door to boss office is open
	private int hiddenInVersteckCount = 0; // Track how many times player hid in versteck
	private boolean teleportMachineRevealed = false; // Track if teleport machine is revealed
	private boolean isHiding = false; // Track if player is currently hiding
	private String currentHidingSpot = ""; // Track where player is hiding
	private Guard guard; // The guard enemy
	
	// Score system
	private int score = 0;
	private boolean hasDisguise = false; // One-time use to avoid guard
	private boolean hasEnergyDrink = false; // One-time use for extra action
	private boolean guardShot = false; // Track if guard has been shot

	public Game() {
		// Build parser and initial state

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
		bibliothek.setExits(dachboden, flurOG, keller, null);
		flurOG.setExits(null, bueroChef, flurEG, bibliothek);
		bueroChef.setExits(null, null, null, flurOG);
		tresorRaum.setExits(null, null, sicherheitsraum, null);
		
		// Ebene Empfangshalle/Sicherheitsraum
		empfangshalle.setExits(null, null, cafeteria, flurEG);
		sicherheitsraum.setExits(tresorRaum, null, ueberwachungsraum, null);
		
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
		
		// Setup special door: bueroChef -> tresorRaum with keycard
		Searchable tresorDoor = new Searchable("door", "A secure door to the vault - requires security access", null);
		tresorDoor.setDoor(true);
		tresorDoor.setRequiredItem("keycard");
		bueroChef.addSearchable(tresorDoor);
		
		// Setup inner door handle in tresorRaum to open door from inside
		Searchable innerHandle = new Searchable("handle", "Door handle on the inside of the vault door", null);
		innerHandle.setDoor(true);
		innerHandle.setRequiredItem("none"); // No item required from inside
		tresorRaum.addSearchable(innerHandle);
		
		// Setup password-protected safe in tresorRaum
		Searchable safe = new Searchable("safe", "Large vault safe - requires 4-digit code", new Item("gatekey", "Heavy Gate Key - opens the exit gate"));
		safe.setPasswordProtected(true);
		safe.setPassword("1984");
		
		// Setup gate in aussenbereich
		Searchable gate = new Searchable("gate", "The exit gate - locked with a heavy padlock", null);
		gate.setGate(true);
		gate.setRequiredItem("gatekey");
		
		// Setup locked attic door in bibliothek
		Searchable atticDoor = new Searchable("atticdoor", "A heavy wooden door leading to the attic - sealed shut with old boards", null);
		atticDoor.setDoor(true);
		atticDoor.setRequiredItem("crowbar");
		
		// Setup roof hatch in dachboden
		Searchable roofHatch = new Searchable("hatch", "A locked roof hatch leading outside - heavy padlock", null);
		roofHatch.setGate(true); // Reuse gate logic for escape
		roofHatch.setRequiredItem("pistol");
		
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
		empfangshalle.addSearchable(new Searchable("drawer", "Reception desk drawer", new Item("bullets", "A box of pistol ammunition")));
		flurEG.addSearchable(new Searchable("shelf", "Dusty shelf", new Item("disguise", "Security Guard Uniform - one-time use to avoid detection! (+50 points)")));
		flurOG.addSearchable(new Searchable("cabinet", "Wall cabinet", null));
		bibliothek.addSearchable(new Searchable("books", "Old books on shelf", new Item("keycard", "Security Keycard - grants access to restricted areas")));
		bibliothek.addSearchable(atticDoor); // Add locked attic door
		bueroChef.addSearchable(new Searchable("drawer", "Boss's desk drawer", null));
		tresorRaum.addSearchable(safe);
		sicherheitsraum.addSearchable(new Searchable("locker", "Security locker", new Item("pistol", "A small pistol - needs ammunition")));
		sicherheitsraum.addSearchable(new Searchable("vent", "Air vent in the wall - looks sealed", null, true));
		ueberwachungsraum.addSearchable(new Searchable("desk", "Control desk", new Item("energydrink", "Energy Drink - one-time use for +1 extra action! (+30 points)")));
		cafeteria.addSearchable(new Searchable("cabinet", "Kitchen cabinet", null));
		kueche.addSearchable(new Searchable("drawer", "Kitchen drawer", new Item("crowbar", "A heavy crowbar - useful for breaking things")));
		lagerraum.addSearchable(new Searchable("crate", "Wooden crate", null));
		keller.addSearchable(new Searchable("shelves", "Metal shelves", null));
		heizungskeller.addSearchable(new Searchable("toolbox", "Old toolbox with a piece of paper inside", new Item("code", "Paper with code - '1984'")));
		versteck.addSearchable(new Searchable("chest", "Hidden chest", null));
		dachboden.addSearchable(new Searchable("boxes", "Dusty boxes", null));
		dachboden.addSearchable(roofHatch); // Add roof escape route
		aussenbereich.addSearchable(new Searchable("bin", "Trash bin", null));
		aussenbereich.addSearchable(gate);
		
		// add notes to rooms
		empfangshalle.addNote(new Note("note", "A note on the reception desk - 'Security clearance badges are kept in the boss's office'"));
		bueroChef.addNote(new Note("note", "A note on the desk - 'He enjoys reading during breaks'"));
		sicherheitsraum.addNote(new Note("note", "A note pinned to the wall - 'Kitchen inventory check overdue'"));
		tresorRaum.addNote(new Note("note", "A sticky note on the vault - 'Remember: The code is hidden in the boiler room'"));
		dachboden.addNote(new Note("note", "A dusty note - 'Emergency exit: Roof hatch locked with heavy padlock. Force may be necessary.'"));
		
		// Initialize the guard - starts in storage room, moves randomly
		guard = new Guard("Security Guard", lagerraum);
		guard.setForbiddenRoom(tresorRaum); // Guard cannot enter vault room
		guard.setForbiddenRoom(dachboden); // Guard cannot access attic
	}


	/**
	 * Start the main game loop: print welcome text, then repeatedly read
	 * a command and process it until the player quits or the game ends.
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

	/**
	 * Print a friendly welcome screen with quick instructions and the
	 * initial room description so the player can get started.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("╔════════════════════════════════════════╗");
		System.out.println("║     WELCOME TO ZORK ESCAPE GAME        ║");
		System.out.println("╚════════════════════════════════════════╝");
		System.out.println();
		System.out.println("You wake up locked inside a mysterious building.");
		System.out.println("Your goal: ESCAPE before the guard catches you!");
		System.out.println();
		System.out.println("┌─ STORY ────────────────────────────────┐");
		System.out.println("│ A Security Guard patrols the building. │");
		System.out.println("│ If he finds you: GAME OVER!            │");
		System.out.println("│ Hide to avoid detection.               │");
		System.out.println("└────────────────────────────────────────┘");
		System.out.println();
		System.out.println("┌─ QUICK START ──────────────────────────┐");
		System.out.println("│ - Find notes - they contain clues!     │");
		System.out.println("│ - Search for items                     │");
		System.out.println("│ - Hide when the Guard is near          │");
		System.out.println("│ - Unlock the exit gate to escape       │");
		System.out.println("│ - Earn points for your actions!        │");
		System.out.println("│   Check 'inventory' to see your score  │");
		System.out.println("└────────────────────────────────────────┘");
		System.out.println();
		System.out.println("BASIC COMMANDS:");
		System.out.println("  go <direction>  - Move around (north/south/east/west)");
		System.out.println("  search <object> - Search for items");
		System.out.println("  hide <object>   - Hide from the guard");
		System.out.println("  pickup <note>   - Read notes");
		System.out.println("  inventory       - Check items & score");
		System.out.println("  map             - Show building map");
		System.out.println("  help            - Full command list");
		System.out.println();
		System.out.println("Type 'help' for detailed instructions.");
		System.out.println("────────────────────────────────────────");
		System.out.println();
		System.out.println(currentRoom.longDescription());
		System.out.println();
		System.out.println("Actions remaining: " + movesLeft);
		System.out.println();
	}

	/**
	 * Process a single player command and perform the requested action.
	 * Returns true when the player requested to quit the game.
	 */
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
			
		} else if (commandWord.equals("inventory")) {
			showInventory();
			
		} else if (commandWord.equals("hide")) {
			if (movesLeft > 0) {
				movesLeft--;
				hideInRoom(command);
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
			
		} else if (commandWord.equals("pickup")) {
			pickupNote(command);

		} else if (commandWord.equals("map")) {
			showMap();

		} else if (commandWord.equals("use")) {
			useItem(command);

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
		System.out.println();
		System.out.println("========================================");
		System.out.println("           HELP - COMMANDS");
		System.out.println("========================================");
		System.out.println();
		System.out.println("=== OBJECTIVE ===");
		System.out.println("Find the exit gate and unlock it to escape!");
		System.out.println("Avoid the Security Guard - he moves every round!");
		System.out.println();
		System.out.println("=== ACTIONS (Cost: 1 action point) ===");
		System.out.println("  go <direction>");
		System.out.println("    - Move to another room");
		System.out.println("    - Directions: north, south, east, west");
		System.out.println("    - Example: go north");
		System.out.println();
		System.out.println("  search <object>");
		System.out.println("    - Search an object for items");
		System.out.println("    - Example: search drawer");
		System.out.println("    - Some objects need special items to open!");
		System.out.println();
		System.out.println("  hide <object>");
		System.out.println("    - Hide in an object (costs 1 action)");
		System.out.println("    - Example: hide desk");
		System.out.println("    - While hiding:");
		System.out.println("      1. Stay hidden - costs 1 additional action");
		System.out.println("      2. Exit - FREE, no action cost");
		System.out.println("    - Hiding protects you from the Guard!");
		System.out.println();
		System.out.println("=== FREE ACTIONS (No cost) ===");
		System.out.println("  pickup <note>");
		System.out.println("    - Pick up and read a note");
		System.out.println("    - Example: pickup note");
		System.out.println("    - Notes contain important clues!");
		System.out.println();
		System.out.println("  inventory");
		System.out.println("    - Show items and current score");
		System.out.println();
		System.out.println("  map");
		System.out.println("    - Show the building map");
		System.out.println();
		System.out.println("  use <item>");
		System.out.println("    - Use a powerup item");
		System.out.println("    - Example: use disguise, use energydrink");
		System.out.println();
		System.out.println("  help");
		System.out.println("    - Show this help message");
		System.out.println();
		System.out.println("  quit");
		System.out.println("    - Exit the game");
		System.out.println();
		System.out.println("=== SCORING ===");
		System.out.println("- Find items: +10 points");
		System.out.println("- Crack safe: +20 points");
		System.out.println("- Pick up notes: +5 points");
		System.out.println("- Hide from Guard: +5 points per encounter");
		System.out.println("- Use disguise: +50 points");
		System.out.println("- Use energy drink: +30 points");
		System.out.println("- Escape via gate: +100 points");
		System.out.println("- Escape via teleport: +150 points");
		System.out.println();
		System.out.println("=== TIPS ===");
		System.out.println("- Read all notes - they have important hints!");
		System.out.println("- Search everything you can!");
		System.out.println("- Hide when the Guard is nearby!");
		System.out.println("- You have " + MAX_MOVES_PER_ROUND + " actions per round");
		System.out.println("- Guard moves after every round!");
		System.out.println("- Look for secret easter eggs...");
		System.out.println();
		System.out.println("========================================");
	}

	/**
	 * Move the player in the direction provided by the command. Handles
	 * special cases like locked vents and updates the currentRoom if the
	 * move succeeds. It also checks for guard encounters after moving.
	 */
	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Go where?");
		} else {

			String direction = command.getSecondWord();

			// Special check: prevent going south from tresorRaum to sicherheitsraum without crowbar
			if (currentRoom == tresorRaum && direction.equalsIgnoreCase("south")) {
				boolean hasCrowbar = false;
				for (Item item : inventory) {
					if (item.getName().equalsIgnoreCase("crowbar")) {
						hasCrowbar = true;
						break;
					}
				}
				
				if (!hasCrowbar) {
					System.out.println("The vent is sealed from this side. You cannot go through without the crowbar.");
					return;
				}
			}

			// Try to leave current room.
			Room nextRoom = currentRoom.nextRoom(direction);

			if (nextRoom == null)
				System.out.println("There is no door!");
			else {
				currentRoom = nextRoom;
				System.out.println(currentRoom.longDescription());
				checkGuardEncounter(); // Check if guard is in new room
			}
		}
	}
	
	private void showInventory() {
		System.out.println("========================================");
		System.out.println("Score: " + score + " points");
		System.out.println("========================================");
		if (inventory.isEmpty()) {
			System.out.println("You are carrying nothing.");
		} else {
			System.out.println("You are carrying:");
			for (Item item : inventory) {
				System.out.println("  " + item.getName() + " - " + item.getDescription());
			}
		}
		if (hasDisguise) {
			System.out.println();
			System.out.println("Active Power-up: Disguise (protects from 1 guard encounter)");
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
			
			// Set hiding state
			isHiding = true;
			currentHidingSpot = hideable.getName();
			
			// Easter egg: Check if hiding in versteck
			if (currentRoom == versteck) {
				hiddenInVersteckCount++;
				
				if (hiddenInVersteckCount >= 4 && !teleportMachineRevealed) {
					System.out.println();
					System.out.println("*** As you hide, you notice the wall shifting slightly... ***");
					System.out.println("*** A hidden panel opens, revealing a strange glowing machine! ***");
					System.out.println("*** A TELEPORT MACHINE has been revealed! ***");
					teleportMachineRevealed = true;
					
					// Add teleport machine to the room
					Searchable teleportMachine = new Searchable("teleportmachine", "A mysterious glowing teleport machine with buttons", null);
					teleportMachine.setDoor(true); // Use door flag for special handling
					teleportMachine.setRequiredItem("easter-egg");
					versteck.addSearchable(teleportMachine);
				} else if (hiddenInVersteckCount < 4) {
					System.out.println("(Hidden " + hiddenInVersteckCount + "/4 times in this room...)");
				}
			}
			
			// Action already deducted in processCommand, now check if round ended
			if (movesLeft == 0) {
				System.out.println();
				System.out.println("=== NEW ROUND ===");
				movesLeft = MAX_MOVES_PER_ROUND;
				moveGuard(); // Guard moves if round ended
			}
			System.out.println("Actions remaining: " + movesLeft);
			
			// Ask player if they want to stay hidden or exit
			askHidingOptions();
		}
	}
	
	private void askHidingOptions() {
		while (isHiding) {
			System.out.println();
			System.out.println("You are hiding in the " + currentHidingSpot + ".");
			System.out.println("What do you want to do?");
			System.out.println("  1. Stay hidden");
			System.out.println("  2. Exit hiding spot");
			System.out.print("Enter choice (1 or 2): ");
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				String input = reader.readLine();
				if (input == null || input.trim().isEmpty()) {
					System.out.println("You exit the hiding spot.");
					isHiding = false;
					break;
				}
				
				String choice = input.trim();
				if (choice.equals("1")) {
					System.out.println("You remain hidden in the " + currentHidingSpot + "...");
					System.out.println("Time passes quietly...");
					movesLeft--;
					
					// Check if we're in versteck and increment counter
					if (currentRoom == versteck) {
						hiddenInVersteckCount++;
						
						if (hiddenInVersteckCount >= 4 && !teleportMachineRevealed) {
							System.out.println();
							System.out.println("*** As you remain hidden, you notice the wall shifting slightly... ***");
							System.out.println("*** A hidden panel opens, revealing a strange glowing machine! ***");
							System.out.println("*** A TELEPORT MACHINE has been revealed! ***");
							teleportMachineRevealed = true;
							
							// Add teleport machine to the room
							Searchable teleportMachine = new Searchable("teleportmachine", "A mysterious glowing teleport machine with buttons", null);
							teleportMachine.setDoor(true); // Use door flag for special handling
							teleportMachine.setRequiredItem("easter-egg");
							versteck.addSearchable(teleportMachine);
						} else if (hiddenInVersteckCount < 4) {
							System.out.println("(Hidden " + hiddenInVersteckCount + "/4 times in this room...)");
						}
					}
					
					showMovesLeft();
					// Continue loop - ask again
				} else if (choice.equals("2")) {
					System.out.println("You carefully exit the " + currentHidingSpot + ".");
					isHiding = false;
					System.out.println();
					System.out.println(currentRoom.longDescription());
					showMovesLeft();
					break;
				} else {
					System.out.println("Invalid choice. Please enter 1 or 2.");
					// Continue loop - ask again
				}
			} catch (Exception e) {
				System.out.println("You exit the hiding spot.");
				isHiding = false;
				System.out.println();
				System.out.println(currentRoom.longDescription());
				showMovesLeft();
				break;
			}
		}
	}
	
	private void pickupNote(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Pick up what?");
			return;
		}
		
		String noteName = command.getSecondWord();
		Note note = currentRoom.removeNote(noteName);
		
		if (note == null) {
			System.out.println("There is no " + noteName + " here!");
		} else {
			System.out.println("Note added to inventory!");
			System.out.println("You read the note:");
			System.out.println(note.getText());
			score += 5; // Points for finding note
			System.out.println(">>> +5 POINTS! Current score: " + score);
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
			
			// Special handling for door
			if (searchable.isDoor()) {
				handleDoor(searchable);
				return;
			}
			
			// Special handling for password-protected safe
			if (searchable.isPasswordProtected()) {
				handlePasswordProtected(searchable);
				return;
			}
			
			// Special handling for gate
			if (searchable.isGate()) {
				handleGate(searchable);
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
					System.out.println("You've got a new item in your inventory: " + item.getName());
					score += 10; // Points for finding item
					System.out.println(">>> +10 POINTS! Current score: " + score);
				} else {
					System.out.println("You find nothing useful.");
				}
			}
		}
	}
	
	private void handlePasswordProtected(Searchable safe) {
		if (safe.isSearched()) {
			System.out.println("The safe is already open and empty.");
			return;
		}
		
		System.out.println("The safe has a digital keypad. Enter 4-digit code:");
		System.out.print("> ");
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String input = reader.readLine();
			if (input == null || input.trim().isEmpty()) {
				System.out.println("No code entered.");
				return;
			}
			
			String code = input.trim();
			if (code.equals(safe.getPassword())) {
				System.out.println("*CLICK* The safe opens!");
				safe.setSearched(true);
				Item item = safe.getItem();
				if (item != null) {
					inventory.add(item);
					System.out.println("You've got a new item in your inventory: " + item.getName());
					System.out.println("You also find a pile of money inside!");
					score += 20; // Bonus for cracking the safe
					System.out.println(">>> +20 POINTS for cracking the safe! Current score: " + score);
				}
			} else {
				System.out.println("*BEEP* Wrong code! The safe remains locked.");
			}
		} catch (Exception e) {
			System.out.println("Error reading input.");
		}
	}
	
	private void handleGate(Searchable gate) {
		String requiredItemName = gate.getRequiredItem();
		
		// Check if player has required item
		boolean hasItem = false;
		for (Item item : inventory) {
			if (item.getName().equalsIgnoreCase(requiredItemName)) {
				hasItem = true;
				break;
			}
		}
		
		// Check if this is the roof hatch or main gate
		boolean isRoofHatch = gate.getName().equalsIgnoreCase("hatch");
		
		// For pistol, also check if player has bullets
		if (isRoofHatch && hasItem && !hasBullets()) {
			System.out.println("You have the pistol but no ammunition!");
			System.out.println("You need bullets to shoot the lock.");
			return;
		}
		
		if (!hasItem) {
			if (isRoofHatch) {
				System.out.println("The " + gate.getName() + " has a heavy padlock. You need a " + requiredItemName + " to shoot it off.");
			} else {
				System.out.println("The " + gate.getName() + " is locked with a heavy padlock. You need a " + requiredItemName + " to unlock it.");
			}
		} else {
			if (isRoofHatch) {
				System.out.println("You load the pistol with bullets...");
				System.out.println("You aim the pistol at the padlock...");
				System.out.println("*BANG!* The shot echoes through the attic!");
				System.out.println("The padlock shatters and falls to the ground!");
				System.out.println("The roof hatch swings open, revealing the night sky...");
				System.out.println("You climb through the hatch onto the roof!");
				System.out.println("From the roof, you spot a fire escape ladder leading down...");
				System.out.println("You climb down to freedom!");
			} else {
				System.out.println("You use the " + requiredItemName + " to unlock the gate!");
			}
			System.out.println();
			score += 100; // Big bonus for escaping!
			System.out.println("========================================");
			System.out.println("   CONGRATULATIONS! YOU ESCAPED!");
			System.out.println("========================================");
			System.out.println("You've successfully escaped the building!");
			if (isRoofHatch) {
				System.out.println("ESCAPE ROUTE: Roof Hatch (Attic Route)");
			} else {
				System.out.println("ESCAPE ROUTE: Main Gate (Standard Route)");
			}
			System.out.println("FINAL SCORE: " + score + " points");
			System.out.println("========================================");
			System.out.println("Thank you for playing!");
			System.exit(0);
		}
	}
	
	private void handleDoor(Searchable door) {
		String requiredItemName = door.getRequiredItem();
		
		// Special case: teleport machine easter egg
		if (door.getName().equalsIgnoreCase("teleportmachine")) {
			handleTeleportMachine();
			return;
		}
		
		// Special case: door handle from inside vault (only works if door not already open)
		if (door.getName().equalsIgnoreCase("handle") && currentRoom == tresorRaum) {
			if (tresorDoorOpen) {
				System.out.println("The door is already open.");
			} else {
				// Check if player has crowbar to force the door from inside
				boolean hasCrowbar = false;
				for (Item item : inventory) {
					if (item.getName().equalsIgnoreCase("crowbar")) {
						hasCrowbar = true;
						break;
					}
				}
				
				if (!hasCrowbar) {
					System.out.println("The door handle won't budge. It seems to be locked from the outside.");
					System.out.println("You need something to force it open.");
				} else {
					System.out.println("You use the crowbar to force the door handle from inside.");
					System.out.println("The door opens! You can now access the Boss Office.");
					bueroChef.setExits(null, tresorRaum, null, flurOG);
					tresorRaum.setExits(null, null, sicherheitsraum, bueroChef);
					tresorDoorOpen = true;
					System.out.println("You can now go west to the Boss Office.");
				}
			}
			return;
		}
		
		// Check if player has required item
		boolean hasItem = false;
		for (Item item : inventory) {
			if (item.getName().equalsIgnoreCase(requiredItemName)) {
				hasItem = true;
				break;
			}
		}
		
		if (!hasItem) {
			System.out.println("The " + door.getName() + " is locked. You need a " + requiredItemName + " to unlock it.");
		} else {
			System.out.println("You use your " + requiredItemName + " to unlock the " + door.getName() + ".");
			System.out.println("The door opens with a satisfying click!");
			
			// Open the door from bueroChef to tresorRaum
			if (currentRoom == bueroChef) {
				bueroChef.setExits(null, tresorRaum, null, flurOG);
				tresorRaum.setExits(null, null, sicherheitsraum, bueroChef);
				tresorDoorOpen = true;
				System.out.println("You can now go east to the Vault Room.");
			}
			
			// Open attic door in bibliothek
			if (door.getName().equalsIgnoreCase("atticdoor") && currentRoom == bibliothek) {
				System.out.println("You pry the boards off the attic door with the crowbar!");
				System.out.println("The door creaks open, revealing stairs leading up to the attic.");
				bibliothek.setExits(dachboden, flurOG, keller, null);
				System.out.println("You can now go north to the Attic.");
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
	
	private void handleTeleportMachine() {
		System.out.println();
		System.out.println("*** TELEPORT MACHINE ACTIVATED! ***");
		System.out.println("The machine hums with mysterious energy...");
		System.out.println("A glowing portal opens before you...");
		System.out.println();
		System.out.println("*** WHOOOOSH! ***");
		System.out.println("You feel your body dissolving into energy...");
		System.out.println("Reality shifts around you...");
		System.out.println("You materialize outside the building!");
		System.out.println();
		score += 150; // Easter egg bonus
		System.out.println("========================================");
		System.out.println("   CONGRATULATIONS! YOU ESCAPED!");
		System.out.println("========================================");
		System.out.println("You discovered the secret teleport easter egg!");
		System.out.println("You've successfully escaped the building!");
		System.out.println("FINAL SCORE: " + score + " points (+50 easter egg bonus!)");
		System.out.println("========================================");
		System.out.println("Thank you for playing!");
		System.exit(0);
	}
	
	private void useItem(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Use what?");
			return;
		}
		
		String itemName = command.getSecondWord().toLowerCase();
		
		// Check if item exists in inventory
		Item itemToUse = null;
		for (Item item : inventory) {
			if (item.getName().equalsIgnoreCase(itemName)) {
				itemToUse = item;
				break;
			}
		}
		
		if (itemToUse == null) {
			System.out.println("You don't have a " + itemName + " in your inventory!");
			return;
		}
		
		// Handle disguise
		if (itemName.equals("disguise")) {
			if (hasDisguise) {
				System.out.println("You've already used the disguise!");
				return;
			}
			System.out.println("You put on the security guard uniform...");
			System.out.println("Perfect! The guard won't recognize you now (ONE TIME USE)!");
			hasDisguise = true;
			score += 50; // Bonus points for using disguise
			inventory.remove(itemToUse);
			System.out.println(">>> +50 POINTS! Current score: " + score);
			
		// Handle energy drink
		} else if (itemName.equals("energydrink")) {
			if (hasEnergyDrink) {
				System.out.println("You've already used an energy drink!");
				return;
			}
			System.out.println("You drink the energy drink...");
			System.out.println("You feel energized! +1 EXTRA ACTION this round!");
			hasEnergyDrink = true;
			movesLeft++; // Grant extra action
			score += 30; // Bonus points for using energy drink
			inventory.remove(itemToUse);
			System.out.println(">>> +30 POINTS! Current score: " + score);
			showMovesLeft();
			
		} else {
			System.out.println("You can't use " + itemName + " like that. Try using it with the 'search' command instead.");
		}
	}
	
	private void showMovesLeft() {
		if (movesLeft == 0) {
			System.out.println();
			System.out.println("=== NEW ROUND ===");
			movesLeft = MAX_MOVES_PER_ROUND;
			moveGuard(); // Guard moves every round
		}
		System.out.println("Actions remaining: " + movesLeft);
	}
	
	private void resetRound() {
		System.out.println();
		System.out.println("=== NEW ROUND ===");
		movesLeft = MAX_MOVES_PER_ROUND;
		moveGuard(); // Guard moves when round resets
		System.out.println("Actions remaining: " + movesLeft);
		System.out.println();
	}
	
	/**
	 * Ask the guard to move and announce the movement to the player.
	 * After the guard moves we immediately check for any encounter.
	 */
	private void moveGuard() {
		guard.move();
		System.out.println();
		System.out.println("*** You hear footsteps... The Guard is on patrol! ***");
		System.out.println("(" + guard.getLocationDescription() + ")");
		System.out.println();
		
		checkGuardEncounter();
	}
	
	/**
	 * Evaluate whether the guard and the player are in the same room and
	 * resolve the outcome. The result can be: player escapes by
	 * neutralizing the guard, uses a disguise, hides successfully, or
	 * gets caught (game over).
	 */
	private void checkGuardEncounter() {
		if (guard.getCurrentRoom() == currentRoom && !isHiding) {
			// Check if player has both disguise, pistol AND bullets - shoot the guard!
			if (hasDisguise && hasPistol() && hasBullets() && !guardShot) {
				System.out.println();
				System.out.println("*** The Guard enters the room! ***");
				System.out.println("*** He sees you in the security uniform and approaches... ***");
				System.out.println("*** You quickly pull out the pistol! ***");
				System.out.println("*** *BANG!* You shoot the guard! ***");
				System.out.println("*** The guard falls to the ground, unconscious! ***");
				System.out.println("*** The way is clear - you can now escape freely! ***");
				System.out.println();
				guardShot = true;
				score += 200; // Big bonus for eliminating the guard!
				System.out.println(">>> +200 POINTS for neutralizing the guard! Current score: " + score);
				System.out.println();
				System.out.println("========================================");
				System.out.println("   CONGRATULATIONS! YOU ESCAPED!");
				System.out.println("========================================");
				System.out.println("With the guard neutralized, you calmly walk out!");
				System.out.println("ESCAPE ROUTE: Combat Victory (Secret Route)");
				System.out.println("FINAL SCORE: " + score + " points");
				System.out.println("========================================");
				System.out.println("Thank you for playing!");
				System.exit(0);
			}
			
			// Check if player has disguise active (but no gun or bullets)
			if (hasDisguise) {
				System.out.println();
				System.out.println("*** The Guard enters the room! ***");
				System.out.println("*** He sees you in the security uniform... ***");
				System.out.println("*** He nods at you - he thinks you're a colleague! ***");
				System.out.println("*** The Guard continues his patrol... ***");
				System.out.println("*** Your disguise has been used up! ***");
				hasDisguise = false; // Disguise consumed
				return;
			}
			
			System.out.println();
			System.out.println("========================================");
			System.out.println("        GAME OVER!");
			System.out.println("========================================");
			System.out.println("The Guard spotted you!");
			System.out.println("You've been caught and escorted out.");
			System.out.println("Better luck next time!");
			System.out.println("========================================");
			System.out.println("Final Score: " + score + " points");
			System.out.println("========================================");
			System.exit(0);
		} else if (guard.getCurrentRoom() == currentRoom && isHiding) {
			System.out.println();
			System.out.println("*** The Guard enters the room! ***");
			System.out.println("*** You hold your breath in your hiding spot... ***");
			System.out.println("*** The Guard looks around but doesn't see you! ***");
			System.out.println("*** The Guard leaves the room and continues his patrol... ***");
			score += 5; // Points for successfully hiding from guard
			System.out.println(">>> +5 POINTS for hiding successfully! Current score: " + score);
			guard.move(); // Guard moves to random adjacent room
			System.out.println("*** He moved to: " + guard.getCurrentRoom().shortDescription() + " ***");
			System.out.println();
		}
	}
	
	private boolean hasPistol() {
		for (Item item : inventory) {
			if (item.getName().equalsIgnoreCase("pistol")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasBullets() {
		for (Item item : inventory) {
			if (item.getName().equalsIgnoreCase("bullets")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Display a simple ASCII map to help the player navigate. This map is
	 * purely informational and does not change game state.
	 */
	private void showMap() {
		System.out.println();
		System.out.println("========================================");
		System.out.println("            BUILDING MAP");
		System.out.println("========================================");
		System.out.println();
		System.out.println("[Attic]");
		System.out.println("   |");
		System.out.println("   |");
		System.out.println("   |");
		System.out.println("[Library]---[Upper Corridor]---[Boss Office]---[Vault Room]");
		System.out.println("    |              |                               |");
		System.out.println("    |              |                               |");
		System.out.println("    |              |                               |");
		System.out.println("[Cellar]------[Ground Corridor]------[Reception Hall] [Security Room]");
		System.out.println("    |              |                |              |");
		System.out.println("    |              |                |              |");
		System.out.println("    |              |                |              |");
		System.out.println("[Boiler Room]-[Kitchen]------[Cafeteria]---[Surveillance Room]");
		System.out.println("    |              |                |");
		System.out.println("    |              |                |");
		System.out.println("    |              |                |");
		System.out.println("[Hideout]------[Storage Room]--[Outside Area]");
		System.out.println();
		System.out.println("Your current location: " + currentRoom.shortDescription());
		System.out.println("Guard location: " + guard.getCurrentRoom().shortDescription());
		System.out.println("========================================");
	}
}

