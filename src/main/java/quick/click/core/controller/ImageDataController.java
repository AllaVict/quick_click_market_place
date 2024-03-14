package quick.click.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import quick.click.core.service.ImageDataService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static quick.click.commons.constants.ApiVersion.VERSION_1_0;
import static quick.click.commons.constants.Constants.Endpoints.IMAGES_URL;
import static quick.click.core.controller.ImageDataController.BASE_URL;

@CrossOrigin
@RestController
@RequestMapping(BASE_URL)
public class ImageDataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDataController.class);

    public static final String BASE_URL = VERSION_1_0 + IMAGES_URL;

    private final ImageDataService imageDataService;

    public ImageDataController(final ImageDataService imageDataService) {
        this.imageDataService = imageDataService;
    }

    /**
     POST    http://localhost:8080/v1.0/images/1
     save several images in database as byte and in a folder for the given advert id
     */
    @PostMapping("/{advertId}")
    public ResponseEntity<?> uploadImagesListToAdvert(@PathVariable("advertId") Long advertId,
                                                      @RequestParam("file") MultipartFile[] files){

        Arrays.asList(files)
                .forEach(file -> {
                    try {
                        imageDataService.uploadImageToAdvert(advertId,file);
                    } catch (IOException e) {
                        LOGGER.debug("In uploadImagesListToAdvert exception with message {} ", e.getMessage());
                        throw new RuntimeException(e);
                         }
                });

        LOGGER.debug("In uploadImagesListToAdvert received POST files are uploaded successfully with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body("Files are uploaded successfully.");
    }

    /**
     /**
     GET    http://localhost:8080/v1.0/images/1
     get several images as list of byte[] for the given advert id
     */

    @GetMapping("/{advertId}")
    public ResponseEntity<?> getAllImagesToAdvert(@PathVariable("advertId") Long advertId){
        List<byte[]> imagesList = imageDataService.findByteListToAdvert(advertId);

        LOGGER.debug("In getAllImagesToAdvert received GET all files for advert with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body(imagesList);
    }

    /**
     DELETE    http://localhost:8080/v1.0/images/1
     delete all images for the given advert id
     */
    @DeleteMapping("/{advertId}")
    public ResponseEntity<?> deleteImagesListToAdvert(@PathVariable("advertId") Long advertId){

        imageDataService.deleteImageDataListToAdvert(advertId);

        LOGGER.debug("In deleteImagesListToAdvert received DELETE all files for advert with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body("Files were deleted successfully");
    }

    /**
     POST    http://localhost:8080/v1.0/images/image/1
     save a image in database as byte and in a folder for the given advert id
     */
    @PostMapping("/image/{advertId}")
    public ResponseEntity<?>  uploadImageToAdvert(@PathVariable("advertId") Long advertId,
                                                  @RequestParam("file") MultipartFile file) throws IOException {

        imageDataService.uploadImageToAdvert(advertId, file);

        LOGGER.debug("In uploadImagesListToAdvert received POST file is uploaded successfully with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body("File is uploaded successfully.");

    }
    /**
     GET http://localhost:8080/v1.0/images/image/1
     get a image from database as byte[] for the given advert id
     */
    @GetMapping("/image/{advertId}")
    public ResponseEntity<?>  getImageToAdvert(@PathVariable("advertId") Long advertId) throws IOException {

        byte[] image = imageDataService.findImageToAdvert(advertId).getImageData();

        LOGGER.debug("In uploadImagesListToAdvert received Get a file for advert with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body(image);

    }
    /**
     GET  http://localhost:8080/v1.0/images/file_system/1
     get a image from a folder as byte[] for the given advert id
     */

    @GetMapping("/file_system/{advertId}")
    public ResponseEntity<?>  downloadImageFromFileSystem(@PathVariable("advertId") Long advertId) throws IOException {

        byte[] image = imageDataService.downloadImageFromFileSystem(advertId);

        LOGGER.debug("In uploadImagesListToAdvert received GET a file for advert with id {} ", advertId);

        return ResponseEntity.status(HttpStatus.OK).body(image);

    }
}
