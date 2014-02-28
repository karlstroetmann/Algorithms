import java.util.*; 

public class Example15 { 

    static <T> T writeAll(List<T> src, Sink<? super T> snk) {
        T last = null;
        for (T t: src) {
            last = t;
            snk.flush(last);
        }
        return last;
    }

    public static void main(String[] args) { 
        Sink<Object> s  = new Sink();
        List<String> sl = new LinkedList<String>();
        String str = writeAll(sl, s); // finally it works
    }
}

class Sink<T> {
    void flush(T t) {
	// more code here
    }
}

