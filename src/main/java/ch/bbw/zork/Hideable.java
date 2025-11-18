package ch.bbw.zork;

/**
 * Hideable - a small value object representing an object in a room
 * where a player can hide (e.g. "desk", "closet").
 */
public class Hideable {
    
    private String name;
    private String description;
    
    public Hideable(String name, String description) {
        // Name used by commands and a short description shown to player
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        // Returns the identifier players use when hiding
        return name;
    }
    
    public String getDescription() {
        // A short human-friendly description
        return description;
    }
}
