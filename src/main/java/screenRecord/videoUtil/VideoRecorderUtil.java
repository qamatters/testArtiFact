package screenRecord.videoUtil;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class VideoRecorderUtil {
    public static String saveIntoDirectory(ScreenCapture capture, File directory) throws IOException {
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String savedPath = directory.getAbsolutePath() + File.separatorChar + System.currentTimeMillis() + ".jpeg";
        ImageIO.write(capture.getSource(), "jpeg", new File(savedPath));
        return savedPath;
    }

}
