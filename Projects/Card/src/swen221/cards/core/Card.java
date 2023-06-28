package swen221.cards.core;

public class Card implements Comparable<Card> {
	
	/**
	 * Represents a card suit.
	 * 
	 * @author David J. Pearce
	 *
	 */
	public enum Suit {
		HEARTS,
		CLUBS,
		DIAMONDS,
		SPADES;
	}
	
	/**
	 * Represents the different card "numbers".
	 * 
	 * @author David J. Pearce
	 *
	 */
	public enum Rank {
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING,
		ACE;
	}
	
	// =======================================================
	// Card stuff
	// =======================================================
	
	private Suit suit; // HEARTS, CLUBS, DIAMONDS, SPADES
	private Rank rank; // 2 <= number <= 14 (ACE)
	
	/**
	 * Construct a card in the given suit, with a given number
	 * 
	 * @param suit
	 *            --- between 0 (HEARTS) and 3 (SPADES)
	 * @param number
	 *            --- between 2 and 14 (ACE)
	 */
	public Card(Suit suit, Rank number) {				
		this.suit = suit;
		this.rank = number;
	}

	/**
	 * Get the suit of this card, between 0 (HEARTS) and 3 (SPADES).
	 * 
	 * @return
	 */
	public Suit suit() {
		return suit;
	}

	/**
	 * Get the number of this card, between 2 and 14 (ACE).
	 * 
	 * @return
	 */
	public Rank rank() {
		return rank;
	}	
	
	public Card getCard() {
		return this;
	}
		
	private static String[] suits = { "Hearts","Clubs","Diamonds","Spades"};
	private static String[] ranks = { "2 of ", "3 of ", "4 of ",
			"5 of ", "6 of ", "7 of ", "8 of ", "9 of ", "10 of ", "Jack of ",
			"Queen of ", "King of ", "Ace of " };
	
	public String toString() {
		return ranks[rank.ordinal()] + suits[suit.ordinal()];		
	}
	
	public boolean equals(Card o) {
		if (this.compareTo(o) == 0) return true;
		else return false;
	}
	
	public int hashCode(){
		int hc = 0;
		int cardSuit = 0;
		int cardRank = 0;
		if (this.suit() == Suit.HEARTS) cardSuit = 0;
			else if (this.suit() == Suit.CLUBS) cardSuit = 1;
			else if (this.suit() == Suit.DIAMONDS) cardSuit = 2;
			else if (this.suit() == Suit.SPADES) cardSuit = 3;

		if (this.rank() == Rank.TWO) cardRank = 0;
			else if (this.rank() == Rank.THREE) cardRank = 1;
			else if (this.rank() == Rank.FOUR) cardRank = 2;
			else if (this.rank() == Rank.FIVE) cardRank = 3;
			else if (this.rank() == Rank.SIX) cardRank = 4;
			else if (this.rank() == Rank.SEVEN) cardRank = 5;
			else if (this.rank() == Rank.EIGHT) cardRank = 6;
			else if (this.rank() == Rank.NINE) cardRank = 7;
			else if (this.rank() == Rank.TEN) cardRank = 8;
			else if (this.rank() == Rank.JACK) cardRank = 9;
			else if (this.rank() == Rank.QUEEN) cardRank = 10;
			else if (this.rank() == Rank.KING) cardRank = 11;
			else if (this.rank() == Rank.ACE) cardRank = 12;
		
		hc = 100 * cardSuit + cardRank;
		return hc;
	}

	@Override
	public int compareTo(Card o) {
		// TODO: you need to implement this!
		if (o == null) return 0;
		return this.hashCode() - o.hashCode();
	}
	
	public Card clone() {
		return new Card(this.suit, this.rank);
	}
	
}
