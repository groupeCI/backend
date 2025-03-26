package com.coworking.management.service;

import com.coworking.management.dto.ReqRes;
import com.coworking.management.entity.Espace;
import com.coworking.management.repository.EspaceRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EspaceService {

    @Autowired
    private EspaceRepo espaceRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadDir);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Unable to create upload folder : " + uploadDir, e);
        }
    }

    public ReqRes getAllEspaces() {
        ReqRes reqRes = new ReqRes();
        try {
            List<Espace> espaces = espaceRepo.findAll();
            if (!espaces.isEmpty()) {
                reqRes.setEspacesList(espaces);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No espaces found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getEspaceById(Long id) {
        ReqRes reqRes = new ReqRes();
        try {
            Espace espace = espaceRepo.findById(id).orElseThrow(() -> new RuntimeException("Espace Not found"));
            reqRes.setEspaces(espace);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Espace with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes createEspace(Espace espace, MultipartFile mainImage, MultipartFile[] galleryImages) {
        ReqRes reqRes = new ReqRes();

        try {
            // Validation de l'image principale
            if (mainImage == null || mainImage.isEmpty()) {
                reqRes.setStatusCode(400);
                reqRes.setMessage("Main image is required.");
                return reqRes;
            }

            // Traitement de l'image principale
            String mainImageFilename = saveImage(mainImage);
            espace.setImage(mainImageFilename);

            // Traitement de la galerie (optionnelle)
            if (galleryImages != null && galleryImages.length > 0) {
                List<String> gallery = new ArrayList<>();
                for (MultipartFile file : galleryImages) {
                    if (!file.isEmpty()) {
                        String filename = saveImage(file);
                        gallery.add(filename);
                    }
                }
                espace.setGallery(gallery);
            }

            Espace savedEspace = espaceRepo.save(espace);
            reqRes.setEspaces(savedEspace);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Espace created successfully.");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error creating space: " + e.getMessage());
        }
        return reqRes;
    }

    private String saveImage(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
        return filename;
    }

    public ReqRes updateEspace(Long id, Espace espace, MultipartFile[] files) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Espace> espaceOptional = espaceRepo.findById(id);
            if (espaceOptional.isPresent()) {
                Espace existingEspace = espaceOptional.get();
                existingEspace.setTitle(espace.getTitle());
                existingEspace.setDescription(espace.getDescription());
                existingEspace.setLocalisation(espace.getLocalisation());
                existingEspace.setPrice(espace.getPrice());
                existingEspace.setCapacity(espace.getCapacity());
                existingEspace.setAvailable(espace.isAvailable());
                existingEspace.setHours(espace.getHours());
                existingEspace.setRating(espace.getRating());
                existingEspace.setReviews(espace.getReviews());
                existingEspace.setAmenities(espace.getAmenities());
                existingEspace.setFeatures(espace.getFeatures());

                if (files != null && files.length > 0) {
                    List<String> gallery = new ArrayList<>();
                    for (MultipartFile file : files) {
                        if (!file.isEmpty()) {
                            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
                            gallery.add(filename);
                        }
                    }
                    existingEspace.setGallery(gallery);
                }

                Espace updatedEspace = espaceRepo.save(existingEspace);
                reqRes.setEspaces(updatedEspace);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Espace updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Espace not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteEspace(Long id) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Espace> espaceOptional = espaceRepo.findById(id);
            if (espaceOptional.isPresent()) {
                espaceRepo.deleteById(id);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Espace deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("Espace not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }
}