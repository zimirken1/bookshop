package com.scand.bookshop.service.metadataextractor;

import org.springframework.web.multipart.MultipartFile;

public interface Extractor {
    Metadata extractMetaData(MultipartFile file);
    String getExtension();
}
