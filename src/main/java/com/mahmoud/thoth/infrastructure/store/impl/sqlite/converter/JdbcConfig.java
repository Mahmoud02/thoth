package com.mahmoud.thoth.infrastructure.store.impl.sqlite.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.lang.NonNull;

import java.util.Arrays;

@Configuration
public class JdbcConfig extends AbstractJdbcConfiguration {

    @Override
    @NonNull
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(
                new JsonbWritingConverter(),
                new JsonbReadingConverter()
        ));
    }
}
