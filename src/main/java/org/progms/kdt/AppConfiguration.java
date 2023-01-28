package org.progms.kdt;

import org.progms.kdt.configuration.YamlPropertiesFactory;
import org.springframework.context.annotation.PropertySource;
@PropertySource(value = "application.yaml", factory = YamlPropertiesFactory.class)
public class AppConfiguration {




}
