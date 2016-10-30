package language;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DeflaterOutputStream;

public class LanguageClassification {

	public static void main(String[] args) throws IOException{
		Path languagePath = Paths.get("input");
		Path testFilePath = Paths.get("testfiles");
		List<Language> classes = new ArrayList<Language>();
		
		//Read reference values
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(languagePath);
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			String languageName = file.toString().split("\\.")[0];
			String content = readFileContentFiltered(path);
			double compressionRate = calculateCompressionRate(content);
			classes.add(new Language(languageName,compressionRate,content));
			System.out.println("name: " + languageName + ", compression rate: " + compressionRate); 
		}
		
		//Test all files in testfiles directory
		directoryStream = Files.newDirectoryStream(testFilePath);
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			String content = readFileContentFiltered(path);
			Language language = null;
			double diffToCurrent = 0;
			for(Language curLanguage : classes){
				double compressionRate = calculateCompressionRate(curLanguage.getReferenceText() + content);
				System.out.print("File " + file.toString() + " as " + curLanguage.getName() + ": " + compressionRate);
				double diff = Math.abs(curLanguage.getCompressionRate() - compressionRate);
				System.out.println(" - Delta " + diff);
				if(diffToCurrent > diff || language == null){
					diffToCurrent = diff;
					language = curLanguage;
				}
			}
			System.out.println("File " + file.toString() + " is " + language.getName());
		}
			
	}
	
	public static byte[] compress(String str) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream out = new DeflaterOutputStream(baos);
        out.write(str.getBytes("UTF-8"));
        out.close();
        return baos.toByteArray();
    }
	
	public static double calculateCompressionRate(String str) throws IOException{
		byte[] compressed = compress(str);
		double compressionRate = (double) compressed.length/str.getBytes("UTF-8").length;
		return compressionRate;
    }
	
	private static String readFileContent(Path file) throws IOException{
		return Files.readAllLines(file).stream().collect(Collectors.joining("\n"));
	}
	
	private static String readFileContentFiltered(Path file) throws IOException{
		return filterString(readFileContent(file));
	}

	private static String filterString(String unfiltered) {
		String filtered = unfiltered;
		filtered = Normalizer.normalize(filtered, Normalizer.Form.NFD).replaceAll("[^a-zA-Z]", "");
		filtered = filtered.toLowerCase();
		return filtered;
	}
	
	
}
