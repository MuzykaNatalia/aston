package ru.aston.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import static ru.aston.constant.Constant.SCHEMA_SQL_FILE;

@Component
public class DatabaseInitializer implements InitializingBean {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource resource = new ClassPathResource(SCHEMA_SQL_FILE);
        ScriptUtils.executeSqlScript(jdbcTemplate.getDataSource().getConnection(), resource);
    }
}
