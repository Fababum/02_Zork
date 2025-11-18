package ch.bbw.zork;
/**
 * Author:  Michael Kolling, Version: 1.1, Date: August 2000
 * refactoring: Rinaldo Lanza, September 2020
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Room {
	
	private String description;
	private HashMap<String, Room> exits;
	private ArrayList<Item> items;
	private ArrayList<Hideable> hideables;
	private ArrayList<Searchable> searchables;
	private ArrayList<Note> notes;

	public Room(String description) {
		this.description = description;
		this.exits = new HashMap<>();
		this.items = new ArrayList<>();
		this.hideables = new ArrayList<>();
		this.searchables = new ArrayList<>();
		this.notes = new ArrayList<>();
	}

	/**
	 * Define the room's exits in the four cardinal directions. Pass
	 * null for a direction that doesn't have an exit.
	 */
	public void setExits(Room north, Room east, Room south, Room west) {
		exits.put("north", north);
		exits.put("east", east);
		exits.put("south", south);
		exits.put("west", west);
	}

	/**
	 * A short single-line description used in map and location prints.
	 */
	public String shortDescription() {
		return description;
	}

	/**
	 * A longer description used when the player enters the room. It
	 * includes visible exits, items, notes and interactive objects.
	 */
	public String longDescription() {
		StringBuilder stringBuilder = new StringBuilder("You are in " + description + ".\n");
		stringBuilder.append(exitString());
		String itemsStr = getItemsString();
		if (!itemsStr.isEmpty()) {
			stringBuilder.append("\n").append(itemsStr);
		}
		String notesStr = getNotesString();
		if (!notesStr.isEmpty()) {
			stringBuilder.append("\n").append(notesStr);
		}
		String hideablesStr = getHideablesString();
		if (!hideablesStr.isEmpty()) {
			stringBuilder.append("\n").append(hideablesStr);
		}
		String searchablesStr = getSearchablesString();
		if (!searchablesStr.isEmpty()) {
			stringBuilder.append("\n").append(searchablesStr);
		}
		return stringBuilder.toString();
	}

	/**
	 * Build a short string listing the available exits (e.g. "Exits: north east").
	 */
	private String exitString() {
		StringBuilder exitString = new StringBuilder("Exits:");
		for (String direction : exits.keySet()) {
			if (exits.get(direction) != null) {
				exitString.append(" ").append(direction);
			}
		}
		return exitString.toString();
	}

	/**
	 * Return the room that lies in the given direction, or null if there
	 * is no exit that way.
	 */
	public Room nextRoom(String direction) {
		return exits.get(direction);
	}
	
	/**
	 * Add an item to this room's ground so the player can pick it up later.
	 */
	public void addItem(Item item) {
		items.add(item);
	}
	
	/**
	 * Remove and return an item by name (case-insensitive). Returns null
	 * when the item is not present.
	 */
	public Item removeItem(String itemName) {
		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			if (item.getName().equalsIgnoreCase(itemName)) {
				items.remove(i);
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Check whether an item with the given name exists in the room.
	 */
	public boolean hasItem(String itemName) {
		for (Item item : items) {
			if (item.getName().equalsIgnoreCase(itemName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return a human-friendly string listing the items that lie here.
	 */
	public String getItemsString() {
		if (items.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder("Items here:");
		for (Item item : items) {
			sb.append(" ").append(item.getName());
		}
		return sb.toString();
	}
	
	/**
	 * Manage places a player can hide in this room.
	 */
	public void addHideable(Hideable hideable) {
		hideables.add(hideable);
	}
	
	/**
	 * Find a hideable by name (case-insensitive) or return null when not present.
	 */
	public Hideable getHideable(String hideableName) {
		for (Hideable hideable : hideables) {
			if (hideable.getName().equalsIgnoreCase(hideableName)) {
				return hideable;
			}
		}
		return null;
	}
	
	/**
	 * Return a short list of objects the player can hide in.
	 */
	public String getHideablesString() {
		if (hideables.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder("You can hide in:");
		for (Hideable hideable : hideables) {
			sb.append(" ").append(hideable.getName());
		}
		return sb.toString();
	}
	
	/**
	 * Add an interactive object the player can search (drawers, lockers, vents).
	 */
	public void addSearchable(Searchable searchable) {
		searchables.add(searchable);
	}
	
	/**
	 * Find a searchable object by name or null if it's not here.
	 */
	public Searchable getSearchable(String searchableName) {
		for (Searchable searchable : searchables) {
			if (searchable.getName().equalsIgnoreCase(searchableName)) {
				return searchable;
			}
		}
		return null;
	}
	
	/**
	 * Return a short description of searchable objects located in this room.
	 */
	public String getSearchablesString() {
		if (searchables.isEmpty()) {
			return "";
		}
		StringBuilder sb = new StringBuilder("You can search:");
		for (Searchable searchable : searchables) {
			sb.append(" ").append(searchable.getName());
		}
		return sb.toString();
	}
	
	/**
	 * Note management: add or remove a note. Notes can be picked up by the player.
	 */
	public void addNote(Note note) {
		notes.add(note);
	}
	
	public Note removeNote(String noteName) {
		for (int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);
			if (note.getName().equalsIgnoreCase(noteName)) {
				notes.remove(i);
				return note;
			}
		}
		return null;
	}
	
	/**
	 * Return a human-friendly description of notes lying here.
	 */
	public String getNotesString() {
		if (notes.isEmpty()) {
			return "";
		}
		if (notes.size() == 1) {
			return "A note is lying here";
		}
		return "There are " + notes.size() + " notes lying here";
	}

}




