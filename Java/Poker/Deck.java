/***************************************************************************
Copyright (c) 2000:
      University of Alberta,
      Deptartment of Computing Science
      Computer Poker Research Group

    See "Liscence.txt"
***************************************************************************/

import java.util.Random;

/**
*  A Deck of 52 Cards which can be dealt and shuffled
*  @author  Aaron Davidson
*/

public class Deck {
    public static final int NUM_CARDS = 52;
    private char   mPosition;               // top of deck
    private Card[] mCards   = new Card[NUM_CARDS];
    private Random mRandGen = new Random();
   
    public Deck() {
        mPosition = 0;
        for (int i = 0; i < NUM_CARDS; i++) {
            mCards[i] = new Card(i);
        }
    }
   /**
    * Constructor with shuffle seed.
    * @param seed the seed to use in randomly shuffling the deck.
    */
    public Deck(long seed) {
        this();
        if (seed == 0) { 
            seed = System.currentTimeMillis();
        }
        mRandGen.setSeed(seed);
    }
    /**
     * Places all cards back into the deck.
     * Note: Does not sort the deck.
     */
    public void reset() { mPosition = 0; }
    /**
     * Shuffles the cards in the deck.
     */
    public void shuffle() {
        Card  tempCard;
        int   i, j;
        for (i = 0; i < NUM_CARDS; ++i) {
            j = i + mRandGen.nextInt(NUM_CARDS - i);
            tempCard  = mCards[j];
            mCards[j] = mCards[i];
            mCards[i] = tempCard;
        }
        mPosition = 0;
    }
    /**
     * Obtain the next card in the deck.
     * If no cards remain, a null card is returned
     * @return the card dealt
     */
    public Card deal() {
        return mPosition < NUM_CARDS ? mCards[mPosition++] : null;
    }
    public int findCard(Card c) {
        int i = mPosition;
        int n = c.getIndex();
        while (i < NUM_CARDS && n != mCards[i].getIndex()) {
            i++;
        }
        return (i < NUM_CARDS ? i : -1);
    }
    /**
     * Remove all cards in the given hand from the Deck.
     */
    public void extractHand(Hand h) {
        for (int i = 1; i <= h.size(); ++i) {
            extractCard(h.getCard(i));
        }
    }   
    /**
     * Remove a card from within the deck.
     * @param c the card to remove.
    */
    public void extractCard(Card c) {
      int i = findCard(c);
      if (i != -1) {
          Card t            = mCards[i];
          mCards[i]         = mCards[mPosition];
          mCards[mPosition] = t;
          ++mPosition;
      } else {
          System.err.println("*** ERROR: could not find card " + c);
          System.err.println(this);
          Thread.currentThread().dumpStack();
      }
    }
    /**
     * Obtain the position of the top card. 
     * (the number of cards dealt from the deck)
     * @return the top card index
     */
    public int getTopCardIndex() {
        return mPosition;
    }
    /**
     * Obtain the number of cards left in the deck
     */
    public int cardsLeft() {
        return NUM_CARDS - mPosition;
    }
    /**
     * Obtain the card at a specific index in the deck.
     * Does not matter if card has been dealt or not.
     * If i < topCardIndex it has been dealt.
     * @param i the index into the deck (0..51)
     * @return the card at position i
    */
    public Card getCard(int i) {    
        return mCards[i];
    }
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("* ");
        for (int i = 0; i < mPosition; ++i) {
            s.append(mCards[i].toString() + " ");
        }
        s.append("\n* ");
        for (int i = mPosition; i < NUM_CARDS; ++i) {
            s.append(mCards[i].toString() + " ");
        }
        return s.toString();
    }
}
