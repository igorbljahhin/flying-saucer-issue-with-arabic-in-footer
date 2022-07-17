import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RTLPDFMain {
    private static void loadFonts(ITextRenderer renderer) {
        final String[] fonts = new String[]{
                "NotoSansArabic-Regular.ttf",
                "Roboto-Regular.ttf",
        };

        for (String font : fonts) {
            try {
                renderer.getFontResolver().addFont(font, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (DocumentException | IOException e) {
                throw new RuntimeException("Failed to load font " + font, e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File outputFile = new File("output.pdf");

        final ITextRenderer renderer = new ITextRenderer();
        loadFonts(renderer);

        RTLTextReplacedElementFactory replacedElement = new RTLTextReplacedElementFactory(renderer.getOutputDevice(), "arabic");
        renderer.getSharedContext().setReplacedElementFactory(replacedElement);

        final String html = IOUtils.resourceToString("/input.html", StandardCharsets.UTF_8);
        renderer.setDocumentFromString(html);
        renderer.layout();

        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            renderer.createPDF(baos);

            byte[] bytes = baos.toByteArray();
            FileUtils.writeByteArrayToFile(outputFile, bytes);
        } catch (IOException | DocumentException e) {
            throw new RuntimeException("Failed to render PDF document", e);
        }

        Desktop.getDesktop().open(outputFile);
    }
}
