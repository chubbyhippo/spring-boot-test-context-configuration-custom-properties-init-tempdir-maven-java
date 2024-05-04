package io.github.chubbyhippo.customproperties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomPropertiesTempDirTest {

    @Autowired
    private ConfigurableApplicationContext context;

    @TempDir
    File tempDir;

    private File newDir;
    private File newFile;

    @BeforeEach
    public void setup() throws IOException {
        Map<String, Object> map = new HashMap<>();

        // Create new directory in temp directory
        newDir = new File(tempDir, "newDir");
        boolean dirCreated = newDir.mkdir();

        // Create new file in the new directory
        newFile = new File(newDir, "newFile.txt");
        boolean fileCreated = newFile.createNewFile();

        // Ensure directory and file were created successfully
        assertThat(dirCreated).isTrue();
        assertThat(fileCreated).isTrue();

        // put tempDir path into the map with a property key
        map.put("property.tempdir", tempDir.getAbsolutePath());

        // Add properties to the application context's environment
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        propertySources.addFirst(new MapPropertySource("customMAP", map));
    }

    @Test
    @DisplayName("should contain temp dir path")
    void shouldContainTempDirPath() {
        String tempDirProperty = context.getEnvironment().getProperty("property.tempdir");
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
