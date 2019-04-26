package io.github.opensanca.rabbit.config.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import io.github.opensanca.rabbit.config.properties.generic.QueueProperties;
import io.github.opensanca.rabbit.factory.YamlPropertySourceFactory;

@Configuration
@ConfigurationProperties(prefix = CreatePermissionProperties.PREFIX)
@ConditionalOnResource(resources = CreatePermissionProperties.CLASSPATH_YAML)
@PropertySource(factory = YamlPropertySourceFactory.class, value = CreatePermissionProperties.CLASSPATH_YAML)
public class CreatePermissionProperties extends QueueProperties {

    public static final String CLASSPATH_YAML = "classpath:listeners/listener.create-permission.yaml";
    public static final String PREFIX = "listener.create-permission";
}
