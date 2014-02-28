import java.util.*;

public class TestSort 
{
    public static void main(String[] args) 
    {
        final int arraySize   = 1000;
        final int numberTests = 10;

        Double[]  array   = new Double[arraySize];
        Random    randGen = new Random(0);

        Comparator<Double> comp  = new
            Comparator<Double>()
            {
                public int compare(Double x, Double y) {
                    return x.compareTo(y);
                }
            };

        for (int k = 0; k < numberTests; ++k) {
            System.out.println("Test number " + k);
            for (int i = 0; i < arraySize; ++i) {
                array[i] = randGen.nextDouble();
                // array[i] = (double) (i % 10);
            }
            SortingAlgorithm sortAlg = new MinSortAlgorithm(array, comp);
            sortAlg.testSort();
        }
    }
}
