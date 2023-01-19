package org.example;


import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Jpg {
//    public List<RenderedImage> getImagesFromPDF(PDDocument document) throws IOException {
//        List<RenderedImage> images = new ArrayList<>();
//        for (PDPage page : document.getPages()) {
//            images.addAll(getImagesFromResources(page.getResources()));
//        }
//        document.close();
//        return images;
//    }
//
//    private List<RenderedImage> getImagesFromResources(PDResources resources) throws IOException {
//        List<RenderedImage> images = new ArrayList<>();
//
//        for (COSName xObjectName : resources.getXObjectNames()) {
//            PDXObject xObject = resources.getXObject(xObjectName);
//
//            if (xObject instanceof PDFormXObject) {
//                images.addAll(getImagesFromResources(((PDFormXObject) xObject).getResources()));
//            } else if (xObject instanceof PDImageXObject) {
//                images.add(((PDImageXObject) xObject).getImage());
//            }
//        }
//
//        return images;
//    }

    public static void main(String args[]) throws Exception {
//        File file = new File("src/main/resources/pdfs/31d88cc9-07e0-4d86-a9e9-26903e31fc07.pdf");
//        PDDocument document = new PDDocument();
//        document = PDDocument.load(file);
//        List<RenderedImage> images = new Jpg().getImagesFromPDF(PDDocument.load(file));
        testPDFBoxExtractImages();


    }

    public static void testPDFBoxExtractImages() throws Exception {
        PDDocument document = PDDocument.load(new File("src/main/resources/pdfs/e3650495-d22f-409d-8e9a-d76b7dfed63e.pdf"));
        PDPageTree list = document.getPages();
        for (PDPage page : list) {
            PDResources pdResources = page.getResources();
            for (COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
                    File file = new File("src/main/resources/pdfs" + System.nanoTime() + ".png");
                    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject)o).getImage(), "png", file);
                }
            }
        }
    }
}

