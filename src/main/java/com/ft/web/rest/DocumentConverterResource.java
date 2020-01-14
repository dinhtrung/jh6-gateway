package com.ft.web.rest;

import java.net.URLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ft.service.DocumentConverterService;


@RestController
@RequestMapping("/api/converter")
@ConditionalOnProperty(prefix = "pandoc", name = { "path" })
public class DocumentConverterResource {

	@Autowired
	DocumentConverterService documentConverterService;
	
	/**
	 * Convert an uploaded file with specified format into target format
	 * @param from
	 * @param to
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/pandoc")
	public ResponseEntity<byte[]> pandocConverter(
			@RequestParam String from, 
			@RequestParam String to, 
			@RequestParam("file") MultipartFile file,
			@RequestParam(required = false) String options
		) throws Exception {
		String mimeType = URLConnection.guessContentTypeFromName("output." + to);
		if (mimeType == null) {
			mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.parseMediaType(mimeType).toString())
                .body(documentConverterService.convert(file.getBytes(), from, to, options));
	}
}
