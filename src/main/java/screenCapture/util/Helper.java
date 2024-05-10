package screenCapture.util;

import org.apache.commons.io.FileUtils;
import screenCapture.pdfHelper.PDFUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Helper {

    public static int countFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path must be a directory");
        }

        String[] files = directory.list();
        return files != null ? files.length : 0;
    }
    public static void deleteTempFiles(String path) throws IOException {
        System.out.println("Total files present inside the directory are :"+ countFiles(path));
        FileUtils.cleanDirectory(new File(path));
        System.out.println("Total files present inside the directory after deletion are :"+ countFiles(path));
    }

    public static String timeStamp() {
        Date now = new Date();
        String Timestamp = now.toString().replace(":", "-").replace(" ", "_");
        return Timestamp;
    }

    public static String getScreenshot(String directory) throws AWTException, IOException, InterruptedException {
        String fileName = System.currentTimeMillis() + ".jpeg";
        String filePath = directory + "//" + fileName;
        Thread.sleep(1000L);
        Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Robot robot = new Robot();
        BufferedImage bufferedImage = robot.createScreenCapture(rectangle);
        ImageIO.write(bufferedImage, "jpeg", new File(filePath));
        return filePath;
    }

    public static void generatePDFReport(String screenshotsPath, String tempFilesPath, String results, String reportName) throws IOException {
        File screenShotFiles = new File(screenshotsPath);
        File[] listOfScreenshots = screenShotFiles.listFiles();
        if (listOfScreenshots.length == 0) {
            System.out.println("There is no screenshot file present inside screenshots folder");
        }

        assert listOfScreenshots != null;

        File[] var6 = listOfScreenshots;
        int var7 = listOfScreenshots.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            File f = var6[var8];
            PDFUtil.createPDFFromSingleFile(f.getPath(), f.getName().replace(".jpeg", ".pdf"), tempFilesPath);
        }

        PDFUtil.mergePdf(tempFilesPath, results, reportName);
    }
}
