import java.io.File;
import java.util.List;

import Utility.FileWriter;
import Utility.FilenameFormatParser;
import Utility.Format;
import Youtube.Playlist;
import Youtube.Video;
import javafx.concurrent.Task;

public class MusicRenamer extends Task<Object> {
	
	private Playlist playlist;
	private Format fileNameFormat;
	
	public MusicRenamer(Playlist playlist, Format fileNameFormat) {
		this.playlist = playlist;
		this.fileNameFormat = fileNameFormat;
	}
	
	/* Add playlist index to the files */
	private void renameFiles() {	
		List<Video> videos = playlist.getVideos();
		int leadingZeros = Integer.toString(videos.size()).toString().length();
		
		for (Video video : videos) {
			
			File fileOnDisk = video.getVideoOnDisk();
			//System.out.println((fileOnDisk == null) + " - " + video.getVideoOnDisk());
			
			if (fileOnDisk == null) {
				continue;
			}
			
			//Some funky code to make sure we get the right extension even when we don't have one or have a lot of dots in our extension
			String extension = FileWriter.getFileExtension(fileOnDisk);
			if (extension.trim().equals("") || extension.length() > 10)
				extension = "mp3";
			extension = extension.replace(".", "");
			extension = "." + extension;
			
			//get final file name
			String finalName = FilenameFormatParser.getFileName(fileNameFormat, video, leadingZeros) + extension;
			//remove unwanted chars from filename
			finalName = FileWriter.removeNotAllowedCharsFromFilename(finalName);
			
			//rename file
			File destination = new File(fileOnDisk.getParentFile().getAbsolutePath() + "\\" + finalName);
			
			//check if the operation was successfull
			boolean isSuccess = fileOnDisk.renameTo(destination);
			if (!isSuccess) {
				System.err.println("File '" + fileOnDisk + "' could not be renamed to file '" + destination + "'.");
				
				//delete file if it already exists
				if (destination.exists()) {
					fileOnDisk.delete();
				}
			}
		}
		
	}

	@Override
	protected Object call() throws Exception {
		renameFiles();
		return null;
	}
	
	
	
	
	
}
