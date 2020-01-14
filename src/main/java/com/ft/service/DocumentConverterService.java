package com.ft.service;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

/**
 * Service Implementation for managing Snippet.
 */
@Service
@ConditionalOnProperty(prefix = "pandoc", name = { "path" })
public class DocumentConverterService {

    private final Logger log = LoggerFactory.getLogger(DocumentConverterService.class);
    
//    /**
//     * Process a snippet.
//     *
//     * @param snippetDTO the entity to save
//     * @return the persisted entity
//     * @throws IOException 
//     */
//    public SnippetDTO docx2markdown(SnippetDTO snippetDTO) throws IOException {
//        log.debug("Request to convert Snippet : {}", snippetDTO);
//        if (snippetDTO.getDocument() != null) {
//                byte[] markdown = convert(snippetDTO.getDocument(), "docx", "gfm");
//                snippetDTO.setMarkdownContent(new String(markdown, "UTF-8"));
//                
//                byte[] html = convert(markdown, "gfm", "html");
//                snippetDTO.setHtmlContent(new String(html, "UTF-8"));
//        }
//        return snippetDTO;
//    }
    
    @Value("${pandoc.path}")
    private String pandoc;
    
    public byte[] convert(byte[] source, String from, String to, String options) throws IOException {
    	log.debug("Convert data from {} to {}", from, to);
        File input = File.createTempFile(UUID.randomUUID().toString(), "." + from);
        FileCopyUtils.copy(source, input);
        
        File dest = File.createTempFile(UUID.randomUUID().toString(), "." + to);

        // COnvert Docx to Markdown
        log.debug("{} {} --from {} --to {} --output {} {}", pandoc, input.getAbsolutePath(), from, to, dest.getAbsolutePath(), options);
        ProcessBuilder processBuilder = new ProcessBuilder(pandoc,
                input.getAbsolutePath(),
                "--from", from,
                "--to", to,
                "--output", dest.getAbsolutePath()
                );
        if (options != null) processBuilder.command().add(options);
        processBuilder.redirectOutput(Redirect.INHERIT);
        processBuilder.redirectError(Redirect.INHERIT);
        final Process p = processBuilder.start();
        while (p.isAlive()) {}
        byte[] result = FileCopyUtils.copyToByteArray(dest);
        input.deleteOnExit();
        return result;
    }
    
}
