//This class calculates  diffrent needed coordinates for the stack

import java.util.*;

public class calcUtil {
	
	private static int yhigh = 20;
	private static int lettersize = 10;
		
	   public static Double roundTo6Digits(Double Zahl) {
			Zahl *= 1000000;
			Zahl = Math.ceil(Zahl);
			return Zahl /= 1000000;
	    }
	   
	   public static int relativStackKoord(int YStart, LinkedList LL) {
		   return YStart - LL.size() * yhigh;
	   }
	   
	   public static int relativStackKoord(int YStart, int AnzElement) {
		   return YStart - AnzElement * yhigh;
	   }
	   
	   public static int relativXPosition(int xPos, StringElement strE) {
		   return xPos += lettersize * (1 + strE.getStrLength());
	   }
	   
	   public static int relativXPosition(int xPos,int strLength) {
		   return xPos += lettersize * (1 + strLength);
	   }

}
