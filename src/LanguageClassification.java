import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;

public class LanguageClassification {

	public static void main(String[] args) throws IOException{
		Path languagePath = Paths.get("input");
		Path testFilePath = Paths.get("testfiles");
		Map<String, Double> classes = new HashMap<String, Double>();
		
		//Read reference values
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(languagePath);
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			String languageName = file.toString().split("\\.")[0];
			double compressionRate = calculateCompressionRate(path);
			classes.put(languageName, compressionRate);
			System.out.println("name: " + languageName + ", compression rate: " + compressionRate); 
		}
		
		//Test all files in testfiles directory
		directoryStream = Files.newDirectoryStream(testFilePath);
		for (Path path : directoryStream) {
			Path file = path.getFileName();
			double compressionRate = calculateCompressionRate(path);
			String language = null;
			double diffToNearest = Double.MAX_VALUE;
			for(Map.Entry<String,Double> entry : classes.entrySet()){
				double diff = Math.abs(entry.getValue() - compressionRate);
				if(diffToNearest > diff){
					diffToNearest = diff;
					language = entry.getKey();
				}
			}
			System.out.println("File " + file.toString() + " is " + language);
		}
			
	}
	
	public static byte[] compress(byte[] bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream out = new DeflaterOutputStream(baos);
            out.write(bytes);
            out.close();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return baos.toByteArray();
    }
	
	public static double calculateCompressionRate(Path file) throws IOException {
		byte[] input = Files.readAllBytes(file);
		byte[] compressed = compress(input);
		double compressionRate = (double) compressed.length/input.length;
		
		return compressionRate;
    }
}
