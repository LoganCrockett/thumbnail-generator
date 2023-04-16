package crockett.logan.thumbnail_generator.app;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ThumbnailTest {
	
	private final String filePath = "resources\\images\\";
	private final String fileName = "orion.jpg";
	private final String outputFilePath = this.filePath + "output\\";
	
	private Thumbnail thumbnail;
	
	@BeforeEach
	void beforeEach() {
		try {
			this.thumbnail = new Thumbnail(this.filePath + this.fileName);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@AfterEach
	void afterEach() {
		this.thumbnail = null;
	}

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
			this.thumbnail.writeThumbnailToFileSystem(null, "png");
		});
	}
	
	@Test
	void writeThumbnailToFileSystemThrowsIllegalArgumentExceptionForNullFileFormat() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.writeThumbnailToFileSystem(this.outputFilePath + "testing.png", null);
		});
	}

	@Test
	void writeThumbnailToFileSystemSuccessfullyWrites() {
		String testFilePath = this.outputFilePath + "test.png";

		try {
			this.thumbnail.writeThumbnailToFileSystem(testFilePath, "png");
			
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
			this.thumbnail.scale((int)p.getX(), (int)p.getY());
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
			this.thumbnail.scale(300, 200);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	// crop Tests
	@Test
	void cropThrowsIllegalArgumentExceptionForNegativeStartingX() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(-1, 0, 200, 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForNegativeStartingY() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, -1, 200, 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForNegativeWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, 0, -200, 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForNegativeHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, 0, 200, -300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForTooBigStartingX() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(this.thumbnail.getWidth() + 1, 0, 200, 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForTooBigStartingY() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, this.thumbnail.getHeight() + 1, 200, 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForTooBigWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, 0, this.thumbnail.getWidth() + 1, 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForTooBigHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, 0, 200, this.thumbnail.getHeight() + 1);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForExceedingOriginalImageSizeXDirection() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(1, 0, this.thumbnail.getWidth(), 300);
		});
	}
	
	@Test
	void cropThrowsIllegalArgumentExceptionForExceedingOriginalImageSizeYDirection() {
		assertThrows(IllegalArgumentException.class, () -> {
			this.thumbnail.crop(0, 1, 200, this.thumbnail.getHeight());
		});
	}
	
	@Test
	void cropWorksWithNoExceptionsThrown() {
		try {
			int newWidth = this.thumbnail.getWidth() - 100;
			int newHeight = this.thumbnail.getHeight() - 100;
			this.thumbnail.crop(0, 0, newWidth, newHeight);
			
			assertEquals(newWidth, this.thumbnail.getWidth());
			assertEquals(newHeight, this.thumbnail.getHeight());
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
