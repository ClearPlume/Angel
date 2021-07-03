package top.fallenangel.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
    private int code;
    private String codeDetail;
    private String msg;
    private T data;

    private Result(ResponseCode code, T data) {
        this.code = code.getCode();
        this.codeDetail = code.getCodeDetail();
        this.msg = code.getMsg();
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> ok(T data) {
        return create(ResponseCode.OK, data);
    }

    public static <T> Result<T> error() {
        return error(null);
    }

    public static <T> Result<T> error(T data) {
        return create(ResponseCode.ERROR, data);
    }

    public static <T> Result<T> create(ResponseCode code) {
        return create(code, null);
    }

    public static <T> Result<T> create(ResponseCode code, T data) {
        return new Result<>(code, data);
    }
}
