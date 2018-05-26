package Youtube;

import java.util.ArrayList;
import java.util.List;

public class YoutubeUtility {

	/* Gets a List of videos (ids) that are not present on disk */
	public static List<String> getMissingVideosOnDisk(Playlist playlist) {
		if (playlist == null)
			return null;
		
		List<Video> videos = playlist.getVideos();
		List<String> missingVideos = new ArrayList<String>();
		
		for (Video v : videos) {
			if (v.getVideoOnDisk() == null) {
				missingVideos.add(v.getVideoID());
				
			}
		}
		
		return missingVideos;
	}
	/* Gets a List of all videos ids in a given playlist */
	public static List<String> getVideoIDs(Playlist playlist) {
		if (playlist == null)
			return null;
		
		List<Video> videos = playlist.getVideos();
		List<String> missingVideos = new ArrayList<String>();
		
		for (Video v : videos) {
				missingVideos.add(v.getVideoID());
		}
		
		return missingVideos;
	}


}
