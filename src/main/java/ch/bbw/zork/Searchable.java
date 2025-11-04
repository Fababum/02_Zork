package ch.bbw.zork;

/**
 * Class Searchable - represents an object that can be searched
 */
public class Searchable {
    
    private String name;
    private String description;
    private Item item;
    private boolean searched;
    private boolean isVent;
    
    public Searchable(String name, String description, Item item) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.searched = false;
        this.isVent = false;
    }
    
    public Searchable(String name, String description, Item item, boolean isVent) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.searched = false;
        this.isVent = isVent;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Item getItem() {
        return item;
    }
    
    public boolean isSearched() {
        return searched;
    }
    
    public void setSearched(boolean searched) {
        this.searched = searched;
    }
    
    public boolean isVent() {
        return isVent;
    }
}
