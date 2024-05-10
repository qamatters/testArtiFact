package screenRecord.videoUtil;

import com.sun.jna.Platform;

import javax.media.MediaLocator;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class ScreenRecorder {
    private static boolean recording = false;
    private static String videoName = "output.mov";
    private static List<String> frames;
    private static long startedAt = 0;
    private static Thread currentThread;
    public static void start(String newVideoName) {
        if (!recording) {
            if (!VideoRecorderConfiguration.getTempDirectory().exists()) {
                VideoRecorderConfiguration.getTempDirectory().mkdirs();
            }
            calculateScreenshotSize();
            videoName = newVideoName;
            if (!videoName.endsWith(".mov")) {
                videoName += ".mov";
            }
            recording = true;
            frames = new ArrayList<String>();
            startedAt = new Date().getTime();
            currentThread = getRecordThread();
            currentThread.start();
        }
    }

    private static Thread getRecordThread() {
        return new Thread() {
            @Override
            public void run() {
                Robot rt;
                ScreenCapture capture;
                try {
                    rt = new Robot();
                    do {
                        capture = new ScreenCapture(rt.createScreenCapture(new Rectangle(
                                VideoRecorderConfiguration.getX(), VideoRecorderConfiguration.getY(),
                                VideoRecorderConfiguration.getWidth(), VideoRecorderConfiguration.getHeight())));
                        frames.add(VideoRecorderUtil.saveIntoDirectory(capture, new File(
                                VideoRecorderConfiguration.getTempDirectory().getAbsolutePath() + File.separatorChar
                                        + videoName.replace(".mov", ""))));
                        Thread.sleep(VideoRecorderConfiguration.getCaptureInterval());
                    } while (recording);
                } catch (Exception e) {

                    System.out.println(e.getStackTrace());
                    recording = false;
                }
            }
        };
    }

    private static void calculateScreenshotSize() {
        // if fullScreen was set, all the configuration will be changed back.
        if (VideoRecorderConfiguration.wantToUseFullScreen()) {
            Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
            VideoRecorderConfiguration.setWidth((int) size.getWidth());
            VideoRecorderConfiguration.setHeight((int) size.getHeight());
            VideoRecorderConfiguration.setCoordinates(0, 0);
        } else {
            // we have to check if x+width <= Toolkit.getDefaultToolkit().getScreenSize().getWidth() and the same for
            // the height
            if (VideoRecorderConfiguration.getX() + VideoRecorderConfiguration.getWidth() > Toolkit.getDefaultToolkit()
                    .getScreenSize().getWidth()) {
                VideoRecorderConfiguration.setWidth((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()
                        - VideoRecorderConfiguration.getX()));
            }
            if (VideoRecorderConfiguration.getY() + VideoRecorderConfiguration.getHeight() > Toolkit.getDefaultToolkit()
                    .getScreenSize().getHeight()) {
                VideoRecorderConfiguration.setHeight((int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()
                        - VideoRecorderConfiguration.getY()));
            }
        }
    }

    public static String stop() throws MalformedURLException {
        String videoPathString = null;
        if (recording) {
            recording = false;
            if (currentThread.isAlive()) {
                long now = new Date().getTime();
                while (frames.isEmpty()) {
                    try {
                        Thread.sleep(VideoRecorderConfiguration.getCaptureInterval());
                    } catch (InterruptedException e) {
                    }
                }
                currentThread.interrupt();
            }
            videoPathString = createVideo();
            if (!VideoRecorderConfiguration.wantToKeepFrames()) {
                deleteDirectory(new File(VideoRecorderConfiguration.getTempDirectory().getAbsolutePath()
                        + File.separatorChar + videoName.replace(".mov", "")));
            }
        }
        return videoPathString;
    }

    private static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

    private static String createVideo() throws MalformedURLException {
        Vector<String> vector = new Vector<String>(frames);
        String videoPathString = null;
        JpegImagesToMovie jpegImaveToMovie = new JpegImagesToMovie();
        if (!VideoRecorderConfiguration.getVideoDirectory().exists()) {
            VideoRecorderConfiguration.getVideoDirectory().mkdirs();
        }
        MediaLocator oml;
        String fileURL;
        if (Platform.isWindows()) {
            fileURL = "file://" + VideoRecorderConfiguration.getVideoDirectory().getPath() + File.separatorChar + videoName;
        } else {
            fileURL = VideoRecorderConfiguration.getVideoDirectory().getAbsolutePath() + File.separatorChar + videoName;
        }
        if ((oml = JpegImagesToMovie.createMediaLocator(fileURL)) == null) {
            System.exit(0);
        }
        if (jpegImaveToMovie.doIt(VideoRecorderConfiguration.getWidth(), VideoRecorderConfiguration
                .getHeight(), (1000 / VideoRecorderConfiguration.getCaptureInterval()), vector, oml)) {
            videoPathString = VideoRecorderConfiguration.getVideoDirectory().getAbsolutePath() + File.separatorChar
                    + videoName;
        }
        System.out.println("video path url is "+ videoPathString);
        return videoPathString;
    }
}
