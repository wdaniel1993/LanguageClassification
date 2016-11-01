package language;

/**
 * Language 
 * Represents a language with a reference text and compression
 *
 */
public class Language {
	private int compression;
	private String referenceText;
	private String name;
	
	public int getCompression() {
		return compression;
	}
	public void setCompressionRate(int compression) {
		this.compression = compression;
	}
	public String getReferenceText() {
		return referenceText;
	}
	public void setReferenceText(String referenceText) {
		this.referenceText = referenceText;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Language(String name, int compression, String referenceText) {
		super();
		this.compression = compression;
		this.referenceText = referenceText;
		this.name = name;
	}
}
