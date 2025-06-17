package com.onlineexam.analytics.service;

import com.onlineexam.analytics.model.Report;
import com.onlineexam.analytics.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class test1 {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    

    @Test
    void testGetAllReports_EmptyList() {
        when(reportRepository.findAll()).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reportService.getAllReports();
        });

        assertEquals("No reports found", exception.getMessage());
    }
}
