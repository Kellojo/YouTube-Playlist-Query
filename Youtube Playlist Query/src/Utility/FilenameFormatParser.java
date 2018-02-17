package Utility;
import Youtube.Video;

public class FilenameFormatParser {

	public static String getFileName(Format format, Video video, int leadingZeros) {
		String filename = format.getFormat();
		filename = filename.replace("%T", video.getTitle());
		filename = filename.replace("%CID", video.getChannelId());
		filename = filename.replace("%PA", video.getPublishedAt());
		filename = filename.replace("%ID", video.getVideoID());
		filename = filename.replace("%PIND", String.format("%0" + leadingZeros + "d", video.getPositionInPlaylist()));
		
		return filename;
	}
}
