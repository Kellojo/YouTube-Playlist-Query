package Youtube;

public class YouTubeAPIKey {
	private String key = "";

	
	
	public YouTubeAPIKey(String key) {
		super();
		this.key = key.trim();
	}
	
	public boolean isValid() {
		return key.trim().length() > 38;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String toString() {
		return key;
	}
}
