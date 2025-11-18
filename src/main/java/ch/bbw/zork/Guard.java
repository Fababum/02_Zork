package ch.bbw.zork;

import java.util.ArrayList;
import java.util.Random;


/**
 * Guard - a simple moving enemy that patrols the building.
 *
 * The guard can either follow a predefined patrol route or move
 * randomly between adjacent rooms. Some rooms can be marked as
 * forbidden (the guard will not enter them). To make movement feel
 * more natural we remember the previous room and avoid immediately
 * stepping back when other options are available.
 */
public class Guard {

	// Guard's display name (shown in messages)
	private String name;
	// Current room the guard is standing in
	private Room currentRoom;
	// Random number generator used for random movement
	private Random random;
	// Optional patrol route: if populated the guard follows these rooms
	private ArrayList<Room> patrolRoute;
	// Index into the patrol route (when used)
	private int patrolIndex;
	// Rooms the guard must not enter (e.g. vault, attic)
	private ArrayList<Room> forbiddenRooms; // Rooms the guard cannot enter
	// Remember the last room to reduce immediate backtracking
	private Room previousRoom; // remember last room to avoid immediate backtracking
	
	public Guard(String name, Room startRoom) {
		this.name = name;
		this.currentRoom = startRoom;
		this.random = new Random();
		this.patrolRoute = new ArrayList<>();
		this.patrolIndex = 0;
		this.forbiddenRooms = new ArrayList<>();
		this.previousRoom = null;
	}
	
	public String getName() {
		return name;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}
	
	public void setPatrolRoute(ArrayList<Room> route) {
		this.patrolRoute = route;
		this.patrolIndex = 0;
	}
	
	public void setForbiddenRoom(Room room) {
		this.forbiddenRooms.add(room);
	}
	
	/**
	 * Move the guard to the next room in patrol route, or randomly if no route set
	 */
	public void move() {
		if (!patrolRoute.isEmpty()) {
			// Follow patrol route
			patrolIndex = (patrolIndex + 1) % patrolRoute.size();
			previousRoom = currentRoom;
			currentRoom = patrolRoute.get(patrolIndex);
		} else {
			// Move randomly
			moveRandomly();
		}
	}
	
	private void moveRandomly() {
		// Get all possible exits from current room
		ArrayList<Room> possibleRooms = new ArrayList<>();
		
		Room north = currentRoom.nextRoom("north");
		Room east = currentRoom.nextRoom("east");
		Room south = currentRoom.nextRoom("south");
		Room west = currentRoom.nextRoom("west");
		
		if (north != null && !forbiddenRooms.contains(north)) possibleRooms.add(north);
		if (east != null && !forbiddenRooms.contains(east)) possibleRooms.add(east);
		if (south != null && !forbiddenRooms.contains(south)) possibleRooms.add(south);
		if (west != null && !forbiddenRooms.contains(west)) possibleRooms.add(west);
		
		// Move to a random adjacent room
		if (!possibleRooms.isEmpty()) {
			// Avoid immediately moving back to the previous room when possible
			if (previousRoom != null && possibleRooms.size() > 1) {
				possibleRooms.remove(previousRoom);
			}
			int randomIndex = random.nextInt(possibleRooms.size());
			previousRoom = currentRoom;
			currentRoom = possibleRooms.get(randomIndex);
		}
	}
	
	public String getLocationDescription() {
		return "The Guard is currently in: " + currentRoom.shortDescription();
	}
}
