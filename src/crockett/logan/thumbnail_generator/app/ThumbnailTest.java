package crockett.logan.thumbnail_generator.app;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
	
	// scale Tests
	@ParameterizedTest
	@MethodSource("provideInvalidDataPointsForScale")
	void scaleThrowsIllegalArgumentExceptionForInavlidWidthsAndHeights(Point p) {
		assertThrows(IllegalArgumentException.class, () -> {
			new Thumbnail(this.filePath + this.fileName).scale((int)p.getX(), (int)p.getY());
		});
	}
	
	private static Stream<Arguments> provideInvalidDataPointsForScale() {
		return Stream.of(
				Arguments.of(new Point(-1, 200)),
				Arguments.of(new Point(0, 200)),
				Arguments.of(new Point(300, -1)),
				Arguments.of(new Point(-1, 0))
				);
	}
	
	@Test
	void scaleWorksWithNoExceptionsThrown() {
		try {
			new Thumbnail(this.filePath + this.fileName).scale(300, 200);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
