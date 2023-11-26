package com.scand.bookshop.converter;

import com.scand.bookshop.dto.PriceDTO;
import org.springframework.core.convert.converter.Converter;

public class StringToPriceDTOConverter implements Converter<String, PriceDTO> {

  @Override
  public PriceDTO convert(String source) {
    PriceDTO priceDTO = new PriceDTO();
    priceDTO.setPrice(Double.valueOf(source));
    return priceDTO;
  }
}
