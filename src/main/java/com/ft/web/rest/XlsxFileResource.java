package com.ft.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ft.security.AuthoritiesConstants;
import com.ft.service.XlsxFileService;
import com.ft.service.dto.XlsxFileDTO;

/**
 * REST controller for managing xlsxFile.
 */
@RestController
@RequestMapping(value = "/api", method = {RequestMethod.GET, RequestMethod.POST})
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
public class XlsxFileResource {
	
	private final Logger log = LoggerFactory.getLogger(XlsxFileResource.class);

    private final XlsxFileService xlsxFileService;

    public XlsxFileResource(XlsxFileService xlsxFileService) {
        this.xlsxFileService = xlsxFileService;
    }

    /**
     * POST /gmp-xls-files : Create a new xlsxFile.
     *
     * @param xlsFileDTO the xlsxFileDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         xlsxFileDTO, or with status 400 (Bad Request) if the xlsxFile has
     *         already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/import-xlsx")
    public ResponseEntity<Long> importXlsxFile(@RequestBody XlsxFileDTO xlsFileDTO) throws Exception {
        xlsxFileService.importAnyFile(xlsFileDTO);
        return ResponseEntity.accepted().build();
    }

    /**
     * Generate an xlsx file with following payload: { 
     * "name": "File-name.xlsx",
     * "meta": {
     * "thead": ["col1", "col2", "col3"], 
     * "tbody": [ 
     * ["col1_row1", 11, "col3_row1"],
     * ["col1_row2", 12, "col3_row2"] 
     * ] 
     * }
     * }
     * 
     * 
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/generate-xlsx")
    public ResponseEntity<byte[]> getXlsx(@RequestBody XlsxFileDTO req) throws Exception {
        byte[] result = null;
		try (Workbook workbook = new XSSFWorkbook()) {
            // Create a blank sheet with report name
            Sheet sheet = workbook.createSheet(req.getMeta().get("name") + "");

            // Generate the headings
            int rownum = 0;
            Row row0 = sheet.createRow(rownum);
            Cell cell0 = row0.createCell(0, CellType.STRING);
            cell0.setCellValue(req.getMeta().get("name") + "");

            // Generate the second row
            rownum += 2;
            Row headingRow = sheet.createRow(rownum);
            @SuppressWarnings("unchecked")
            List<String> thead = (List<String>) req.getMeta().get("thead");

            for (int col = 0; col < thead.size(); col++) {
                Cell c = headingRow.createCell(col);
                c.setCellValue(thead.get(col));
            }

            // Print the data body
            rownum++;
            @SuppressWarnings("unchecked")
            List<List<Object>> tbody = (List<List<Object>>) req.getMeta().get("tbody");
            for (List<Object> rowData : tbody) {
                // this creates a new row in the sheet
                Row row = sheet.createRow(rownum++);
                for (int cellnum = 0; cellnum < rowData.size(); cellnum++) {
                    // this line creates a cell in the next column of that row
                    Cell cell = row.createCell(cellnum);
                    Object obj = rowData.get(cellnum);
                    if (obj instanceof String)
                        cell.setCellValue((String) obj);
                    else if (obj instanceof Integer)
                        cell.setCellValue((Integer) obj);
                    else {
                        cell.setCellValue((Double) obj);
                    }
                }
            }
            // this Writes the workbook gfgcontribute
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            	workbook.write(out);
            	result  = out.toByteArray();
            } catch (IOException f) {
            	
            }
        } catch (IOException e) {
        	log.error("Cannot generate xlsx", e);
        }
        return ResponseEntity.ok().body(result);
    }
    
    @PostMapping("/generate-csv")
    public ResponseEntity<String> getCsv(@RequestBody XlsxFileDTO req) throws Exception {
        FileWriter out = new FileWriter(req.getMeta().get("name") + ".csv");
        CSVPrinter printer = CSVFormat.DEFAULT.print(out);
        // Generate the headings
        int rownum = 0;
        
        @SuppressWarnings("unchecked")
        List<String> thead = (List<String>) req.getMeta().get("thead");
        printer.printRecord(thead);
        
        rownum++;
        @SuppressWarnings("unchecked")
        List<List<Object>> tbody = (List<List<Object>>) req.getMeta().get("tbody");
        
        for (List<Object> rowData : tbody) {
            printer.printRecord(rowData);
        }
        out.close();
        log.debug("wrote {} lines", rownum);
        
        return ResponseEntity.ok().body(out.toString());
    }
}
