package com.util.htmltopdf.util;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * @author Mr丶s
 * @date 2020/6/6 12:15 下午
 * @description html导出pdf
 */
public class HtmlToPdf {


    //文字库
    private static final String FONTPATH = "/Users/mashuai/work/java/my/code/freemarker-html2pdf/htmltopdf/src/main/resources/font/SIMKAI.TTF";

    /**
     * 导出pdf（加水印）
     *
     * @param filePath html文件位置
     * @param pdfPath  pdf导出位置
     * @param text     水印文字
     * @param imgUrl   图片水印地址
     */
    public static void createPDF(String filePath, String pdfPath, String text, String imgUrl) {
        Assert.notNull(filePath, "FilePath can not be null");
        Assert.notNull(pdfPath, "PdfPath can not be null");
        Assert.notNull(text, "Text can not be null");
        Assert.notNull(imgUrl, "ImgUrl can not be null");
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            //自定义文字水印
            writer.setPageEvent(new TextwaterMark(text));
            //自定义图片水印
            writer.setPageEvent(new ImageWaterMark(imgUrl));
            document.addTitle("PDF标题");
            document.open();

            //读取html文件
            File file = new File(filePath);
            byte[] bytes = File2byte(file);

            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ByteArrayInputStream is = new ByteArrayInputStream("".getBytes());

            InputStream inputStream = null;
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, in, inputStream, StandardCharsets.UTF_8, new PdfFont());
            // XMLWorkerHelper.getInstance().parseXHtml(writer, document, in, is, new PdfFont());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    /**
     * 将文件转换成byte数组
     *
     * @param filePath
     * @return
     */
    public static byte[] File2byte(File filePath) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }


    /**
     * 中文字体支持
     */
    static class PdfFont extends XMLWorkerFontProvider {
        @Override
        public Font getFont(final String fontname, String encoding, float size, final int style) {
            try {
                return FontFactory.getFont(FONTPATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED, 10f, Font.NORMAL, BaseColor.BLACK);
            } catch (Exception ignored) {
            }
            return super.getFont(fontname, encoding, size, style);
        }

    }

    /**
     * 事件处理类, 文字水印
     */
    static class TextwaterMark extends PdfPageEventHelper {

        private String text;

        public TextwaterMark(String text) {
            this.text = text;
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                // 加入水印
                PdfContentByte waterMar = writer.getDirectContentUnder();
                // 开始设置水印
                waterMar.beginText();
                // 设置水印透明度
                PdfGState gs = new PdfGState();
                // 设置填充字体不透明度为0.2f
                gs.setFillOpacity(0.9f);
                //设置笔触字体不透明度为0.4f
                gs.setStrokeOpacity(0.9f);
                // 设置水印字体参数及大小
                BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
                        BaseFont.NOT_EMBEDDED);
                waterMar.setFontAndSize(baseFont, 60);
                // 设置透明度
                waterMar.setGState(gs);
                //设置水印颜色
                waterMar.setColorFill(BaseColor.RED);

                // 设置水印对齐方式 水印内容 X坐标 Y坐标 旋转角度
                waterMar.showTextAligned(Element.ALIGN_CENTER, text, 300, 500, 45);
                //结束设置
                waterMar.endText();
                waterMar.stroke();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 事件监听，图片水印
     */
    static class ImageWaterMark extends PdfPageEventHelper {

        private String imgUrl;

        public ImageWaterMark(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            try {
                // 加入水印
                PdfContentByte waterMar = writer.getDirectContentUnder();
                // 开始设置水印
                waterMar.beginText();
                // 设置水印透明度
                PdfGState gs = new PdfGState();
                // 设置笔触字体不透明度为0.4f
                gs.setStrokeOpacity(0.9f);
                //设置图片水印路径
                Image image = Image.getInstance(imgUrl);
                // 设置坐标 绝对位置 X Y
                image.setAbsolutePosition(300, 300);
                // 设置旋转弧度
                image.setRotation(0);// 旋转 弧度
                // 设置旋转角度
                image.setRotationDegrees(0);// 旋转 角度
                // 设置等比缩放
                image.scalePercent(5);// 依照比例缩放
                // image.scaleAbsolute(200,100);//自定义大小
                // 设置透明度
                waterMar.setGState(gs);
                // 添加水印图片
                waterMar.addImage(image);
                // 设置透明度
                waterMar.setGState(gs);
                //结束设置
                waterMar.endText();
                waterMar.stroke();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}