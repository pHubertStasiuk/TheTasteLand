package com.tasteland.app.thetasteland.utils;

import com.tasteland.app.thetasteland.shared.exceptions.utils.InvalidPropertyFormatException;
import com.tasteland.app.thetasteland.shared.exceptions.utils.InvalidPropertyName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

@Component
public class PropertyUtils {

    private final Environment environment;

    @Autowired
    public PropertyUtils(Environment environment) {
        this.environment = environment;
    }

    public Integer getInt(String property) {
        try {
            Optional<String> prop = Optional.of(environment.getProperty(property));
            if (prop.isPresent()) return Integer.parseInt(prop.get());
        } catch (NumberFormatException nfe) {
            throw new InvalidPropertyFormatException("Property: " + property + " could not be cast to Integer");
        }
        throw new InvalidPropertyName("Invalid property name: " + property);
    }

    public String get(String property) {
        Optional<String> prop = Optional.of(environment.getProperty(property));
        if (prop.isPresent()) {
            return prop.get();
        } else {
            throw new InvalidPropertyName("Invalid property name: " + property);
        }
    }

    public String decode(String property) {
        String prop = get(property);
        byte[] decodedProperty = Base64.getDecoder().decode(prop.getBytes());
        return new String(decodedProperty);
    }

    public String encode(String property) {
        String prop = get(property);
        return Base64.getEncoder()
                .withoutPadding()
                .encodeToString(prop.getBytes());
    }
}
