package io.github.chubbyhippo.customproperties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomPropertiesApplicationTests {

    @Autowired
    private ConfigurableApplicationContext context;

    @Test
    @DisplayName("should change property dynamically")
    void shouldChangePropertyDynamically() {

        //Get application properties
        var propertySources = context.getEnvironment().getPropertySources();
        assertThat(context.getEnvironment()
                .getProperty("custom.property")).isEqualTo("default");

        //Change custom.property value
        var map = new HashMap<String, Object>();
        map.put("custom.property", "new-value");

        propertySources.addFirst(new MapPropertySource("customMAP", map));
        assertThat(context.getEnvironment()
                .getProperty("custom.property")).isEqualTo("new-value");
    }
}