package com.scand.bookshop.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCoverService {
    private final MessageSource messageSource;
    private final HttpServletRequest request;

    public byte[] generateCover(byte[] data, int pageNum) {
        log.info("Generating cover for page number: " + pageNum);
        PDDocument document = getDocumentFromBytes(data);
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage image = getCoverBufferedImage(pdfRenderer, pageNum);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        writeImageToStream(image, baos);
        try {
            document.close();
        } catch (IOException e) {
            log.error("Error while closing the document", e);
            throw new RuntimeException(messageSource.getMessage(
                    "error_closing_doc", null, request.getLocale()));
        }
        return baos.toByteArray();
    }

    private PDDocument getDocumentFromBytes(byte[] data) {
        try {
            return PDDocument.load(data);
        } catch (IOException e) {
            log.error("Error while loading document from bytes", e);
            throw new RuntimeException(messageSource.getMessage(
                    "error_loading_doc", null, request.getLocale()));
        }
    }

    private BufferedImage getCoverBufferedImage(PDFRenderer pdfRenderer, int pageNum) {
        try {
            return pdfRenderer.renderImage(pageNum);
        } catch (IOException e) {
            log.error("Error while rendering image for page: " + pageNum, e);
            throw new RuntimeException(messageSource.getMessage(
                    "error_cover_image", null, request.getLocale()));
        }
    }

    private void writeImageToStream(BufferedImage image, ByteArrayOutputStream baos) {
        try {
            ImageIO.write(image, "png", baos);
        } catch (IOException e) {
            log.error("Error while writing image to stream", e);
            throw new RuntimeException(messageSource.getMessage(
                    "error_writing_img", null, request.getLocale()));
        }
    }

}
