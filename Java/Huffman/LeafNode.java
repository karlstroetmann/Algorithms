import java.util.*;

public class LeafNode extends Node {
    private char mCharacter;
    private int  mFrequency;
    
    public LeafNode(char character, int frequency) {
        mCharacter = character;
        mFrequency = frequency;
    }
    public Integer cost() {
        return 0;
    }
    public Integer count() {
        return mFrequency;
    }
    public Character getCharacter() {
        return mCharacter;
    }
    public String toString() {
        return mCharacter + "";
    }

    protected ArrayList<String> toStringArray() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(" : " + mCharacter);
        return result;
    }
}