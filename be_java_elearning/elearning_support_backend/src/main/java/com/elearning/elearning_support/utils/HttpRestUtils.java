package com.elearning.elearning_support.utils;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.elearning.elearning_support.constants.CharacterConstants;
import com.elearning.elearning_support.utils.gson.GsonMapperUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class HttpRestUtils {

    private final int TIMEOUT_IN_10K_MS = 10 * 1000;
    public static final int TIMEOUT_IN_30K_MS = 30 * 1000;

    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * restTemplateMap with key: timeout in millisecond, value: RestTemplate
     */
    private final Map<Integer, RestTemplate> restTemplateMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        RestTemplate defaultTenSecondRestTemplate = createRestTemplate(TIMEOUT_IN_10K_MS);
        RestTemplate defaultThirtySecondRestTemplate = createRestTemplate(TIMEOUT_IN_30K_MS);
        restTemplateMap.put(TIMEOUT_IN_10K_MS, defaultTenSecondRestTemplate);
        restTemplateMap.put(TIMEOUT_IN_30K_MS, defaultThirtySecondRestTemplate);
    }

    private RestTemplate createRestTemplate(int timeoutInMs) {
        RestTemplate restTemplate = new RestTemplate();
        // Tạo connection manager và client cho RestTemplate
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200); // Số lượng kết nối tối đa
        connectionManager.setDefaultMaxPerRoute(20); // Số lượng kết nối tối đa cho mỗi route

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(timeoutInMs);
        requestFactory.setConnectTimeout(timeoutInMs);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    /**
     * @param timeoutInMs timeout of restTemplate
     * @return if value timeoutInMs is not in (DEFAULT_TIMEOUT_IN_30K_MS, DEFAULT_TIMEOUT_IN_10K_MS), return RestTemplate with timeout is
     * timeoutInMs and put restTemplateMap
     */
    public RestTemplate getRestTemplate(int timeoutInMs) {
        return restTemplateMap.computeIfAbsent(timeoutInMs, k -> createRestTemplate(timeoutInMs));
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng generic (list/map)
     *
     * @param url           Đường dẫn API
     * @param method        Phương thức HTTP
     * @param headers       HTTP headers của lời gọi REST
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về
     * @throws RestClientException Nếu remote server trả về không thành công
     */
    public <T> T exchange(String url, HttpMethod method, HttpHeaders headers, Object request, ParameterizedTypeReference<T> responseClazz,
        int timeout) {
        long startTime = System.currentTimeMillis();
        try {
            HttpEntity<?> entity = new HttpEntity<>(request, headers);
            RestTemplate restTemplate = getRestTemplate(timeout);
            T response = restTemplate.exchange(url, method, entity, responseClazz).getBody();
            long executionTime = System.currentTimeMillis() - startTime;
            log.info("restExchangeRef: method {}, url {}, request {}, response {}, time {} ms",
                method.name(), url, request, response, executionTime);
            return response;
        } catch (RestClientException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("restExchangeRef: RestClientException on method {}, url {}, request {}, time {} ms, error {}",
                method.name(), url, GsonMapperUtils.toJson(request), executionTime, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("restExchangeRef: {} on method {}, url {}, request {}, time {} ms, error {}",
                e.getClass().getName(), method.name(), url, request, executionTime, e.getMessage(), e);
            throw e;
        }
    }

    public <T> T exchange(String url, HttpMethod method, HttpHeaders headers, Object request, ParameterizedTypeReference<T> responseClazz) {
        return exchange(url, method, headers, request, responseClazz, TIMEOUT_IN_10K_MS);
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng object
     *
     * @param url           Đường dẫn API
     * @param method        Phương thức HTTP
     * @param headers       HTTP headers của lời gọi REST
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về
     * @throws RestClientException Nếu remote server trả về không thành công
     */
    public <T> T exchange(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> responseClazz,
        int timeout) {
        long startTime = System.currentTimeMillis();
        try {
            HttpEntity<?> entity = new HttpEntity<>(request, headers);
            RestTemplate restTemplate = getRestTemplate(timeout);
            T response = restTemplate.exchange(url, method, entity, responseClazz).getBody();
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("restExchange: method {}, url {}, request {}, response {}, time {} ms",
                method.name(), url, GsonMapperUtils.toJson(request), GsonMapperUtils.toJson(response), executionTime);
            return response;
        } catch (RestClientException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("restExchange: RestClientException on method {}, url {}, request {}, time {} ms, error {}",
                method.name(), url, GsonMapperUtils.toJson(request), executionTime, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("restExchange: {} on method {}, url {}, request {}, time {} ms, error {}",
                e.getClass().getName(), method.name(), url, GsonMapperUtils.toJson(request), executionTime, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng object
     *
     * @param url           Đường dẫn API
     * @param method        Phương thức HTTP
     * @param headers       HTTP headers của lời gọi REST
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về
     * @param metaData      Dữ lệu bổ sung
     * @throws RestClientException Nếu remote server trả về không thành công
     */
    public <T> T exchange(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> responseClazz,
        int timeout, Map<String, Object> metaData) {
        long startTime = System.currentTimeMillis();
        try {
            HttpEntity<?> entity = new HttpEntity<>(request, headers);
            RestTemplate restTemplate = getRestTemplate(timeout);
            T response = restTemplate.exchange(url, method, entity, responseClazz).getBody();
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("restExchange: method {}, url {}, request {}, response {}, time {} ms",
                method.name(), url, GsonMapperUtils.toJson(request), GsonMapperUtils.toJson(response), executionTime);
            return response;
        } catch (RestClientException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("restExchange: RestClientException on method {}, url {}, request {}, time {} ms, error {}",
                method.name(), url, GsonMapperUtils.toJson(request), executionTime, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            log.error("restExchange: {} on method {}, url {}, request {}, time {} ms, error {}",
                e.getClass().getName(), method.name(), url, GsonMapperUtils.toJson(request), executionTime, e.getMessage(), e);
            throw e;
        }
    }

    public <T> T callRest(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> responseClazz, int timeout,
        Map<String, Object> metaData) {
        try {
            return exchange(url, method, headers, request, responseClazz, timeout, metaData);
        } catch (Exception ignored) {
            return null;
        }
    }

    public <T> T callRest(String url, HttpMethod method, String bearerToken, Object request, Class<T> responseClazz, String contentType,
        Map<String, Object> metaData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (bearerToken != null) {
            headers.setBearerAuth(bearerToken.startsWith(BEARER_PREFIX) ? bearerToken.split(CharacterConstants.SPACE)[1] : bearerToken);
        }
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        return callRest(url, method, headers, request, responseClazz, TIMEOUT_IN_10K_MS, metaData);
    }

    public <T> T exchange(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> clazz) {
        return exchange(url, method, headers, request, clazz, TIMEOUT_IN_10K_MS);
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng object
     *
     * @param url           Đường dẫn API
     * @param method        Phương thức HTTP
     * @param headers       HTTP headers của lời gọi REST
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về
     */
    public <T> T callRest(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> responseClazz, int timeout) {
        return exchange(url, method, headers, request, responseClazz, timeout);
    }

    public <T> T callRest(String url, HttpMethod method, HttpHeaders headers, Object request, Class<T> responseClazz) {
        return callRest(url, method, headers, request, responseClazz, TIMEOUT_IN_10K_MS);
    }

    public <T> T callRest(String url, HttpMethod method, String bearerToken, Object request, Class<T> responseClazz, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (bearerToken != null) {
            headers.setBearerAuth(bearerToken.startsWith(BEARER_PREFIX) ? bearerToken.split(CharacterConstants.SPACE)[1] : bearerToken);
        }
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        return callRest(url, method, headers, request, responseClazz, TIMEOUT_IN_10K_MS);
    }

    public <T> T callRest(String url, HttpMethod method, String bearerToken, Object request, Class<T> responseClazz) {
        return callRest(url, method, bearerToken, request, responseClazz, MediaType.APPLICATION_JSON_VALUE);
    }

    public <T> T post(String url, String bearerToken, Object request, Class<T> responseClazz) {
        return callRest(url, HttpMethod.POST, bearerToken, request, responseClazz, MediaType.APPLICATION_JSON_VALUE);
    }

    public <T> T post(String url, String bearerToken, Object request, Class<T> responseClazz, Map<String, Object> metaData) {
        return callRest(url, HttpMethod.POST, bearerToken, request, responseClazz, MediaType.APPLICATION_JSON_VALUE, metaData);
    }

    public <T> T get(String url, String bearerToken, Object request, Class<T> responseClazz) {
        return callRest(url, HttpMethod.GET, bearerToken, request, responseClazz, MediaType.APPLICATION_JSON_VALUE);
    }

    public <T> T put(String url, String bearerToken, Object request, Class<T> responseClazz) {
        return callRest(url, HttpMethod.PUT, bearerToken, request, responseClazz, MediaType.APPLICATION_JSON_VALUE);
    }

    public <T> T delete(String url, String bearerToken, Object request, Class<T> responseClazz) {
        return callRest(url, HttpMethod.DELETE, bearerToken, request, responseClazz, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng collection
     *
     * @param url           Đường dẫn API
     * @param method        Phương thức HTTP
     * @param headers       HTTP headers của lời gọi REST
     * @param timeout       Thời gian timeout của request
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về
     */
    public <T> T callRestRef(String url, HttpMethod method, HttpHeaders headers, Object request, ParameterizedTypeReference<T> responseClazz,
        int timeout) {
        try {
            return exchange(url, method, headers, request, responseClazz, timeout);
        } catch (Exception ignored) {
            return null;
        }
    }

    public <T> T callRestRef(String url, HttpMethod method, HttpHeaders headers, Object request, ParameterizedTypeReference<T> responseClazz) {
        return callRestRef(url, method, headers, request, responseClazz, TIMEOUT_IN_10K_MS);
    }

    public <T> T callRestRef(String url, HttpMethod method, String bearerToken, Object request, ParameterizedTypeReference<T> responseClazz,
        int timeout, String contextType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.valueOf(MediaType.ALL_VALUE)));
        if (bearerToken != null) {
            headers.setBearerAuth(bearerToken.startsWith(BEARER_PREFIX) ? bearerToken.split(CharacterConstants.SPACE)[1] : bearerToken);
        }
        headers.set(HttpHeaders.CONTENT_TYPE, contextType);
        return callRestRef(url, method, headers, request, responseClazz, timeout);
    }

    public <T> T callRestRef(String url, HttpMethod method, String bearerToken, Object request, ParameterizedTypeReference<T> responseClazz) {
        return callRestRef(url, method, bearerToken, request, responseClazz, TIMEOUT_IN_10K_MS, MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng async
     *
     * @param url           Đường dẫn API
     * @param bearerToken   Token sử dụng để xác thực khi gọi API
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về (dạng Class<T>)
     */
    public <T> void callAsync(String url, String bearerToken, HttpMethod httpMethod, Object request, Class<T> responseClazz,
        CoreSubscriber<T> consumer) {
        reactor.netty.http.client.HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofMillis(TIMEOUT_IN_30K_MS));
        WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, bearerToken)
            .build();

        // Don't attach request body if http method is GET
        if (Objects.equals(httpMethod, HttpMethod.GET)) {
            webClient.method(HttpMethod.GET)
                .retrieve()
                .bodyToMono(responseClazz)
                .subscribe(consumer);
        } else {
            webClient.method(httpMethod)
                .body(Mono.just(request), request.getClass())
                .retrieve()
                .bodyToMono(responseClazz)
                .subscribe(consumer);
        }
    }

    /**
     * Gọi REST API và lấy kết quả trả về dạng async
     *
     * @param url           Đường dẫn API
     * @param bearerToken   Token sử dụng để xác thực khi gọi API
     * @param request       Body object gửi đi khi gọi API
     * @param responseClazz Loại class của object trả về (dạng ParameterizedTypeReference<T>)
     */
    public <T> void callAsyncRef(String url, String bearerToken, HttpMethod httpMethod, Object request, ParameterizedTypeReference<T> responseClazz,
        CoreSubscriber<T> consumer) {
        reactor.netty.http.client.HttpClient httpClient = HttpClient.create()
            .responseTimeout(Duration.ofMillis(TIMEOUT_IN_30K_MS));
        WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, bearerToken)
            .build();

        // Don't attach request body if http method is GET
        if (Objects.equals(httpMethod, HttpMethod.GET)) {
            webClient.method(HttpMethod.GET)
                .retrieve()
                .bodyToMono(responseClazz)
                .subscribe(consumer);
        } else {
            webClient.method(httpMethod)
                .body(Mono.just(request), request.getClass())
                .retrieve()
                .bodyToMono(responseClazz)
                .subscribe(consumer);
        }
    }

}
