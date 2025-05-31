package io.github.pintowar.jweb.console.boot;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
@ConditionalOnProperty(value = "jweb.console.enabled", havingValue = "true")
public class EvalConsoleConfiguration {

  @Bean
  public EvalConsoleHandler evalConsoleHandler(ApplicationContext context) {
    return new EvalConsoleHandler(context);
  }

  @Bean
  RouterFunction<ServerResponse> consoleRoutes(EvalConsoleHandler evalConsoleHandler) {
    return route()
        .nest(
            path("/console"),
            builder ->
                builder
                    .GET("", req -> evalConsoleHandler.index())
                    .GET("/engines", req -> evalConsoleHandler.engines())
                    .POST("/{engine}/eval", evalConsoleHandler::eval))
        .build();
  }
}
