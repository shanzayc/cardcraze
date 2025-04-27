/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * Class to make card objects using OOP
 * @author shanzay
 */
public class Card {
    
    //instance variables
    private String content; //holds unicode symbol
    private boolean isFlipped; 
    private boolean matched;
    
    //constructor method 

    /**
     * Constructor method to make card object
     * @param c the string which holds the cards symbol
     */
    public Card(String c){
        content = c;
        isFlipped = false;
        matched = false;
    }
    
    //getter methods

    /**
     * Returns true or false depending on if the card has been flipped or not
     * @return the flip status of card 
     */
    public boolean getIsFlipped(){
        return isFlipped;
    }
    
    /**
     * Returns true or false depending on if card has been matched 
     * @return the matched status of card
     */
    public boolean getMatched(){
        return matched;
    }
    
    /**
     * Returns the symbol on the card
     * @return cards content
     */
    public String getContent(){
        return content;
    }
    
    //setter methods

    /**
     * Sets flipped status of the card
     * @param isF is new flipped status to set 
     */
    public void setIsFlipped(boolean isF){
        this.isFlipped = isF;
    }
    
    /**
     * Sets matched status of the card
     * @param m is the new matched status to set
     */
    public void setMatched(boolean m){
        this.matched = m;
    }
       
}
