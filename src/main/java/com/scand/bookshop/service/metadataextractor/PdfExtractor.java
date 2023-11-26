package com.scand.bookshop.service.metadataextractor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PdfExtractor implements Extractor {
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    @Override
    public String getExtension() {
        return "pdf";
    }

    @Override
    public Metadata extractMetaData(MultipartFile file) {
        PDDocument document = loadDocument(file);
        PDDocumentInformation info = document.getDocumentInformation();
        Metadata metadata = new Metadata(
                info.getTitle(),
                info.getAuthor(),
                info.getSubject(),
                info.getProducer(),
                info.getCreator(),
                extractContent(file)
        );
        closeDocument(document);
        return metadata;
    }

    private byte[] extractContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "could_not_extract", null, request.getLocale()));
        }
    }

    private PDDocument loadDocument(MultipartFile file) {
        try {
            return PDDocument.load(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "error_loading_doc", null, request.getLocale()));
        }
    }

    private void closeDocument(PDDocument document) {
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(messageSource.getMessage(
                    "error_loading_doc", null, request.getLocale()));
        }
    }

}
