/***************************************************************************
Copyright (c) 2000:
      University of Alberta,
      Deptartment of Computing Science
      Computer Poker Research Group

      See "Liscence.txt"
***************************************************************************/

import java.lang.String;

/**
* Represents a playing card
* from a set of cards {0..51} which map to cards having a suit 
*                  { 0 ..  3 } <==> { CLUBS, DIAMONDS, HEARTS, SPADES }
* and a face value { 0 .. 12 } <==> { 2 .. ACE }
* 
* @author  Aaron Davidson
*/

/* fully explicit card to integer conversions :
    
    2c =  0    2d = 13    2h = 26    2s = 39
    3c =  1    3d = 14    3h = 27    3s = 40
    4c =  2    4d = 15    4h = 28    4s = 41
    5c =  3    5d = 16    5h = 29    5s = 42
    6c =  4    6d = 17    6h = 30    6s = 43
    7c =  5    7d = 18    7h = 31    7s = 44
    8c =  6    8d = 19    8h = 32    8s = 45
    9c =  7    9d = 20    9h = 33    9s = 46
    Tc =  8    Td = 21    Th = 34    Ts = 47
    Jc =  9    Jd = 22    Jh = 35    Js = 48
    Qc = 10    Qd = 23    Qh = 36    Qs = 49
    Kc = 11    Kd = 24    Kh = 37    Ks = 50
    Ac = 12    Ad = 25    Ah = 38    As = 51

	formula: <color, rank> |-> color * 13 + rank
*/


public class Card {
    public final static int CLUBS    = 0;
    public final static int DIAMONDS = 1;
    public final static int HEARTS   = 2;
    public final static int SPADES   = 3;
    
    public final static int BAD_CARD = -1;
    public final static int TWO      =  0;
    public final static int THREE    =  1;
    public final static int FOUR     =  2;
    public final static int FIVE     =  3;
    public final static int SIX      =  4;
    public final static int SEVEN    =  5;
    public final static int EIGHT    =  6;
    public final static int NINE     =  7;
    public final static int TEN      =  8;
    public final static int JACK     =  9;
    public final static int QUEEN    = 10;
    public final static int KING     = 11;
    public final static int ACE      = 12;
    
    public final static int NUM_SUITS = 4;
    public final static int NUM_RANKS = 13;
    public final static int NUM_CARDS = 52;
   
    private int mIndex;

    /**
     * Constructor -- makes an empty card.
     */
    public Card() {
        mIndex = -1;
    }
   
    /**
     * Constructor.
     * @param rank face value of the card
     * @param suit suit of the card
     */
    public Card(int rank, int suit) {
        mIndex = toIndex(rank, suit);
    }
   
    /**
     * Constructor.
     * Creates a Card from an integer index {0..51}
     * @param index integer index of card between 0 and 51
     */
    public Card(int index) {
        if (index >= 0 && index < NUM_CARDS)
            mIndex = index;
        else
            mIndex = BAD_CARD;
    }   
   
	/**
	 * Creates a card from a string representation like, e.g., "As".
	 */
    public Card(String s) {
        if (s.length() == 2) {
            mIndex = chars2index(s.charAt(0), s.charAt(1));
		}
    }
   
    /**
     * Constructor.
     * Creates a card from its character based representation.
     * @param rank the character representing the card's rank
     * @param suit the character representing the card's suit
    */
    public Card(char rank, char suit) {
        mIndex = chars2index(rank, suit);
    }
   
    private int chars2index(char rank, char suit) {
        int r = -1;
        switch (rank) {
           case '2': r = TWO;    break;
           case '3': r = THREE;  break;
           case '4': r = FOUR;   break;
           case '5': r = FIVE;   break;
           case '6': r = SIX;    break;
           case '7': r = SEVEN;  break;
           case '8': r = EIGHT;  break;
           case '9': r = NINE;   break;
           case 'T': r = TEN;    break;
           case 'J': r = JACK;   break;
           case 'Q': r = QUEEN;  break;
           case 'K': r = KING;   break;
           case 'A': r = ACE;    break;
           case 't': r = TEN;    break;
           case 'j': r = JACK;   break;
           case 'q': r = QUEEN;  break;
           case 'k': r = KING;   break;
           case 'a': r = ACE;    break;
        }
        int s = -1;
        switch (suit) {
           case 'h': s = HEARTS;   break;
           case 'd': s = DIAMONDS; break;
           case 's': s = SPADES;   break;
           case 'c': s = CLUBS;    break;
           case 'H': s = HEARTS;   break;
           case 'D': s = DIAMONDS; break;
           case 'S': s = SPADES;   break;
           case 'C': s = CLUBS;    break;
        }
        if (s != -1 && r != -1) {
            return toIndex(r, s);
		}
        else return BAD_CARD;
    }
    /**
     * Return the integer index for this card.
     * @return the card's index value
     */
    public int getIndex() {
        return mIndex;
    }
    /**
     * Change the index of the card.
     * @param index the new index of the card
     */
    public void setIndex(int index) {
        mIndex = index;
    }
    /**
     * convert a rank and a suit to an index
     * @param rank the rank to convert
     * @param suit the suit to convert
     * @return the index calculated from the rank and suit
     */
    public static int toIndex(int rank, int suit) {
        return NUM_RANKS * suit + rank;
    }
    /**
     * Change this card to another. This is more practical 
     * than creating a new object for optimization reasons.
     * @param rank face value of the card
     * @param suit suit of the card
     */
   public void setCard(int rank, int suit) {
       mIndex = toIndex(rank, suit);
   }
   /**
    * Obtain the rank of this card
    * @return rank
    */
    public int getRank() {
        return mIndex % NUM_RANKS;
    }
    /**
     * Obtain the rank corresponding to this index
     * @return rank
     */
    public static int getRank(int i) {
        return i % NUM_RANKS;
    }
    /**
     * Obtain the suit of this card
     * @return suit
     */
    public int getSuit() {
        return mIndex / NUM_RANKS;
    }
    /**
     * Obtain the suit corresponding to this index
     * @return suit
     */
    public final static int getSuit(int i) {
        return i / NUM_RANKS;
    }
    /**
     * Obtain a String representation of this Card
     * @return A string for this card
     */
    public String toString() {
        String s = new String();
        s += getRankChar(getRank());
        switch (getSuit()) {
           case HEARTS:   s += 'h'; break;
           case DIAMONDS: s += 'd'; break;
           case CLUBS:    s += 'c'; break;
           case SPADES:   s += 's'; break;
      }
      return s;
	}
	/**
	 * Get the rank character corresponding to the numerical rank.
	 * @param  r numerical rank, 2 is 0, A = 12.
	 * @return character representation of this rank
	 */
	public static char getRankChar(int r) {
		char s;
		switch (r) {
		    case ACE:   s = 'A'; break;
            case KING:  s = 'K'; break;
            case QUEEN: s = 'Q'; break;
            case JACK:  s = 'J'; break;
            case TEN:   s = 'T'; break;
            default:    s = Character.forDigit(r + 2, Character.MAX_RADIX); break;
		}
		return s;
	}
}

