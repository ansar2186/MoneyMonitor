package com.ansar.moneymanaer_api.controller;

import com.ansar.moneymanaer_api.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashBoardService dashBoardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashBoardData = dashBoardService.getDashBoardData();
        return ResponseEntity.ok(dashBoardData);

    }
}
