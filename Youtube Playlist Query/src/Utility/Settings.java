package Utility;

import java.util.prefs.Preferences;

public class Settings {
	
	private static Preferences settings;
	
	private static final String PropertyName_LastUsedPlaylistID = "LastUsedPlaylistID";
	private static final String PropertyName_LastUsedYTApiKey = "LastUsedApiKey";
	private static final String PropertyName_LastUsedFileLocation = "LastUsedFileLocation";
	
	private static final String Default_YTApiKey = "AIzaSyDSs6VwJdOUUyoSYN4ZZcd0PlzsHobqfDE";
	
	
	// Getters ------------------------------------------------------------------------
	
	/* Get the last used playlist id */
	public static String getLastUsedPlaylistID() {
		return getSettings().get(PropertyName_LastUsedPlaylistID, "");
	}
	/* Get the last used Youtube Api Key */
	public static String getLastUsedApiKey() {
		return getSettings().get(PropertyName_LastUsedYTApiKey, Default_YTApiKey);
	}
	/* Get the last used file location */
	public static String getLastUsedFileLocation() {
		return getSettings().get(PropertyName_LastUsedFileLocation, "");
	}
	
	// Setters ------------------------------------------------------------------------
	
	/* Set the last used playlist id */
	public static void setLastUsedPlaylistID(String playlistID) {
		getSettings().put(PropertyName_LastUsedPlaylistID, playlistID);
	}
	/* Set the last used Youtube Api Key */
	public static void setLastUsedApiKey(String apiKey) {
		getSettings().put(PropertyName_LastUsedYTApiKey, apiKey);
	}
	/* Set the last used file location */
	public static void setLastUsedFileLocation(String path) {
		getSettings().put(PropertyName_LastUsedFileLocation, path);
	}
	
	// Utility ------------------------------------------------------------------------
	
	/* Get Preferences for our class */
	private static Preferences getSettings() {
        if (settings == null) {
        	settings = Preferences.userNodeForPackage(Settings.class);
        }
        return settings;
    }
}
