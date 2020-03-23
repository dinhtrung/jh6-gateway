package com.ft.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.service.dto.XlsxFileDTO;

/**
 * Service Implementation for managing GmpXlsFile.
 */
@Service
public class XlsxFileService {

	private final Logger log = LoggerFactory.getLogger(XlsxFileService.class);

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	ObjectMapper objectMapper;

	/**
	 * Import Any format of Excel into DB, to any entity Meta contain following
	 * info: - fields: The list of field, can be line by line, tabbed, or comma
	 * separated - pkey: The primary key to map, if any; - entity: The collection to
	 * insert into
	 * 
	 * @param xlsFileDTO
	 * @return
	 * @throws Exception
	 */
	@Async
	public void importAnyFile(XlsxFileDTO xlsFileDTO) {

		long result = 0L;
		String fields = xlsFileDTO.getMeta().get("fields") + "";
		String pkey = xlsFileDTO.getMeta().get("pkey") + "";
		String entity = xlsFileDTO.getMeta().get("entity") + "";
		log.debug("fields: {}, pkey: {}, entity: {}", fields, pkey, entity);
		String[] headerColumns = StringUtils.tokenizeToStringArray(fields, "\r\n\f\t,", true, true);
		try (Workbook workBook = WorkbookFactory.create(new ByteArrayInputStream(xlsFileDTO.getFile()))){
			for (int s = 0; s < workBook.getNumberOfSheets(); s++) {
				Sheet sheet = workBook.getSheetAt(s);
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					result = importRowIntoCollection(result, pkey, entity, headerColumns, sheet, i);
				}
			}
			log.info("Successfully import {} rows into {}", result, entity);
		} catch (Exception e) {
			log.error("Cannot import xlsx file", e);
		}
	}

	/**
	 * Import one abitrary data into any collection
	 * 
	 * @param result
	 * @param pkey
	 * @param entity
	 * @param headerColumns
	 * @param sheet
	 * @param i
	 * @return
	 */
	private long importRowIntoCollection(long result, String pkey, String entity, String[] headerColumns, Sheet sheet,
			int i) {
		try {
			Row row = sheet.getRow(i);
			Map<String, Object> entry = new HashMap<>();
			for (int j = 0; j < headerColumns.length; j++) {
				Cell cell = row.getCell(j);
				try {
					entry.put(headerColumns[j], cell.getStringCellValue());
				} catch (Exception e) {
					try {
						entry.put(headerColumns[j], cell.getNumericCellValue());
					} catch (Exception e1) {
						log.warn("Cannot parse cell: {}, {}, {}: {}", i, j, cell, e);
					}
				}
			}
			// Map the pkey if it exists
			if (!pkey.isEmpty()) {
				entry.put("_id", entry.get(pkey));
				entry.remove(pkey);
			}
			mongoTemplate.save(entry, entity);
			result++;
		} catch (Exception e) {
			log.debug("Cannot import row {}: {}", i, e);
		}
		return result;
	}

	/**
	 * 
	 * @param xlsFileDTO
	 * @throws Exception
	 */
	@Async
	public void importXlsxFile(XlsxFileDTO xlsFileDTO) throws Exception {
		long result = 0L;
		// get fields
		List<String> fields = (List<String>) xlsFileDTO.getMeta().get("fields");
		// get commons
		Map<String, String> commons = objectMapper.convertValue(xlsFileDTO.getMeta().get("commons"), Map.class);
		// get entity
		String entity = xlsFileDTO.getMeta().get("entity").toString();
		log.debug("fields: {}, commons: {}, entity: {}", fields, commons, entity);
		// remove data in mongoDB
		InputStream is = new ByteArrayInputStream(xlsFileDTO.getFile());
		Workbook workBook = WorkbookFactory.create(is);
		for (int s = 0; s < workBook.getNumberOfSheets(); s++) {
			Sheet sheet = workBook.getSheetAt(s);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				result = importRowIntoDB(result, commons, entity, fields, sheet, i);
			}
		}
		log.info("Successfully import {} rows into {}", result, entity);
	}

	/**
	 * 
	 * @param result
	 * @param commons
	 * @param entity
	 * @param headerColumns
	 * @param sheet
	 * @param i
	 * @return
	 */
	private long importRowIntoDB(long result, Map<String, String> commons, String entity, List<String> fields,
			Sheet sheet, int i) {
		try {
			Row row = sheet.getRow(i);
			Map<Object, Object> meta = new HashMap<>();
			Map<Object, Object> entry = new HashMap<>();
			for (int j = 0; j < fields.size(); j++) {
				meta.put("_id", j + 1);
				Cell cell = row.getCell(j);
				try {
					meta.put(fields.get(j), cell.getStringCellValue());
					if (j == 0) {
						entry.put("_id",
								(commons.get("type") + "-" + commons.get("name") + "-" + cell.getStringCellValue())
										.toLowerCase());
					}
				} catch (Exception e) {
					try {
						meta.put(fields.get(j), cell.getNumericCellValue());
					} catch (Exception e1) {
						log.warn("Cannot parse cell: {}, {}, {}: {}", i, j, cell, e);
					}
				}
			}
			// Map the pkey if it exists
			// Map all commons field
			entry.put("meta", meta);
			entry.put("type", commons.get("type"));
			entry.put("name", commons.get("name"));
			mongoTemplate.save(entry, entity);
			result++;
		} catch (Exception e) {
			log.debug("Cannot import row {}: {}", i, e);
		}
		return result;
	}
}
