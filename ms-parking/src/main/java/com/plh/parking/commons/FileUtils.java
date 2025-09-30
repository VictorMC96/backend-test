package com.plh.parking.commons;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.plh.parking.model.dto.TxtGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

public class FileUtils {

    public static byte[] write(TxtGenerator txtGenerator) throws IOException {
        CsvMapper csvMapper = CsvMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .build();

        Character separator = Optional.ofNullable(txtGenerator.getSeparator())
                .orElse('\t');

        CsvSchema csvSchema = csvMapper.schemaFor(txtGenerator.getData().get(0).getClass())
                .withoutQuoteChar()
                .withColumnSeparator(separator)
                .withHeader();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()){
        	csvMapper.writer(csvSchema).writeValue(baos, txtGenerator.getData());
        	return baos.toByteArray();
        }

    }
}
