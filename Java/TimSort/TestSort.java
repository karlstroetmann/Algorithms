import java.util.*;

public class TestSort 
{
    public static void main(String[] args) 
    {
        final int arraySize   = 2000;
        final int numberTests = 100;

        Double[]  array   = new Double[arraySize];
        Random    randGen = new Random(0);

        for (int k = 0; k < numberTests; ++k) {
            System.out.println("Test number " + k);
            for (int i = 0; i < arraySize; ++i) {
                array[i] = Math.floor(randGen.nextDouble() * 100);
                // array[i] = (double) (i % 10);
            }
            SortingAlgorithm sortAlg = new SimplifiedTimSort(array);
	    sortAlg.testSort();
        }
    }
}
