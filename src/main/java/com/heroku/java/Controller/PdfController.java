package com.heroku.java.Controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Services.PdfServices;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class PdfController {

    private final PdfServices pdfServices;

    public PdfController(PdfServices pdfServices) {
        this.pdfServices = pdfServices;
    }

    @PostMapping("/export-to-pdf")
    public void generatePdfFile(HttpServletResponse response, @RequestParam(name = "searchValue", required = false) String searchValue) throws DocumentException, IOException, SQLException {
        System.out.println("Received searchValue: " + searchValue); // Debug print statement
        pdfServices.generatePdfFile(response, searchValue);
    }
}
