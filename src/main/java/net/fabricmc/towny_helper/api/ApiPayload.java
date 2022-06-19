package net.fabricmc.towny_helper.api;

public class ApiPayload<T> {
    private boolean success;
    private T content;
    private String msg;

    public ApiPayload(boolean success, T content) {
        this.success = success;
        this.content = content;
    }

    public ApiPayload(boolean success, T content, String msg) {
        this.success = success;
        this.content = content;
        this.msg = msg;
    }

    public ApiPayload(T content, String msg) {
        this.content = content;
        this.msg = msg;
    }

    public ApiPayload(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static <T> ApiPayload<T> sendSuccessful() {
        return new ApiPayload<T>(true);
    }

    public static <T> ApiPayload<T> sendSuccessWithObject(T object) {
        return new ApiPayload<>(true, object);
    }

    public static <T> ApiPayload<T> sendFail() {
        return new ApiPayload<T>(false);
    }
}

