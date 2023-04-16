package crockett.logan.thumbnail_generator.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

public class Thumbnail {
	private BufferedImage thumbnail;
	
	/**
	 * Creates a new Thumbnail based on the provided file path
	 * @param filePath file path of the image to read
	 * @throws IllegalArgumentException if the file path does not exist
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException if the file path is null
	 */
	public Thumbnail(String filePath) throws IllegalArgumentException, IOException, NullPointerException {
		File imageFile = new File(filePath);
		
		if (imageFile.isDirectory()) {
			throw new IllegalArgumentException("File " + filePath + " does not exist.");
		}
		
		FileImageInputStream inputImageStream = null;
		try {
			inputImageStream = new FileImageInputStream(imageFile);
			this.thumbnail = ImageIO.read(inputImageStream);
		}
		finally {
			if (this.thumbnail == null && inputImageStream != null) {
				inputImageStream.close();
			}
		}
		
	}
	
	/**
	 * Writes a Thumbnail to the File System
	 * @param filePath file path to write to
	 * @param outputFormat file format
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException if the filePath is null
	 */
	public void writeThumbnailToFileSystem(String filePath, String outputFormat) throws IOException, NullPointerException {
		File outputFile = new File(filePath);
		
		FileImageOutputStream outputStream = null;
		try {
			outputStream = new FileImageOutputStream(outputFile);
			ImageIO.write(this.thumbnail, outputFormat, outputStream);
		}
		finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
}
