package screenCapture.wordHelper;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateWordPicturesInSinglePages {


    public static void addImagesToWord(List<String> screenShotPath, String reportName, String reportLocation) {
        try {
            XWPFDocument doc = new XWPFDocument();

            try {
                XWPFParagraph[] page = new XWPFParagraph[screenShotPath.size()];

                for(int pg = 0; pg < screenShotPath.size(); ++pg) {
                    page[pg] = doc.createParagraph();
                    page[pg].setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun run = page[pg].createRun();
                    File image = new File((String)screenShotPath.get(pg));
                    FileInputStream imageData = new FileInputStream(image);

                    try {
                        int imageType = 5;
                        String imageFileName = image.getName();
                        int width = 600;
                        int height = 400;
                        run.addPicture(imageData, imageType, imageFileName, Units.toEMU((double)width), Units.toEMU((double)height));
                        XWPFParagraph paragraph2 = doc.createParagraph();
                        paragraph2.setAlignment(ParagraphAlignment.CENTER);
                    } catch (Throwable var18) {
                        try {
                            imageData.close();
                        } catch (Throwable var16) {
                            var18.addSuppressed(var16);
                        }

                        throw var18;
                    }

                    imageData.close();
                }

                FileOutputStream out = new FileOutputStream(reportLocation + "//" + reportName);

                try {
                    doc.write(out);
                } catch (Throwable var17) {
                    try {
                        out.close();
                    } catch (Throwable var15) {
                        var17.addSuppressed(var15);
                    }

                    throw var17;
                }

                out.close();
            } catch (Throwable var19) {
                try {
                    doc.close();
                } catch (Throwable var14) {
                    var19.addSuppressed(var14);
                }

                throw var19;
            }

            doc.close();
        } catch (InvalidFormatException | IOException var20) {
            System.err.println(var20.getMessage());
        }

    }

    public static List<String> getScreenshotPaths(String screenShotPath) {
        File screenShotFiles = new File(screenShotPath);
        File[] listOfScreenshots = screenShotFiles.listFiles();
        List<String> screenshotPaths = new ArrayList();

        assert listOfScreenshots != null;

        if (listOfScreenshots.length == 0) {
            System.out.println("There is no screenshot file present inside screenshots folder");
        } else {
            File[] var4 = listOfScreenshots;
            int var5 = listOfScreenshots.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File f = var4[var6];
                screenshotPaths.add(f.getPath());
            }
        }

        return screenshotPaths;
    }
}
