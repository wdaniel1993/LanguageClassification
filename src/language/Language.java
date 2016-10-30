package language;

public class Language {
	private double compressionRate;
	private String referenceText;
	private String name;
	public double getCompressionRate() {
		return compressionRate;
	}
	public void setCompressionRate(double compressionRate) {
		this.compressionRate = compressionRate;
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
	
	public Language(String name, double compressionRate, String referenceText) {
		super();
		this.compressionRate = compressionRate;
		this.referenceText = referenceText;
		this.name = name;
	}
}
