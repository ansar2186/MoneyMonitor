package com.ansar.moneymanaer_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/status", "/health"})
public class HomeController {

    @GetMapping
    public String healthCheck() {
        return "Application is running !";
    }

  /* @GetMapping
    public ResponseEntity<String> getEmp() {
        return ResponseEntity.ok()
                .header("Name","Ansar Ahmad")
                .header("Age","35")
                .header("City","Najibabad")
                .body("Employee Information");
    }*/

    /*@GetMapping
    public ResponseEntity<String> getEmp2() {
        return ResponseEntity.ok().body("Employee Information");
    }
*/

}
