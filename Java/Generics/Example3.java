import java.util.*;

public class Example3 {

    public static void main(String[] args) {
        List<String> stringList = new LinkedList<String>();
		List<Object> objectList = stringList; 
		objectList.add(new Integer(666));
		String hell = stringList.get(0);
    }
}