package ch.bbw.zork;

/**
 * Class Searchable - represents an object that can be searched
 */
public class Searchable {
    
    private String name;
    private String description;
    private Item item;
    private boolean searched;
    
    public Searchable(String name, String description, Item item) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.searched = false;
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
}
