package io.github.pintowar.jweb.console.repl.impl;

import static java.util.Collections.singletonMap;

import groovy.lang.Binding;
import groovy.transform.TimedInterrupt;
import io.github.pintowar.jweb.console.repl.ReplJsr223;
import io.github.pintowar.jweb.console.repl.ScriptResult;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;

public class GroovyShell extends ReplJsr223 {

  private static final long SCRIPT_TIMEOUT_IN_SECONDS = 5;
  private static final List<String> RECEIVERS_BLACK_LIST =
      Stream.of(System.class, Thread.class).map(Class::getName).collect(Collectors.toList());

  public GroovyShell() {
    super();
  }

  public GroovyShell(Map<String, Object> bindings) {
    super(bindings);
  }

  @Override
  public String getEngineName() {
    return "groovy";
  }

  @Override
  public ScriptResult execute(String script) {
    return groovyShellExecute(script);
  }

  private ScriptResult groovyShellExecute(String script) {
    try (StringWriter writer = new StringWriter()) {
      groovy.lang.GroovyShell groovyShell = createGroovyShell(writer);
      Object result = groovyShell.evaluate(script);
      return ScriptResult.create(result, writer.toString());
    } catch (IOException e) {
      throw new IllegalStateException("Problem while closing script stdout buffer.", e);
    } catch (MultipleCompilationErrorsException e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private groovy.lang.GroovyShell createGroovyShell(Writer writer) {
    CompilerConfiguration configuration = createCompilerConfiguration();
    Binding binding = createBinding(writer);
    return new groovy.lang.GroovyShell(binding, configuration);
  }

  private Binding createBinding(Writer writer) {
    Binding binding = new Binding();
    defaultBindings.forEach(
        (k, v) -> {
          if (!k.equals("out")) {
            binding.setVariable(k, v);
          }
        });

    binding.setProperty("out", writer);
    return binding;
  }

  private CompilerConfiguration createCompilerConfiguration() {
    ASTTransformationCustomizer timedCustomizer =
        new ASTTransformationCustomizer(
            singletonMap("value", SCRIPT_TIMEOUT_IN_SECONDS), TimedInterrupt.class);
    SecureASTCustomizer secureCustomizer = new SecureASTCustomizer();
    secureCustomizer.setDisallowedReceivers(RECEIVERS_BLACK_LIST);
    CompilerConfiguration configuration = new CompilerConfiguration();
    configuration.addCompilationCustomizers(secureCustomizer, timedCustomizer);
    return configuration;
  }
}
