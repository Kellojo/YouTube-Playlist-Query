package Utility;

/* A format is used to have a display name of a given filename format and the format itself in a handy object */
public class Format {
	private String format;
	private String displayName;
	
	public Format(String format, String displayName) {
		super();
		this.format = format;
		this.displayName = displayName;
	}

	public String getFormat() {
		return format;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String toString() {
		return displayName;
	}
}
