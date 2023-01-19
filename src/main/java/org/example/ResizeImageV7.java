package org.example;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;



import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.element.Image;

public class ResizeImageV7 {



    /*
     * Manipulates a PDF file src with the file dest as result
     *
     * @param src  the original PDF
     * @param dest the resulting PDF
     * @param resizeFactor factor to multiplicate to resize image
     * @throws IOException
     */
    public void manipulatePdf(String src, String dest, Float resizeFactor) throws IOException {

        //Get source pdf
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(dest));

        // Iterate over all pages to get all images.
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfPage page = pdfDoc.getPage(i);
            PdfDictionary pageDict = page.getPdfObject();
            PdfDictionary resources = pageDict.getAsDictionary(PdfName.Resources);
            // Get images
            PdfDictionary xObjects = resources.getAsDictionary(PdfName.XObject);
            for (Iterator<PdfName> iter = xObjects.keySet().iterator(); iter.hasNext(); ) {
                // Get image
                PdfName imgRef = iter.next();
                PdfStream stream = xObjects.getAsStream(imgRef);
                PdfImageXObject image = new PdfImageXObject(stream);
                BufferedImage bi = image.getBufferedImage();
                if (bi == null)
                    continue;

                // Create new image
                int width = (int) (bi.getWidth() * resizeFactor);
                int height = (int) (bi.getHeight() * resizeFactor);
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(resizeFactor, resizeFactor);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();

                // Write new image
                ImageIO.write(img, "JPG", imgBytes);
                Image imgNew = new Image(ImageDataFactory.create(imgBytes.toByteArray()));

                // Replace the original image with the resized image
                xObjects.put(imgRef, imgNew.getXObject().getPdfObject());
            }
        }

        pdfDoc.close();
    }

    /*
     * Main method.
     *
     * @param src  the original PDF
     * @param dest the resulting PDF
     * @param resizeFactor factor to multiplicate to resize image
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

            String sourcePDF = "src/main/resources/pdfs/e3650495-d22f-409d-8e9a-d76b7dfed63e.pdf";
            String destPDF = "src/main/resources/pdfs/777.pdf";

            new ResizeImageV7().manipulatePdf(sourcePDF, destPDF, 2.5f);

        }
    }
