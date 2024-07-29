package com.heroku.java.Services;
import java.awt.Color;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.stereotype.Service;
import com.heroku.java.Model.CaseBased;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfServices {

    private final DataSource dataSource;

    // @Autowired
    public PdfServices(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generatePdfFile(HttpServletResponse response, String year) throws DocumentException, IOException, SQLException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=AIPIMS_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        List<CaseBased> listofPredictions = fetchPredictions(year);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Title and Date
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Paragraph title = new Paragraph("AI Power Inventory Management System", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        Font fontDate = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph date = new Paragraph("Date Created: " + currentDateTime, fontDate);
        date.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(date);

        document.add(new Paragraph("\n")); // Add space between sections

        // Company Information
        Font fontCompany = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph companyInfoTitle = new Paragraph("AI POWER SDN. BHD.", fontCompany);
        document.add(companyInfoTitle);

        Font fontCompanyDetails = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Paragraph companyDetails = new Paragraph(
            
            "Electrical Company\n" +
            "Lot 11181, Jalan Perepat, Batu 11,\n" +
            "Jalam Telok Mengkuang, 42500,\n" +
            "Telok Panglima Garang, Selangor,\n" +
            "aipower8988@gmail.com\n" +
            "(+60) 012-237 3146",
            fontCompanyDetails
        );
        document.add(companyDetails);

        document.add(new Paragraph("\n")); // Add space between sections

        // Inventory Table
        Font fontInventory = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph inventoryTitle = new Paragraph("Inventory", fontInventory);
        document.add(inventoryTitle);

        PdfPTable table = new PdfPTable(5); // Adjust the number of columns as needed
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);

        // Table Header
        Font fontTableHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        addTableHeader(table, fontTableHeader);

         // Table Rows
         for (CaseBased record : listofPredictions) {
            table.addCell(record.getItemName());
            table.addCell(record.getProjectType()); // Placeholder, replace with actual category if available
            table.addCell(String.valueOf(record.getIquantity()));
            table.addCell(String.valueOf(record.getPredictedQuan())); // Placeholder, replace with actual order quantity if available
            table.addCell(record.getYears()); // Placeholder, replace with actual unit price if available
        }

        document.add(table);
        document.close();
    
    }

    private List<CaseBased> fetchPredictions(String year) throws SQLException {
        List<CaseBased> listofPredictions = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            // Modify query to handle no specific year
            String query = "SELECT v.*, i.itemid, i.itemquantity, i.itemname, p.projectname, p.projecttype FROM cbr v " +
                            "JOIN project_item pt ON (v.piid = pt.piid) " +
                            "JOIN item i ON (pt.itemid = i.itemid) " + 
                            "JOIN project p ON (pt.projectid = p.projectid) " +
                            "WHERE v.years LIKE ? ORDER BY v.cbrid";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            // Use '%' for all years if no specific year is provided
            if (year == null || year.isEmpty()) {
                preparedStatement.setString(1, "%");
            } else {
                preparedStatement.setString(1, year);
            }
            
            ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            Integer cbrID = resultSet.getInt("cbrid");
            String projectName = resultSet.getString("projectname");
            String projectType = resultSet.getString("projecttype");
            String itemName = resultSet.getString("itemname");
            Integer predictQuan = resultSet.getInt("predictedquantity");
            String years = resultSet.getString("years");
            Integer reqID = resultSet.getInt("reqid");
            Integer piid = resultSet.getInt("piid");
            Integer itemid = resultSet.getInt("itemid");
            Integer iquantity = resultSet.getInt("itemquantity");

            CaseBased cbr = new CaseBased(cbrID, predictQuan, years, reqID, piid, itemid, itemName, projectType, projectName, iquantity);
            listofPredictions.add(cbr);
        }

        resultSet.close();
        connection.close();
    } catch (SQLException e) {
        throw e;
    }
        return listofPredictions;
    }
    private void addTableHeader(PdfPTable table, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(0, 112, 184));
        cell.setPadding(5);
        cell.setPhrase(new Phrase("Item Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Project Type", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Quantity on Hand", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order Quantity", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Year", font));
        table.addCell(cell);
    }
}

