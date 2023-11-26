package com.scand.bookshop.service.metadataextractor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class EpubExtractor implements Extractor {

    @Override
    public Metadata extractMetaData(MultipartFile file) {
        return new Metadata();
    }

    @Override
    public String getExtension() {
        return "epub";
    }
}