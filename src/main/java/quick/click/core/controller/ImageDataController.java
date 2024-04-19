package quick.click.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import quick.click.commons.exeptions.AuthorizationException;
import quick.click.commons.exeptions.ResourceNotFoundException;
import quick.click.core.service.ImageDataService;
import quick.click.security.commons.model.AuthenticatedUser;

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
     * POST    http://localhost:8080/v1.0/images/1
     * save several images in database as byte and in a folder for the given advert id
     */
    @PostMapping("/{advertId}")
    public ResponseEntity<?> uploadImagesListToAdvert(@PathVariable("advertId") final Long advertId,
                                                      @RequestParam("file") final MultipartFile[] files,
                                                      @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {
        try {
            Arrays.asList(files)
                    .forEach(file -> {
                        try {
                            imageDataService.uploadImageToAdvert(advertId, file, authenticatedUser);
                        } catch (IOException e) {
                            LOGGER.debug("In uploadImagesListToAdvert exception with message {} ", e.getMessage());
                            throw new RuntimeException(e);
                        }
                    });

            LOGGER.debug("In uploadImagesListToAdvert received POST files are uploaded successfully with id {} ", advertId);

            return ResponseEntity.status(HttpStatus.OK).body("Files are uploaded successfully.");

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during an image uploading: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * GET    http://localhost:8080/v1.0/images/1
     * get several images as list of byte[] for the given advert id
     */

    @GetMapping("/{advertId}")
    public ResponseEntity<?> findAllImagesToAdvert(@PathVariable("advertId") final Long advertId) {

        try {
            final List<byte[]> imagesList = imageDataService.findByteListToAdvert(advertId);

            LOGGER.debug("In getAllImagesToAdvert received GET all files for advert with id {} ", advertId);

            return ResponseEntity.status(HttpStatus.OK).body(imagesList);

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Advert not found with id : '{}'", advertId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during finding all images to an advert: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

    /**
     * GET http://localhost:8080/v1.0/images/1/2
     * get a image from database as byte[] for the given imageId and advert id
     */
    @GetMapping("/{advertId}/{imageId}")
    public ResponseEntity<?> findImageByIdAndByAdvertId(@PathVariable("imageId") final Long imageId,
                                                        @PathVariable("advertId") final Long advertId) throws IOException {

        try {
            final byte[] image = imageDataService.findImageByIdAndByAdvertId(imageId, advertId).getImageData();

            LOGGER.debug("In uploadImagesListToAdvert received Get a file for advert with id {} ", advertId);

            return ResponseEntity.status(HttpStatus.OK).body(image);

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Image not found with id : '{}'", imageId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during an Image finding: {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }

    }

    /**
     * DELETE    http://localhost:8080/v1.0/images/1/2
     * delete an image for the given advert id and given image id
     */
    @DeleteMapping("/{advertId}/{imageId}")
    public ResponseEntity<?> deleteImageByIdAndByAdvertId(@PathVariable("imageId") final Long imageId,
                                                          @PathVariable("advertId") final Long advertId,
                                                          @AuthenticationPrincipal final AuthenticatedUser authenticatedUser) {
        try {
            imageDataService.deleteImageByIdAndByAdvertId(imageId, advertId, authenticatedUser);

            LOGGER.debug("In deleteImageById received DELETE image with id : {} ", imageId);

            return ResponseEntity.status(HttpStatus.OK).body("The image has been deleted successfully.");

        } catch (AuthorizationException exception) {

            LOGGER.error("Unauthorized access attempt by user {}", authenticatedUser.getEmail(), exception);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access");

        } catch (ResourceNotFoundException exception) {

            LOGGER.error("Image not found with id : '{}'", imageId, exception);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());

        } catch (Exception exception) {

            LOGGER.error("Unexpected error during an Image deleting : {}", exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");

        }
    }

}

//    /**
//     DELETE    http://localhost:8080/v1.0/images/1/2
//     delete all images for the given advert id and given image id
//     */
//    @DeleteMapping("/{advertId}")
//    public ResponseEntity<?> deleteAllImagesByAdvertId(@PathVariable("advertId") Long advertId){
//        imageDataService.deleteImageDataListToAdvert(advertId);
//
//        LOGGER.debug("In deleteAllImagesByAdvertId received DELETE all images for advert with id : {} ", advertId);
//
//        return ResponseEntity.status(HttpStatus.OK).body("All Images has been deleted successfully.");
//    }
//    /**
//     POST    http://localhost:8080/v1.0/images/image/1
//     */
//    @PostMapping("/image/{advertId}")
//    public ResponseEntity<?>  uploadImageToAdvert(@PathVariable("advertId") Long advertId,
//                                                  @RequestParam("file") MultipartFile file) throws IOException {
//
//        imageDataService.uploadImageToAdvert(advertId, file);
//
//        LOGGER.debug("In uploadImagesListToAdvert received POST file is uploaded successfully with id {} ", advertId);
//
//        return ResponseEntity.status(HttpStatus.OK).body("File is uploaded successfully.");
//
//    }
//    /**
//     GET http://localhost:8080/v1.0/images/image/1
//     get a image from database as byte[] for the given advert id
//     */
//    @GetMapping("/image/{advertId}")
//    public ResponseEntity<?>  getImageToAdvert(@PathVariable("advertId") Long advertId) throws IOException {
//
//        byte[] image = imageDataService.findImageToAdvert(advertId).getImageData();
//
//        LOGGER.debug("In uploadImagesListToAdvert received Get a file for advert with id {} ", advertId);
//
//        return ResponseEntity.status(HttpStatus.OK).body(image);
//
//    }
//    /**
//     GET  http://localhost:8080/v1.0/images/file_system/1
//     get a image from a folder as byte[] for the given advert id
//     */
//
//    @GetMapping("/file_system/{advertId}")
//    public ResponseEntity<?>  downloadImageFromFileSystem(@PathVariable("advertId") Long advertId) throws IOException {
//
//        byte[] image = imageDataService.downloadImageFromFileSystem(advertId);
//
//        LOGGER.debug("In uploadImagesListToAdvert received GET a file for advert with id {} ", advertId);
//
//        return ResponseEntity.status(HttpStatus.OK).body(image);
//
//    }

