package com.coworking.management.controller;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.Espace;
import com.coworking.management.service.EspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/espaces")
public class EspaceController {

    @Autowired
    private EspaceService espaceService;

    @GetMapping("/")
    public ResponseEntity<ReqRes> getAllEspaces() {
        return ResponseEntity.ok(espaceService.getAllEspaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReqRes> getEspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(espaceService.getEspaceById(id));
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReqRes> createEspace(
        @RequestPart("espace") String espaceJson,
        @RequestPart("mainImage") MultipartFile mainImage,
        @RequestPart(value = "galleryImages", required = false) MultipartFile[] galleryImages
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Espace espace = objectMapper.readValue(espaceJson, Espace.class);
            ReqRes response = espaceService.createEspace(espace, mainImage, galleryImages);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ReqRes errorResponse = new ReqRes();
            errorResponse.setStatusCode(500);
            errorResponse.setMessage("Error deserializing space: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReqRes> updateEspace(
        @PathVariable Long id,
        @RequestPart("espace") String espaceJson,
        @RequestPart(value = "files", required = false) MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Espace espace = objectMapper.readValue(espaceJson, Espace.class);

            ReqRes response = espaceService.updateEspace(id, espace, files);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            ReqRes errorResponse = new ReqRes();
            errorResponse.setStatusCode(500);
            errorResponse.setMessage("Error deserializing space : " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReqRes> deleteEspace(@PathVariable Long id) {
        return ResponseEntity.ok(espaceService.deleteEspace(id));
    }
    
}