package Utility;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileWriter {

	/* Write lines to given file */
	public static void writeLinesToFile(List<String> lines, String savePath, String filename) {
		if (lines == null) {
			System.err.println("No lines were written.");
			return;
		}
		
		try {
			PrintWriter writer = new PrintWriter(savePath + filename, "UTF-8");
			for (String s : lines) {
				writer.println(s);
			}
			writer.close();
		} catch (IOException e) {
			System.err.println("Error while writing file to disk.");
			System.err.print(e.getStackTrace());
		}
	}
	
	/* Get the extension of a file */
	public static String getFileExtension(File file) {
		String path = file.getPath();
		int dot = path.lastIndexOf(".");
	    return path.substring(dot + 1);
    }
	
	/* Removes chars from the filename that are not allowed */
	public static String removeNotAllowedCharsFromFilename(String filename) {
		return filename.replaceAll("[\\\\/:*?\"<>|]", "");
	}
}
