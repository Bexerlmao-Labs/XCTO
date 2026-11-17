package cc.bexerlmao.xcto.user.exception;

/**
 * 带业务状态码的运行时异常，由全局异常处理器统一转换为 HTTP 响应。
 */
public class ApiException extends RuntimeException {

    private final int status;

    public ApiException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
