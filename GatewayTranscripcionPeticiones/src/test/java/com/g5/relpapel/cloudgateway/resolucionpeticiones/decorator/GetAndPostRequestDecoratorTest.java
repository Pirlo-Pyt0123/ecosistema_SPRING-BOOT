package com.g5.relpapel.cloudgateway.resolucionpeticiones.decorator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g5.relpapel.cloudgateway.resolucionpeticiones.model.GatewayRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetAndPostRequestDecoratorTest {

    private ServerWebExchange exchange;
    private GatewayRequest gatewayRequest;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        exchange = mock(ServerWebExchange.class);
        ServerHttpRequest mockRequest = mock(ServerHttpRequest.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        when(mockRequest.getHeaders()).thenReturn(headers);
        when(mockRequest.getURI()).thenReturn(URI.create("http://localhost:8080/api/books/1"));
        when(exchange.getRequest()).thenReturn(mockRequest);

        URI targetUri = URI.create("http://MSBOOKSCATALOGUE/api/books/1");
        when(exchange.getAttributes()).thenReturn(
                new HashMap<>(Map.of(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, targetUri))
        );

        objectMapper = new ObjectMapper();

        gatewayRequest = new GatewayRequest();
        gatewayRequest.setExchange(exchange);
        gatewayRequest.setHeaders(headers);
        gatewayRequest.setQueryParams(new LinkedMultiValueMap<>());
    }

    @Test
    void getDecorator_returnsGET() {
        GetRequestDecorator decorator = new GetRequestDecorator(gatewayRequest);
        assertThat(decorator.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    void getDecorator_returnsCorrectURI() {
        GetRequestDecorator decorator = new GetRequestDecorator(gatewayRequest);
        assertThat(decorator.getURI().toString()).contains("MSBOOKSCATALOGUE");
    }

    @Test
    void getDecorator_returnsHeaders() {
        GetRequestDecorator decorator = new GetRequestDecorator(gatewayRequest);
        assertThat(decorator.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");
    }

    @Test
    void getDecorator_bodyIsEmpty() {
        GetRequestDecorator decorator = new GetRequestDecorator(gatewayRequest);
        assertThat(decorator.getBody().collectList().block()).isEmpty();
    }

    @Test
    void getDecorator_queryParamsAreIncludedInURI() {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("titulo", "Clean Code");
        gatewayRequest.setQueryParams(params);

        GetRequestDecorator decorator = new GetRequestDecorator(gatewayRequest);

        assertThat(decorator.getURI().toString()).contains("titulo=Clean%20Code");
    }

    @Test
    void postDecorator_returnsPOST() {
        gatewayRequest.setBody(Map.of("isbn", "978-0134685991"));
        PostRequestDecorator decorator = new PostRequestDecorator(gatewayRequest, objectMapper);
        assertThat(decorator.getMethod()).isEqualTo(HttpMethod.POST);
    }

    @Test
    void postDecorator_returnsCorrectURI() {
        gatewayRequest.setBody(Map.of("key", "value"));
        PostRequestDecorator decorator = new PostRequestDecorator(gatewayRequest, objectMapper);
        assertThat(decorator.getURI().toString()).contains("MSBOOKSCATALOGUE");
    }

    @Test
    void postDecorator_returnsHeaders() {
        gatewayRequest.setBody(Map.of("key", "value"));
        PostRequestDecorator decorator = new PostRequestDecorator(gatewayRequest, objectMapper);
        assertThat(decorator.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");
    }

    @Test
    void postDecorator_bodyIsSerializedAsJSON() {
        gatewayRequest.setBody(Map.of("isbn", "978-0134685991"));
        PostRequestDecorator decorator = new PostRequestDecorator(gatewayRequest, objectMapper);

        var buffers = decorator.getBody().collectList().block();
        assertThat(buffers).isNotEmpty();

        byte[] bytes = new byte[buffers.get(0).readableByteCount()];
        buffers.get(0).read(bytes);
        String body = new String(bytes);

        assertThat(body).contains("isbn");
        assertThat(body).contains("978-0134685991");
    }

    @Test
    void factory_withGET_returnsGetRequestDecorator() {
        gatewayRequest.setTargetMethod(HttpMethod.GET);
        RequestDecoratorFactory factory = new RequestDecoratorFactory(objectMapper);
        assertThat(factory.getDecorator(gatewayRequest)).isInstanceOf(GetRequestDecorator.class);
    }

    @Test
    void factory_withPOST_returnsPostRequestDecorator() {
        gatewayRequest.setTargetMethod(HttpMethod.POST);
        gatewayRequest.setBody(Map.of("key", "value"));
        RequestDecoratorFactory factory = new RequestDecoratorFactory(objectMapper);
        assertThat(factory.getDecorator(gatewayRequest)).isInstanceOf(PostRequestDecorator.class);
    }

    @Test
    void factory_withInvalidMethod_throwsIllegalArgumentException() {
        gatewayRequest.setTargetMethod(HttpMethod.DELETE);
        RequestDecoratorFactory factory = new RequestDecoratorFactory(objectMapper);
        assertThrows(IllegalArgumentException.class, () -> factory.getDecorator(gatewayRequest));
    }
}
