package com.prateek.web.springrestdemo.services;

import java.io.File;
import java.util.List;

import com.prateek.web.springrestdemo.domain.entities.BeerCSVRecord;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csv);
}
