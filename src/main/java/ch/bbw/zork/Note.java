package ch.bbw.zork;

/**
 * Note - represents a written note the player can pick up and read.
 * Notes often contain clues used to solve puzzles in the game.
 */
public class Note {
    
    private String name;
    private String text;
    
    public Note(String name, String text) {
        this.name = name;
        this.text = text;
    }
    
    public String getName() {
        // The identifier used to pick up the note (e.g. "note")
        return name;
    }
    
    public String getText() {
        // The actual content of the note shown to the player
        return text;
    }
}
