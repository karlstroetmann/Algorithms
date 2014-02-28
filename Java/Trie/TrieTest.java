public class TrieTest
{
	public static void main(String[] args) 
	{
		TrieNode<String> trie = new TrieNode<String>();
		trie.insert("Thomas", "it04132");
		trie.insert("Judit", "it04133");
		trie.insert("Melanie", "it04136");
		trie.insert("Axel", "it04126");
		trie.insert("Timo", "it04122");
		trie.insert("Matthias", "it04120");
		trie.insert("Michelle", "it04131");
		trie.insert("Tanja", "it04135");
		trie.insert("Heiko", "it04127");
		trie.insert("Hubert", "it04123");
		trie.insert("Manuel", "it04134");
		trie.insert("Jens", "it04121");
		trie.insert("Tobias", "it04138");
		trie.insert("Patricia", "it04137");
		trie.insert("Marcel", "it04129");
		trie.insert("Holger", "it04124");
		trie.insert("Niels", "it04125");
		trie.insert("Daniela", "it04130");
		trie.insert("Lion", "it04119");
		trie.insert("Michael", "it04118");
		trie.insert("Jan", "it04128");
		System.out.println(trie);
		System.out.println(trie.find("Jens"));
		trie.delete("Jens");
		System.out.println(trie.find("Jens"));		
	}
	
}
