package net.fabricmc.towny_helper.api;

public class ApiPayload {
    private boolean success;
    private Object content;
    private String msg;

    public ApiPayload(boolean success, Object content) {
        this.success = success;
        this.content = content;
    }

    public ApiPayload(boolean success, Object content, String msg) {
        this.success = success;
        this.content = content;
        this.msg = msg;
    }

    public ApiPayload(Object content, String msg) {
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

    public <Type> Type getContent() {
        String str = content.getClass().getName();
        Class<?> classType = null;
        try {
            classType = Class.forName(str);
            return (Type)classType.cast(content);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ApiPayload sendSuccessful(){
        return new ApiPayload(true);
    }
    public static ApiPayload sendSuccessWithObject(Object object){
        return  new ApiPayload(true, object);
    }

    public static ApiPayload sendFail(){
        return new ApiPayload(false);
    }
}
