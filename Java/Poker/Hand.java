/***************************************************************************
Copyright (c) 2000:
      University of Alberta,
      Deptartment of Computing Science
      Computer Poker Research Group

      See "Liscence.txt"
***************************************************************************/

import java.util.StringTokenizer;

/**
 * Stores a Hand of Cards (up to a maximum of 7)
 *
 * @author  Aaron Davidson
 */

public class Hand {
	public final static int MAX_CARDS = 7;
   
	private int[] mCards;  // mCards[0] is number of cards in hand
   
	public Hand() {
		mCards    = new int[MAX_CARDS + 1];
		mCards[0] = 0;
   }
   
   /**
	* Constructs a hand of cards given a string like "Ad Ks".
    * @param cs A string representing a Hand of cards
    */
	public Hand(String cs) {
		mCards    = new int[MAX_CARDS + 1];
		mCards[0] = 0;
		StringTokenizer t = new StringTokenizer(cs," -");
		while(t.hasMoreTokens()) {
			String s = t.nextToken();
			if (s.length() == 2) {
				Card c = new Card(s.charAt(0), s.charAt(1));
				if (c.getIndex() != Card.BAD_CARD) {
					addCard(c);
				}
			}
		}     
	}
	/**
	 * Duplicate an existing hand.
	 * @param h the hand to clone.
    */
	public Hand(Hand h) {
		mCards    = new int[MAX_CARDS + 1];
		mCards[0] = h.size();
		for (int i = 1; i <= mCards[0]; ++i) {
			mCards[i] = h.mCards[i];
		}
	}
	/**
	 * Get the size of the hand.
	 * @return the number of cards in the hand
	 */
	public int size() {
		return mCards[0];
	}
	/**
	 * Remove the last card in the hand.
	 */
	public void removeCard() {
		if (mCards[0] > 0) {
			--mCards[0];
		}
	}
	/**
	 * Remove all cards from the hand.
	 */
	public void makeEmpty() {
		mCards[0] = 0;
	}
	/**
	 * Add a card to the hand. (if there is room)
	 * @param c the card to add
	 * @return true if the card was added, false otherwise
	 */
	public boolean addCard(Card c) {
		if (c == null) {
			return false;
		}
		if (mCards[0] == MAX_CARDS) {
			return false; 
		}
		++mCards[0];
		mCards[mCards[0]] = c.getIndex();
		return true;
	}
	/**
	 * Add a card to the hand. (if there is room)
	 * @param i the index value of the card to add
	 * @return true if the card was added, false otherwise
	 */
	public boolean addCard(int i) {
		if (mCards[0] == MAX_CARDS) {
			return false; 
		}
		++mCards[0];
		mCards[mCards[0]] = i;
		return true;
	}
	/**
	 * Get the a specified card in the hand
	 * @param pos the position (1..n) of the card in the hand
	 * @return the card at position pos
	 */
	public Card getCard(int pos) {
		if (pos < 1 || pos > mCards[0]) {
			return null;
		}
		return new Card(mCards[pos]);
	}
	/**
	 * Add a card to the hand. (if there is room)
	 * @param c the card to add
	 */
	public void setCard(int pos, Card c) {
		if (mCards[0] < pos) {
			return;
		}
		mCards[pos] = c.getIndex();
	}
	/** 
	 * Obtain the array of card indexes for this hand.
	 * First element contains the size of the hand.
	 * @return array of card indexs (size = MAX_CARDS+1)
	 */
	public int[] getCardArray() {
		return mCards;
	}
	/** 
	 * Bubble Sort the hand to have cards in descending order, buy card index.
	 * Used for database indexing.
	 */
	public void sort() {
		boolean flag = true;
		while (flag) {
			flag = false;
			for (int i = 1; i < mCards[0]; ++i) {
				if (mCards[i] < mCards[i+1]) {
					flag = true;
					int t = mCards[i];
					mCards[i  ] = mCards[i+1];
					mCards[i+1] = t;
				}
			}
		}
	}
	/**
	 * Get a string representation of this Hand.
	 */
	public String toString() {
		String s = new String();
		for (int i = 1; i <= mCards[0]; ++i) {
			s += " " + getCard(i).toString();
		}
		return s;
	}
	/**
	 * Get the specified card id 
	 * @param pos the position (1..n) of the card in the hand
	 * @return the card at position pos
	 */
	public int getCardIndex(int pos) {
		return mCards[pos];
	}
}

