package com.util.htmltopdf.controller;

import com.util.htmltopdf.service.ExportPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mr丶s
 * @date 2020/6/6 12:19 下午
 * @description
 */
@Controller
@RequestMapping("pdf")
public class ExportPdfController {

    @Autowired
    private ExportPdfService exportPdfService;


    @GetMapping("htmlToPdf")
    public void HtmlToPdf(HttpServletResponse response) {
        exportPdfService.htmlToPdf(response);
    }



    @GetMapping("/cert")
    public void exportCert(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ModelMap variables = new ModelMap();
        variables.put("code", "dddd");
//		response.addHeader("Content-disposition", "attachment;filename=" + management.getCertificateNumber() + ".HtmlToPdf");
        String content = exportPdfService.render("cert2pdf", request, response, variables);
        response.setContentType("application/HtmlToPdf");
        exportPdfService.html2pdf(content, response.getOutputStream());

    }
}
