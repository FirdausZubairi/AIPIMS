package com.heroku.java.Services;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
// import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.heroku.java.Model.CaseBased;
// import jakarta.servlet.http.HttpSession;
import com.heroku.java.Model.Request;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PdfServices {

    private final DataSource dataSource;

    @Autowired
    public PdfServices(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void generatePdfFile(HttpServletResponse response) throws DocumentException, IOException, SQLException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=student" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        List<CaseBased> listofPredictions = fetchPredictions();

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTiltle = FontFactory.getFont(FontFactory.COURIER_BOLD);
        fontTiltle.setSize(20);
        Paragraph paragraph1 = new Paragraph("AIPIMS Record", fontTiltle);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph1);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 2, 2, 2, 2});
        table.setSpacingBefore(5);

        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.BLACK);

        addTableHeader(cell, table, font);

        for (CaseBased record : listofPredictions) {
            table.addCell(String.valueOf(record.getCbrID()));
            table.addCell(String.valueOf(record.getItemid()));
            table.addCell(record.getItemName());
            table.addCell(String.valueOf(record.getPredictedQuan()));
            table.addCell(String.valueOf(record.getYears()));
        }

        document.add(table);
        document.close();
    }

    private List<CaseBased> fetchPredictions() throws SQLException {
        List<CaseBased> listofPredictions = new ArrayList<>();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "SELECT v.*,i.itemid, i.itemquantity, i.itemname, p.projectname FROM cbr v " +
                "JOIN project_item pt ON (v.piid = pt.piid) " +
                "JOIN item i ON (pt.itemid = i.itemid) " +
                "JOIN project p ON (pt.projectid = p.projectid) " +
                "ORDER BY pt.piid");

        while (resultSet.next()) {
            Integer cbrID = resultSet.getInt("cbrid");
            String projectName = resultSet.getString("projectname");
            String itemName = resultSet.getString("itemname");
            Integer predictQuan = resultSet.getInt("predictedquantity");
            String years = resultSet.getString("years");
            Integer reqID = resultSet.getInt("reqid");
            Integer piid = resultSet.getInt("piid");
            Integer itemid = resultSet.getInt("itemid");
            Integer iquantity = resultSet.getInt("itemquantity");

            CaseBased cbr = new CaseBased(cbrID, predictQuan, years, reqID, piid, itemid, itemName, projectName, iquantity);
            listofPredictions.add(cbr);
        }

        resultSet.close();
        statement.close();
        connection.close();

        return listofPredictions;
    }

    private void addTableHeader(PdfPCell cell, PdfPTable table, Font font) {
        cell.setPhrase(new Phrase("Case ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Item ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Item Name", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Predicted Quantity", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Year", font));
        table.addCell(cell);
    }
}

