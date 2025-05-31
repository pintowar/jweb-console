package io.github.pintowar.jweb.console.micronaut;

import static org.junit.jupiter.api.Assertions.*;

import io.micronaut.web.router.resource.StaticResourceConfiguration;
import org.junit.jupiter.api.Test;

class JwebConsoleStaticConfigTest {

  private JwebConsoleStaticConfig config = new JwebConsoleStaticConfig();

  @Test
  void shouldCheckConfig() {
    StaticResourceConfiguration resolver = config.jwebConsoleStaticResources(null);

    assertTrue(resolver.isEnabled());
    assertEquals("/**", resolver.getMapping());
  }
}
