package io.github.springstudent.common.bean;

/**
 * 异常工具类
 *
 * @author ZhouNing
 **/
public class ExceptionUtil {

    public static String getExceptionMessage(Throwable throwable) {
        StringBuilder message = new StringBuilder();
        message.append(throwable.toString());
        message.append(System.lineSeparator());
        Throwable cause = throwable.getCause();
        if (cause != null) {
            message.append("Caused by: ");
            message.append(getExceptionMessage(cause));
        }
        return message.toString();
    }
}
