package io.github.pintowar.jweb.console.boot;

import static java.util.Collections.emptyEnumeration;
import static java.util.Collections.singletonMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletMapping;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.function.RouterFunctions;

class Helper {

  static HttpServletRequest mockRequest(String uri, String method, String script, String engine) {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn(uri);
    when(request.getMethod()).thenReturn(method);
    when(request.getHeaderNames()).thenReturn(emptyEnumeration());
    when(request.getAttributeNames()).thenReturn(emptyEnumeration());
    when(request.getHttpServletMapping()).thenReturn(mock(HttpServletMapping.class));

    if (script != null) {
      when(request.getParameter("script")).thenReturn(script);
    }
    if (engine != null) {
      when(request.getAttribute(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
          .thenReturn(singletonMap("engine", engine));
    }
    return request;
  }

  static HttpServletRequest mockRequest(String uri) {
    return mockRequest(uri, "GET", null, null);
  }
}
