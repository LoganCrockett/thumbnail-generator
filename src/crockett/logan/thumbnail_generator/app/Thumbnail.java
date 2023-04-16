package crockett.logan.thumbnail_generator.app;

import java.awt.Graphics2D;
import java.awt.Image;
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
	 * Gets the width of the current thumbnail
	 * @return width of the thumbnail
	 */
	public int getWidth() {
		return this.thumbnail.getWidth();
	}
	
	/**
	 * Gets the height of the current thumbnail
	 * @return height of the thumbnail
	 */
	public int getHeight() {
		return this.thumbnail.getHeight();
	}
	
	/**
	 * Writes a Thumbnail to the File System
	 * @param filePath file path to write to
	 * @param outputFormat file format
	 * @throws IOException if an I/O error occurs
	 * @throws NullPointerException if the filePath is null
	 */
	public void writeThumbnailToFileSystem(String filePath, String outputFormat) throws IOException, NullPointerException {
		if (outputFormat == null) {
			throw new IllegalArgumentException("Output format cannot be null");
		}
		
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
	
	/**
	 * Scales an image to the desired width and height
	 * @param width new width of the image (not < 1)
	 * @param height new height of the image (not < 1)
	 * @throws IllegalArgumentException if the width or height is < 1
	 */
	public void scale(int width, int height) throws IllegalArgumentException {
		Image scaledImage = this.thumbnail.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		BufferedImage scaledBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g = (Graphics2D)scaledBufferedImage.getGraphics();
		g.drawImage(scaledImage, 0, 0, width, height, null);
		
		this.thumbnail = scaledBufferedImage;
		g.dispose();
	}
	
	/**
	 * Crops the current thumbnail down to the region specified
	 * @param startingX starting x position
	 * @param startingY starting y position
	 * @param width width of the cropped image
	 * @param height height of the cropped image
	 * @throws IllegalArgumentException if any parameters is less than zero, of if the parameters exceed the current image dimensions
	 */
	public void crop(int startingX, int startingY, int width, int height) throws IllegalArgumentException {
		if (startingX < this.thumbnail.getMinX() || startingX > this.thumbnail.getWidth()) {
			throw new IllegalArgumentException("Starting X must be inside current image bounds");
		}
		
		if (startingY < this.thumbnail.getMinY() || startingY > this.thumbnail.getHeight()) {
			throw new IllegalArgumentException("Starting Y must be inside current image bounds");
		}
		
		if (width < 0 || width > this.thumbnail.getWidth()) {
			throw new IllegalArgumentException("Width must be non-zero and at least equal to the current image width");
		}
		
		if (height < 0 || height > this.thumbnail.getHeight()) {
			throw new IllegalArgumentException("Height must be non-zero and at least equal to the current image height");
		}
		
		if (startingX + width > this.thumbnail.getWidth()) {
			throw new IllegalArgumentException("Current starting position and width exceeds current bounds");
		}
		
		if (startingY + height > this.thumbnail.getHeight()) {
			throw new IllegalArgumentException("Current starting position and height exceeds current bounds");
		}
		
		this.thumbnail = this.thumbnail.getSubimage(startingX, startingY, width, height);
	}
}
