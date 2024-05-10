package screenCapture.pdfHelper;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class PDFUtil {
    public PDFUtil() {
    }

    public static void mergePdf(String filePath, String finalReportPath, String reportName) throws IOException {
        File folder = new File(filePath);
        File[] listOdPDFFiles = folder.listFiles();

        assert listOdPDFFiles != null;

        Arrays.sort(listOdPDFFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationFileName(finalReportPath + reportName);
        File[] var6 = listOdPDFFiles;
        int var7 = listOdPDFFiles.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            File file = var6[var8];
            pdfMergerUtility.addSource(file);
        }

        pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());
    }

    public static void createPDFFromSingleFile(String diffOutPUtFileNames, String pdfName, String tempFilesPath) throws IOException {
        PDDocument pdfDoc = new PDDocument();

        try {
            InputStream inputStream = Files.newInputStream(Paths.get(diffOutPUtFileNames));
            BufferedImage bImage = ImageIO.read(inputStream);
            float width = (float)bImage.getWidth();
            float height = (float)bImage.getHeight();
            PDPage page = new PDPage(new PDRectangle(width, height));
            pdfDoc.addPage(page);
            PDImageXObject pdImage = PDImageXObject.createFromFile(diffOutPUtFileNames, pdfDoc);
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page, AppendMode.APPEND, true, true);

            try {
                contentStream.drawImage(pdImage, 20.0F, 20.0F);
            } catch (Throwable var18) {
                try {
                    contentStream.close();
                } catch (Throwable var17) {
                    var18.addSuppressed(var17);
                }

                throw var18;
            }

            contentStream.close();
            inputStream.close();
            pdfDoc.save(tempFilesPath + pdfName);
        } finally {
            pdfDoc.close();
        }

    }
}
