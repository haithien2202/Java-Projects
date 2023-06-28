package swen221.cards.core;

/**
 * This class represents a card in a trick-taking card game. Its purpose is only
 * to compare between each other in a strategy making process. A card in a
 * trick-taking game has its rank and suit. It also has two flags to indicate
 * whether its suit is lead suit, and whether its suit is trump suit.
 * 
 * @author
 *
 */
public class ComparableCard implements Comparable<ComparableCard> {
    private Card card;
    private boolean isLead;
    private boolean isTrump;

    /**
     * Constructor of ComparableCard
     * 
     * @param card
     * 
     * @param leadSuit
     *                   
     * @param trumpSuit
     *  
     */
    public ComparableCard(Card card, Card.Suit leadSuit, Card.Suit trumpSuit) {
        this.card = card;

        if (this.card.suit() != trumpSuit) {
        	this.isTrump = false;
        } else {
            this.isTrump = true;
        }
        
        if (this.card.suit() != leadSuit) {
        	this.isLead = false;
        } else {
        	this.isLead = true;
        } 
    }

    /**
     * Get card.
     * 
     * @return --- the comparable card.
     */
    public Card getCard() {
        return card;
    }
    
    
    
    @Override
    public boolean equals(Object o) {
    	if (o == null) return false;
        if (this == o) return true;
        if (getClass() != o.getClass()) return false;
        ComparableCard other = (ComparableCard) o;
        if (card == null) { 
        	if (other.card != null)  return false; 
        	else if (!card.equals(other.card)) return false;
        }
        if (isTrump != other.isTrump) return false;
        if (isLead != other.isLead) return false;
        return true;
    }
    
    /**
     * Generate hashcode for comparable card.
     * 
     * @return --- the hashcode number for card.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((card == null) ? 0 : card.hashCode());
        result = prime * result + (isLead ? 1231 : 1237);
        result = prime * result + (isTrump ? 1231 : 1237);
        return result;
    }

    @Override
    public int compareTo(ComparableCard other) {

        // Priority trump cards.
        if (!this.isTrump && other.isTrump) {
            return -1; // trump card always win
        } else if (this.isTrump && !other.isTrump) {
            return 1; // trump card always win
        } else if (this.isTrump && other.isTrump) {
            // if both are trump card
            int result = this.card.rank().ordinal() - other.card.rank().ordinal();
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            // follow lead suit when both are not trump. Suit comes as second priority.
            if (!this.isLead && other.isLead) {
                return -1; // lead suit can beat others
            } else if (!this.isLead && other.isLead) {
                return 1; // lead suit can beat others
            } else {
                // compare their rank
                int result = this.card.rank().ordinal() - other.card.rank().ordinal();
                if (result < 0) {
                    return -1;
                } else if (result > 0) {
                    return 1;
                } else {
                    // same rank different suit, use Card.compareTo() to sort
                    if (this.card.compareTo(other.card) < 0) {
                        return -1;
                    } else if (this.card.compareTo(other.card) > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

}