package io.github.chubbyhippo.customproperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomPropertiesTempDirTest {

    @Autowired
    private ConfigurableApplicationContext context;

    @TempDir
    private File tempDir;

    private File newDir;
    private File newFile;

    @BeforeEach
    void setup() throws IOException {
        var map = new HashMap<String, Object>();

        // Create new directory in temp directory
        newDir = new File(tempDir, "newDir");
        var dirCreated = newDir.mkdir();

        // Create new file in the new directory
        newFile = new File(newDir, "newFile.txt");
        var fileCreated = newFile.createNewFile();

        // Ensure directory and file were created successfully
        assertThat(dirCreated).isTrue();
        assertThat(fileCreated).isTrue();

        // put tempDir path into the map with a property key
        map.put("property.tempdir", tempDir.getAbsolutePath());

        // Add properties to the application context's environment
        var propertySources = context.getEnvironment().getPropertySources();
        propertySources.addFirst(new MapPropertySource("customMAP", map));
    }

    @Test
    @DisplayName("should contain temp dir path")
    void shouldContainTempDirPath() {
        var tempDirProperty = context.getEnvironment().getProperty("property.tempdir");
        System.out.println(tempDirProperty);
        assertThat(tempDirProperty).isEqualTo(tempDir.getAbsolutePath());
    }

    @Test
    @DisplayName("should create dir and file")
    void shouldCreateDirAndFile() {
        assertThat(newDir).exists();
        assertThat(newFile).exists();
    }

}
