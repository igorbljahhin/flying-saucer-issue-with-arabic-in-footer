import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextOutputDevice;
import org.xhtmlrenderer.pdf.ITextReplacedElementFactory;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

public class RTLTextReplacedElementFactory implements ReplacedElementFactory {
    private final String cssClassName;
    private final ITextReplacedElementFactory defaultFactory;

    public RTLTextReplacedElementFactory(ITextOutputDevice outputDevice, String cssClassName) {
        this.defaultFactory = new ITextReplacedElementFactory(outputDevice);
        this.cssClassName = cssClassName;
    }

    @Override
    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        ReplacedElement result;
        Element element = box.getElement();

        if (element == null) {
            result = null;
        } else if (element.getAttribute("class").contains(cssClassName)) {
            String text = element.getTextContent().replaceAll("(?m)\\s + ", "");
            result = new RTLText(c, box, uac, cssWidth, cssHeight, text);
        } else {
            result = defaultFactory.createReplacedElement(c, box, uac, cssWidth, cssHeight);
        }

        return result;
    }

    @Override
    public void remove(Element e) {
    }

    @Override
    public void reset() {
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
    }
}
