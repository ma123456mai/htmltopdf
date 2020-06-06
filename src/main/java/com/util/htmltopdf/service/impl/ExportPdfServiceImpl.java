package com.util.htmltopdf.service.impl;

import com.itextpdf.text.pdf.BaseFont;
import com.util.htmltopdf.service.ExportPdfService;
import com.util.htmltopdf.util.FontUtils;
import com.util.htmltopdf.util.HtmlToPdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author Mr丶s
 * @date 2020/6/5 8:08 下午
 * @description
 */
@Service
public class ExportPdfServiceImpl implements ExportPdfService {
    private final static String imagePath = "classpath:/images/";

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public String render(String templateName, HttpServletRequest request, HttpServletResponse response,
                         ModelMap variables) {
        WebContext context = new WebContext(request, response, request.getServletContext(), request.getLocale(),
                variables);
        return templateEngine.process(templateName, context);
    }

    @Override
    public void html2pdf(String content, OutputStream stream) throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(content);
        ITextFontResolver fontResolver = renderer.getFontResolver();
        for (String fontPath : FontUtils.getFonts()) {
            fontResolver.addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }
        renderer.getSharedContext().setBaseURL(imagePath);
        renderer.layout();
        renderer.createPDF(stream);
    }

    @Override
    public void htmlToPdf(HttpServletResponse response) {
        String filePath = "/Users/mashuai/work/hello.html";
        String pdfPath = "/Users/mashuai/work/1111/fff.pdf";
        String text = "添加水印";
        String imgUrl = "/Users/mashuai/work/java/my/code/freemarker-html2pdf/htmltopdf/src/main/resources/images/IMG_0502.JPG";
        HtmlToPdf.createPDF(filePath, pdfPath, text, imgUrl);
    }
}