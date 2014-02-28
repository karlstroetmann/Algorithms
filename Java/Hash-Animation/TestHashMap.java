public class TestHashMap
{
	public static void main(String[] args) {
		Integer[] dummy = { 0 };
		CreateArray<Integer> creator = new CreateArray<Integer>(dummy);
		Integer[] intArray = creator.createArray(10);
		System.out.println(intArray);
	}
}
