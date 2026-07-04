package cc.bexerlmao.xcto.question.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理器，捕获 Jackson 反序列化等框架层异常并记录日志。
 * 这些异常发生在控制器方法执行之前，不会进入控制器内的 try-catch，
 * 因此需要在此统一捕获，确保问题在日志中可见。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 捕获请求体反序列化异常（如 JSON 格式错误、字段类型不匹配等）。
     * 这通常是 HTTP 400 的根本原因。
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Throwable rootCause = getRootCause(ex);
        log.error("请求体反序列化失败 — 类型: {}, 原因: {}",
                rootCause.getClass().getSimpleName(),
                rootCause.getMessage(),
                rootCause instanceof JsonProcessingException ? null : rootCause);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 400);
        body.put("error", "Bad Request");
        body.put("message", "请求体格式错误: " + (rootCause.getMessage() != null
                ? rootCause.getMessage()
                : ex.getMessage()));
        // 不暴露堆栈跟踪到客户端

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * 兜底：捕获所有未处理的异常，记录日志并返回 500。
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error("未处理的异常: {}", ex.getMessage(), ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage() != null ? ex.getMessage() : "Unknown error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Throwable getRootCause(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
