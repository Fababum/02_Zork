package ch.bbw.zork;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class Guard - represents a moving enemy in the game
 */
public class Guard {
	
	private String name;
	private Room currentRoom;
	private Random random;
	private ArrayList<Room> patrolRoute;
	private int patrolIndex;
	private Room forbiddenRoom; // Room the guard cannot enter
	
	public Guard(String name, Room startRoom) {
		this.name = name;
		this.currentRoom = startRoom;
		this.random = new Random();
		this.patrolRoute = new ArrayList<>();
		this.patrolIndex = 0;
		this.forbiddenRoom = null;
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
		this.forbiddenRoom = room;
	}
	
	/**
	 * Move the guard to the next room in patrol route, or randomly if no route set
	 */
	public void move() {
		if (!patrolRoute.isEmpty()) {
			// Follow patrol route
			patrolIndex = (patrolIndex + 1) % patrolRoute.size();
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
		
		if (north != null && north != forbiddenRoom) possibleRooms.add(north);
		if (east != null && east != forbiddenRoom) possibleRooms.add(east);
		if (south != null && south != forbiddenRoom) possibleRooms.add(south);
		if (west != null && west != forbiddenRoom) possibleRooms.add(west);
		
		// Move to a random adjacent room
		if (!possibleRooms.isEmpty()) {
			int randomIndex = random.nextInt(possibleRooms.size());
			currentRoom = possibleRooms.get(randomIndex);
		}
	}
	
	public String getLocationDescription() {
		return "The Guard is currently in: " + currentRoom.shortDescription();
	}
}
