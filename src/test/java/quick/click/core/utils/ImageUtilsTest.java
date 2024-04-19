package quick.click.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.*;

class ImageUtilsTest {

    @Nested
    @DisplayName("When compress a image")
    class CompressImageTests {

        @Test
        void testCompressAndDecompressImage_DecompressedShouldMatchCompressed() {
            byte[] originalData = "This is a test string to simulate image data.".getBytes();

            byte[] compressedData = ImageUtils.compressImage(originalData);
            assertNotNull(compressedData);
            assertFalse(compressedData.length == 0, "Compressed data should not be empty");

            byte[] decompressedData = ImageUtils.decompressImage(compressedData);
            assertNotNull(decompressedData);
            assertFalse(decompressedData.length == 0, "Decompressed data should not be empty");

            // Verify that decompressed data matches the original data
            assertArrayEquals(originalData, decompressedData, "Decompressed data should match the original data");
        }

        @Test
        void testCompressImage_NonEmptyData() {
            byte[] inputData = ("Sample data for compression.Sample data for compression.Sample data for compression." +
                                "Sample data for compression.Sample data for compression.Sample data for compression." +
                                "Sample data for compression.Sample data for compression.Sample data for compression.")
                    .getBytes();
            byte[] compressedData = ImageUtils.compressImage(inputData);

            assertNotNull(compressedData, "Compressed data should not be null");
            assertTrue(compressedData.length > 0, "Compressed data should not be empty");
            assertTrue(compressedData.length < inputData.length, "Compressed data should be smaller than input data");
        }

        @Test
        void testCompressImage_NullInput() {
            assertThrows(NullPointerException.class, () -> {
                ImageUtils.compressImage(null);
            }, "Compressing null should throw NullPointerException.");
        }

        @Test
        void testCompressAndDecompressImage_EmptyData() {
            byte[] emptyData = {};
            byte[] compressedData = ImageUtils.compressImage(emptyData);
            byte[] decompressedData = ImageUtils.decompressImage(compressedData);
            assertArrayEquals(emptyData, decompressedData, "Compressing and decompressing empty data should return empty.");
        }

    }

    @Nested
    @DisplayName("When decompress a image")
    class DecompressImageTests {

        @Test
        void testDecompressImage_ValidCompressedData() throws Exception {
            byte[] originalData = "Another sample data string for testing.".getBytes();
            byte[] compressedData = ImageUtils.compressImage(originalData);

            byte[] decompressedData = ImageUtils.decompressImage(compressedData);
            assertNotNull(decompressedData, "Decompressed data should not be null");
            assertArrayEquals(originalData, decompressedData, "Decompressed data should match the original data");
        }

        @Test
        void testDecompressImage_NullInput() {
            assertThrows(NullPointerException.class, () -> {
                ImageUtils.decompressImage(null);
            }, "Decompressing null should throw NullPointerException.");
        }

    }
}