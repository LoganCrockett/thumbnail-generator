package crockett.logan.thumbnail_generator.app;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class ThumbnailTest {
	
	private final String filePath = "resources\\images\\";
	private final String fileName = "orion.jpg";

	// Constructor Tests
	@Test
	void constructorThrowsIllegalArgumentExceptionForMissingFile() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Thumbnail(this.filePath);
		});
	}
	
	@Test
	void constructorThrowsNullPointerExceptionForNullFilePath() {
		assertThrows(NullPointerException.class, () -> {
			new Thumbnail(null);
		});
	}
	
	@Test
	void ableToConstructInstance() {
		Thumbnail t;
		try {
			t = new Thumbnail(this.filePath + this.fileName);
			assertNotNull(t);
		}
		catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	// writeThumbnailToFileSystem Tests
	@Test
	void writeThumbnailToFileSystemThrowsNullPointerExceptionForNullFilePath() {
		assertThrows(NullPointerException.class, () -> {
			Thumbnail t = new Thumbnail(this.filePath + this.fileName);
			t.writeThumbnailToFileSystem(null, "png");
		});
	}
	
	@Test
	void writeThumbnailToFileSystemThrowsIllegalArgumentExceptionForNullFileFormat() {
		assertThrows(IllegalArgumentException.class, () -> {
			Thumbnail t = new Thumbnail(this.filePath + this.fileName);
			t.writeThumbnailToFileSystem(this.filePath + "testing.png", null);
		});
	}

	@Test
	void writeThumbnailToFileSystemSuccessfullyWrites() {
		String testFilePath = this.filePath + "test.png";
		Thumbnail t;
		try {
			t = new Thumbnail(this.filePath + this.fileName);
			t.writeThumbnailToFileSystem(testFilePath, "png");
			
			File verify = new File(testFilePath);
			
			assertTrue(verify.exists());
		}
		catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
