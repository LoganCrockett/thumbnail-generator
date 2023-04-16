import java.io.IOException;

import crockett.logan.thumbnail_generator.app.Thumbnail;

public class ThumbnailApplication {

	public static void main(String[] args) {
		String filePath = "resources\\images\\";
		
		Thumbnail t;
		try {
			t = new Thumbnail(filePath + "inputPicture.jpg");
			
			t.writeThumbnailToFileSystem(filePath + "thumbnail.png", "png");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
