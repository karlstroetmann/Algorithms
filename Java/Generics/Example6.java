import java.util.*; 

public class Example6 {

    static void Array2List1(Object[] a, List<?> l) {
        for (Object o: a) {
            // l.add(o);
        }
    }

    static <T> void Array2List2(T[] a, List<T> l) {
        for (T t: a) {
            l.add(t);
        }
    }

    static <T, S extends T> void copy1(List<S> src, List<T> dst) {
		for (S s: src) {
			dst.add(s);
		}
    }

    static <T> void copy2(List<? extends T> src, List<T> dst) {
		for (T t: src) {
			dst.add(t);
		}
    }
}
