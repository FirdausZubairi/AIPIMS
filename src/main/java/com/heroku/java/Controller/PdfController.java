package com.heroku.java.Controller;
import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import com.heroku.java.Services.PdfServices;
import com.lowagie.text.DocumentException;

import jakarta.servlet.http.HttpServletResponse;


@Controller
public class PdfController {

    private final PdfServices pdfServices;

    @Autowired
    public PdfController(PdfServices pdfServices) {
        this.pdfServices = pdfServices;
    }

    @PostMapping("/export-to-pdf")
    public void generatePdfFile(HttpServletResponse response) throws DocumentException, IOException, SQLException {
        pdfServices.generatePdfFile(response);
    }
}

