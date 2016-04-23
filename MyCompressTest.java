import java.io.IOException;

/**
 * A Program to compress and decompress a given text file
 * Algorithm used: Huffman + Self Developed 
 * Result: 21.2% Compressed Text File! whereas huffman algorithm gives 17.5% compression
 * @author Aishwary Pramanik
 * @author Shashank Gangadhara
 * @version 2.0 (Previous version: Huffman Algorithm Implementation)
 */


public class MyCompressTest	{
	public static void main( String args[] ) {
		StringZipInputStream stringZipInputStream=new StringZipInputStream();
		try {
			stringZipInputStream.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		int i=0;
	}
}