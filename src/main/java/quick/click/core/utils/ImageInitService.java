package quick.click.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import quick.click.core.domain.model.ImageData;
import quick.click.core.repository.ImageDataRepository;

import java.io.File;
import java.io.IOException;
@Service
public class ImageInitService implements CommandLineRunner {

    public static final Logger LOGGER = LoggerFactory.getLogger(ImageInitService.class);

    private final ImageDataRepository imageDataRepository;

    @Value("${upload.windows}")
    private String WINDOWS_PATH;

    @Value("${upload.linux}")
    private String LINUX_PATH;

    public ImageInitService(final ImageDataRepository imageDataRepository) {
        this.imageDataRepository = imageDataRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        saveImageDataToAdvert( 1L, downLoadFileToMultiPart("1.jpg"));
        saveImageDataToAdvert( 2L, downLoadFileToMultiPart("2.jpg"));
        saveImageDataToAdvert( 3L, downLoadFileToMultiPart("3.jpg"));
        saveImageDataToAdvert( 4L, downLoadFileToMultiPart("4.jpg"));
        saveImageDataToAdvert( 5L, downLoadFileToMultiPart("5.jpg"));
        saveImageDataToAdvert( 6L, downLoadFileToMultiPart("6.jpg"));

    }

    public ImageData saveImageDataToAdvert(final Long imageDataId, final MultipartFile file) throws IOException {
        final ImageData imageData = imageDataRepository.findById(imageDataId).orElseThrow();
        imageData.setImageData(file.getBytes());
        LOGGER.info("In saveImageDataToAdvert save image to imagedata with id: {}", imageDataId);
        return imageDataRepository.saveAndFlush(imageData);
    }

    public MultipartFile downLoadFileToMultiPart(final String name) throws IOException {
        final String filePath;
        if(System.getProperty("os.name").equals("WINDOWS")){
            filePath=System.getProperty("user.dir")+WINDOWS_PATH;
        } else {
            filePath=System.getProperty("user.dir")+LINUX_PATH;
        }
        final File file = new File(filePath+File.separator+name);
        MultipartFile multipartFile = new FileAsMultipartFile(file, "image/jpeg");
        multipartFile.getBytes();
        return multipartFile;
    }

}
