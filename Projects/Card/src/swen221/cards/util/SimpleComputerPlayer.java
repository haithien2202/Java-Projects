 package swen221.cards.util;

import swen221.cards.core.Card;
import swen221.cards.core.Player;
import swen221.cards.core.Trick;

/**
 * Implements a simple computer player who plays the highest card available when
 * the trick can still be won, otherwise discards the lowest card available. In
 * the special case that the player must win the trick (i.e. this is the last
 * card in the trick), then the player conservatively plays the least card
 * needed to win.
 * 
 * @author David J. Pearce
 * 
 */
public class SimpleComputerPlayer extends AbstractComputerPlayer {

	public SimpleComputerPlayer(Player player) {
		super(player);
	}

	public Card getNextCard(Trick trick) {
		//get leading player
		Card firstPlayed = trick.getCardPlayed(trick.getLeadPlayer());
		//return this card.
		Card bestCard = null;
		// To see the player playing order
		int index = 0;
		// If the player can follow the leading suit
		boolean ableToFollow = false;
		
		// Find the highestCard, lowestCard and closestCard cards
		Card highestCard = null;
		Card closestCard = null;
		Card lowestCard = null;

		// Check if we can follow the suit
				if (firstPlayed != null) {
					for (Card card : player.hand) {
						if (card.suit() == firstPlayed.suit()) {
							ableToFollow = true;
							break;
						}
					}
				}
		
		// Get highest card in the hand
		for (; index < trick.getCardsPlayed().size(); index++) {
			Card current = trick.getCardsPlayed().get(index);
			//else, compare the 2 cards and return the higher one
			if (current != null && (current.suit() == firstPlayed.suit() || current.suit() == trick.getTrumps())) 	bestCard = getBetter(bestCard, current, trick);
			//if cest card is null, current card is the best card
	        else if (bestCard == null) 	bestCard = current;
		}
	
		// Pick the highest card in the hand if the player is the leading player.
		if (firstPlayed != null) {
			for (Card card : player.hand) {
				if (ableToFollow) if (card.suit() != firstPlayed.suit()) continue;
		
				lowestCard = getBetter(lowestCard, card, trick) == lowestCard || lowestCard == null ? card	: lowestCard;
				highestCard = getBetter(highestCard, card, trick);
				
				Card temporary = getBetter(card, bestCard, trick);
				if (temporary.equals(card)) {
					temporary = getBetter(card, closestCard, trick);	
					if ((temporary.equals(closestCard) || closestCard == null) && (card.suit() == firstPlayed.suit() || card.suit() == trick.getTrumps())) closestCard = card;
				}
			}
		}
		//else if they player is not leading player.
		else {
			for (Card card : player.hand) {
				if (highestCard == null) highestCard = card;
				else if (card.suit() == trick.getTrumps()) {
					if (highestCard.suit() == trick.getTrumps()) highestCard = getBetter(highestCard, card, trick);
					else highestCard = card;} 
				else if (highestCard.suit() != trick.getTrumps()) highestCard = (highestCard.rank().ordinal() > card.rank().ordinal()) ? highestCard : card;
			}
		}

		// First to play, do leading AI strat.
		if (index == 0)return highestCard;
		
		//check if we cannot follow the suit (definitely lose), play the lowest card in our hand.
		if (!ableToFollow && highestCard.suit().ordinal() != trick.getTrumps().ordinal()) return lowestCard;

		//neither end or leading, do normal AI strat
		// If there is winnable card, play the highest. Otherwise, play the lowest.
		if (index != 3) {
			// if there is winable card.
			if (getBetter(highestCard, bestCard, trick) == highestCard)	return highestCard;
			//else there is no winable card.
			else return lowestCard;
		}
		// If we are the last to play then run the end strat
		// If there is no winable card, play the lowest card, else get least higher card to play.
		else {
			//if there is more than 1 winable card.
			if (closestCard != null) return closestCard;
			// if there is no winable card.
			else return lowestCard;
		}
	}

	/**
	 * Takes two cards and returns the one that have the higher value
	 * 
	 * @param card
	 * @param other
	 * @param t
	 * @return higherst card
	 */
	public Card getBetter(Card card, Card other, Trick current) {

		// Return the other if the card is null and opposite
		if (other == null) {
			return card;
		} else if (card == null) {
			return other;
		}
		
		//If both of the card are trumps.
		if (card.suit() == current.getTrumps() && other.suit() == current.getTrumps()) {
			return (card.rank().ordinal() > other.rank().ordinal()) ? card : other;
		}
		// if this card is trump.
		else if (card.suit() == current.getTrumps()) {
			return card;
		} 
		//if other card is trump.
		else if (other.suit() == current.getTrumps()) {
			return other;
		}
		// if both are not trump.
		else {
			//return the higher value card.
			//if they have the same rank
			if (card.rank() == other.rank()) {
				return (card.compareTo(other) > 0) ? card : other;
			} 
			//if they don't have the same rank
			else {
				return (card.rank().ordinal() > other.rank().ordinal()) ? card : other; 
			}
		}
	}
}