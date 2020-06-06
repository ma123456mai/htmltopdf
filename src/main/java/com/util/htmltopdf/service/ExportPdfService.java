package com.util.htmltopdf.service;

import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * @author Mr丶s
 * @date 2020/6/5 8:07 下午
 * @description
 */
public interface ExportPdfService {

    /**
     * 渲染模板
     *
     * @param param templateName
     * @param param request
     * @param param response
     * @param param variables
     * @return html
     * @Date 2020/2/16 15:00
     * @Author tjm
     */
    String render(String templateName, HttpServletRequest request, HttpServletResponse response, ModelMap variables);

    /**
     * 导出pdf
     *
     * @param content
     * @param stream
     */
    void html2pdf(String content, OutputStream stream) throws Exception;


    /**
     * 导出pdf
     */
    void htmlToPdf(HttpServletResponse response);
}
