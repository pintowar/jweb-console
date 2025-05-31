package io.github.pintowar.jweb.console.micronaut;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.web.router.resource.StaticResourceConfiguration;
import java.util.Collections;

@Factory
public class JwebConsoleStaticConfig {

  @Bean
  public StaticResourceConfiguration jwebConsoleStaticResources(
      final ResourceResolver resourceResolver) {
    final StaticResourceConfiguration conf =
        new StaticResourceConfiguration(resourceResolver, null);
    conf.setEnabled(true);
    conf.setPaths(Collections.singletonList("classpath:public"));
    conf.setMapping("/**");
    return conf;
  }
}
