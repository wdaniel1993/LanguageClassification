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
		Path languagePath = Paths.get("input");
		Path testFilePath = Paths.get("testfiles");
		List<Language> classes = new ArrayList<Language>();
		
		//Read reference values
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(languagePath);
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			String languageName = file.toString().split("\\.")[0];
			String content = readFileContent(path);
			double compressionRate = calculateCompressionRate(content);
			classes.add(new Language(languageName,compressionRate,content));
			System.out.println("name: " + languageName + ", compression rate: " + compressionRate); 
		}
		
		//Test all files in testfiles directory
		directoryStream = Files.newDirectoryStream(testFilePath);
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			String content = readFileContent(path);
			Language language = null;
			double diffToNearest = Double.MAX_VALUE;
			for(Language curLanguage : classes){
				double compressionRate = calculateCompressionRate(curLanguage.getReferenceText() + content);
				double diff = compressionRate - curLanguage.getCompressionRate();
				if(diffToNearest > diff){
					diffToNearest = diff;
					language = curLanguage;
				}
			}
			System.out.println("File " + file.toString() + " is " + language.getName());
		}
			
	}
	
	public static byte[] compress(String str) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(str.getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }
	
	public static double calculateCompressionRate(String str){
		byte[] compressed = compress(str);
		double compressionRate = (double) compressed.length/str.getBytes().length;
		return compressionRate;
    }
	
	public static String readFileContent(Path file) throws IOException{
		return Files.readAllLines(file).stream().collect(Collectors.joining("\n"));
	}
}
