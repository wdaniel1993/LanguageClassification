package language;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DeflaterOutputStream;

public class LanguageClassification {

	public static void main(String[] args) throws IOException{
		//Path to reference files
		Path languagePath = Paths.get("input");
		//Path to text files, which get classified
		Path testFilePath = Paths.get("testfiles");
		//List of all reference languages
		List<Language> classes = new ArrayList<Language>();
		
		//Read reference files
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(languagePath);
		//Iterate over reference files
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			//Language name = file name without extension
			String languageName = file.toString().split("\\.")[0];
			String content = readFileContentFiltered(path);
			int compression = calculateCompression(content);
			
			classes.add(new Language(languageName,compression,content));
			System.out.println("name: " + languageName + ", compression: " + compression); 
		}
		
		//Test all files in testfiles directory
		directoryStream = Files.newDirectoryStream(testFilePath);
		//Iterate over all files in the test directory
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			String content = readFileContentFiltered(path);
			
			//Calculate the compression delta and use the language with the smallest delta as result
			Language language = null;
			double diffToCurrent = 0;
			for(Language curLanguage : classes){
				int compression = calculateCompression(curLanguage.getReferenceText() + " " + content);
				//Calculate compression delta
				int diff = compression - curLanguage.getCompression();
				System.out.print("File " + file.toString() + " as " + curLanguage.getName() + ": " + compression);
				System.out.println(" - Delta " + diff);
				if(diffToCurrent > diff || language == null){
					diffToCurrent = diff;
					language = curLanguage;
				}
			}
			
			//Print result
			System.out.println("File " + file.toString() + " is " + language.getName());
		}
	}
	
	/*
	 * Compresses a string and returns the compressed bytes
	 */
	public static byte[] compress(String str) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(str.getBytes("UTF-8"));
        out.close();
        return baos.toByteArray();
    }
	
	/*
	 * Calculates the compression of the given string
	 */
	public static int calculateCompression(String str) throws IOException{
		byte[] compressed = compress(str);
		return compressed.length;
    }
	
	/*
	 * Reads file content as text
	 */
	private static String readFileContent(Path file) throws IOException{
		return Files.readAllLines(file).stream().collect(Collectors.joining("\n"));
	}
	
	/*
	 * Reads File content and filters it
	 */
	private static String readFileContentFiltered(Path file) throws IOException{
		return filterString(readFileContent(file));
	}

	/*
	 * Prepares string for compression
	 * Filters special characters and converts characters to upper case
	 */
	private static String filterString(String unfiltered) {
		String filtered = unfiltered;
		//Remove special characters
		filtered = filtered.replaceAll("[^\\p{L}\\p{Nd} ]+", "");
		//Convert to upper case 
		filtered = filtered.toUpperCase();
		return filtered;
	}
}