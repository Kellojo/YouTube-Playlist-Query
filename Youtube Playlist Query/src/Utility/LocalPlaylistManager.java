package Utility;
import java.io.File;

public class LocalPlaylistManager {

	private String location = "";
	
	
	/* Updates the playlist location and return true or false, depending on the validity of the given file */
	public boolean UpdateLocation(File newLocation) {
		boolean isValid = newLocation != null &&
				newLocation.exists();
		
		if (isValid) {
			location = newLocation.getAbsolutePath() + "\\";
			Settings.setLastUsedFileLocation(location);
		}
		
		return isValid && isValid();
	}
	
	public File getLocation() {
		return new File(location);
	}
	public boolean isValid() {
		return location.trim() != "" && getLocation().exists();
	}
	@Override
	public String toString() {
		if (location != null) {
			return getLocation().getAbsolutePath();
		} else {
			return "";
		}
	}
}
