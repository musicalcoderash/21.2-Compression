/**
 * A Program to compress and decompress a given text file
 * Algorithm used: Huffman + Self Developed
 * @author Aishwary Pramanik
 * @author Shashank Gangadhara
 * @version 2.0 (Previous version: Huffman Algorithm Implementation)
 */

import java.io.*;
import java.util.*;

// A class to compress and Decompress the text files
final class StringZipOutputStream implements java.io.Serializable {

	public static Map<Character, Integer> freqMap = new HashMap<Character, 
			Integer>();
	public static StringBuffer encodedMessage = new StringBuffer();
	public static Map<Character, String> charCode = new HashMap<>();
	public static String inputFileName = "words.txt";
	public static String encodedFileName;
	public static String decodedFileName = "DecodedFile.txt";

	
	
	// A method to set the character code value
	public void setCharCode(Map<Character, String> charCode1) {
		this.charCode1 = charCode1;
	}

	public Map<Character, String> charCode1 = new HashMap<>();

	//Inner class to perform binary tree node operations
	private static class NodeH {
		char ch;
		int frequency;
		NodeH left;
		NodeH right;

		NodeH(char ch, int frequency, NodeH left, NodeH right) {
			this.ch = ch;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}
	}
	//Inner class to compare nodes
	private static class HuffManComparator implements Comparator<NodeH> {
		@Override
		public int compare(NodeH node1, NodeH node2) {
			return node1.frequency - node2.frequency;
		}
	}
	//A method to start compression 
	public static void compress() throws IOException {
		final NodeH root = buildTree(freqMap);
		charCode.putAll(generateCodes(freqMap.keySet(), root));
	}
	// A method to get the frequency of characters
	private static Map<Character, Integer> getCharFrequency(String sentence) {
		final Map<Character, Integer> map = new HashMap<Character, Integer>();
		char ch;
		int val;
		for (int i = 0; i < sentence.length(); i++) {
			ch = sentence.charAt(i);
			if (!map.containsKey(ch)) {
				map.put(ch, 1);
			} else {
				val = map.get(ch);
				map.put(ch, ++val);
			}
		}
		return map;
	}


	// A method to build the tree
	private static NodeH buildTree(Map<Character, Integer> map) {
		Vector<NodeH> nodeVector = createNodeQueue(map);

		while (nodeVector.size() > 1) {
			final NodeH node1 = nodeVector.get(0);
			final NodeH node2 = nodeVector.get(1);
			nodeVector.remove(0);
			nodeVector.remove(0);
			NodeH node = new NodeH('\0', node1.frequency + node2.frequency, 
					node1, node2);
			nodeVector.add(node);
		}
		return nodeVector.get(0);
	}
	// A method to create node queue
	private static Vector<NodeH> createNodeQueue(Map<Character, Integer> map) {
		Vector<NodeH> vector = new Vector<>();
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			vector.add(new NodeH(entry.getKey(), entry.getValue(), null, null));
		}
		vector.sort(new HuffManComparator());
		return vector;
	}
	// A method to generate relative codes
        private static Map<Character, String> generateCodes(Set<Character> chars,
                        NodeH node) {
                final Map<Character, String> map = new HashMap<Character, String>();
                doGenerateCode(node, map, "");
                return map;
        }

        private static void doGenerateCode(NodeH node, Map<Character, String> map,
                        String s) {
                if (node.left == null && node.right == null) {
                        map.put(node.ch, s);
                        return;
                }
                doGenerateCode(node.left, map, s + '0');
                doGenerateCode(node.right, map, s + '1');
        }

	// A method to encode the messsage
	private static String encodeMessage(Map<Character, String> charCode, 
			String sentence) throws IOException {
		final StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < sentence.length(); i++) {
			stringBuilder.append(charCode.get(sentence.charAt(i)));
		}
		return stringBuilder.toString();
	}
	//A method to use serializability concept
	private static void serializeMessage(String message) throws IOException {
		final BitSet bitSet = getBitSet(message);

		try (ObjectOutputStream oos = new ObjectOutputStream(new 
				FileOutputStream( encodedFileName))) {
			oos.writeObject(bitSet);
			//			int t1=bitSet.size();
			//			int t2 =bitSet.length();
			//			System.out.println(t1 +" ,"+ t2);
		}
	}

	private static BitSet getBitSet(String message) {
		final BitSet bitSet = new BitSet();
		int i = 0;
		for (i = 0; i < message.length(); i++) {
			if (message.charAt(i) == '0') {
				bitSet.set(i, false);
			} else {
				bitSet.set(i, true);
			}
		}
		bitSet.set(i, true); 
		return bitSet;
	}
	// A method to encode the messsage
	public boolean decodeMessage(Map<Character, String> charCode) throws 
	IOException {
		StringBuffer temp = new StringBuffer();

		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new 
					FileInputStream(  encodedFileName));
			BitSet bitSet = (BitSet) objectInputStream.readObject();

			for (int i = 0; i < bitSet.length() - 1; i++) {
				if (bitSet.get(i)) {
					temp.append('1');
				} else {
					temp.append('0');
				}
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		FileWriter fileWriter = new FileWriter(new File( decodedFileName));


		String value = "";
		for (char c : temp.toString().toCharArray()) {
			value = value + c;
			if (charCode.containsValue(value)) {
				for (Map.Entry<Character, String> e : charCode.entrySet()) {
					if (e.getValue().equals(value)) {
						value = e.getKey() + "";
						break;
					}
				}
				fileWriter.append(value);
				value = "";
			} else {
				continue;
			}
		}
		System.out.println("Decompression done!!\n\n");
		fileWriter.close();
		return true;
	}
	
	// Main method. Programs starts here
	public static void start() throws IOException, 
	ClassNotFoundException, InterruptedException {
		StringZipOutputStream compressObject = new StringZipOutputStream();
		Scanner scanner = new Scanner(System.in);
		int option;
		String fileNameToCompress;
		System.out.println("Enter file name to compress");
		fileNameToCompress = scanner.next();
		int len=fileNameToCompress.length();
		String fileName="";
		for (int index=0;index<len-4;index++)
			fileName=fileName+fileNameToCompress.charAt(index);
		encodedFileName = fileName + ".encoded";
		decodedFileName = fileName + ".decoded.txt";
		double bytes = new File(fileNameToCompress).length();
		System.out.println("File Size(Before Compression):"+bytes/(1024)+"KB");
		option=0;
		while(option!=3){
			System.out.println("\nMENU:\n\t1>.Compress\n\t2>.Decompress\n\t"
					+ "3>.Exit\n");
			option = scanner.nextInt();
			String line;
			BufferedReader bufferedReader = null;

			if (option == 1) {
				System.out.println("Compression Started!!.....");
				bufferedReader = new BufferedReader(new FileReader( 
						fileNameToCompress));

				while ((line = bufferedReader.readLine()) != null) {
					line = line + "\n";
					freqMap.putAll(compressObject.getCharFrequency(line));
				}
				//Thread.sleep(10000);
                                StringZipOutputStream.compress();
                                bufferedReader = new BufferedReader(new FileReader(fileNameToCompress));
				while ((line = bufferedReader.readLine()) != null) {
					line = line + "\n";
					encodedMessage.append(compressObject.encodeMessage(charCode, 
							line));
				}
				compressObject.serializeMessage(encodedMessage.toString());
				compressObject.setCharCode(charCode);
				FileOutputStream fileOut =
						new FileOutputStream( fileNameToCompress + ".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(compressObject);
				out.close();
				fileOut.close();
				System.out.println("Compression done!!\n");
				double bytes_enc = new File(encodedFileName).length();
				System.out.println("File Size(After Compression):"+
						bytes_enc/(1024)
						+"KB\n");
			} else if (option == 2) {
				StringZipOutputStream encode;
				FileInputStream fileIn = new FileInputStream(  
						fileNameToCompress + ".ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				System.out.println("Decompression Started!!.....");
				encode = (StringZipOutputStream) in.readObject();
				in.close();
				fileIn.close();
				compressObject.decodeMessage(encode.charCode1);

			}
		}


	}

}