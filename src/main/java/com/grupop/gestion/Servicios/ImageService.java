package com.grupop.gestion.Servicios;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

//    private static final String DIRECTORY = "src/main/resources/static/uploads";
    private static final String RUTA_ABSOLUTA = "C://Users//Admin//Documents//imagenesGP";
    public String copy(MultipartFile image){
        try{

            String photoName = image.getOriginalFilename();
            Path photoPath = Paths.get(RUTA_ABSOLUTA, photoName).toAbsolutePath();
            Files.copy(image.getInputStream(), photoPath, StandardCopyOption.REPLACE_EXISTING);
            return photoName;
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Error saving image");
        }
    }


}
