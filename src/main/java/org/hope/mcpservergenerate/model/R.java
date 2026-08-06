package org.hope.mcpservergenerate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 关岁安
 * @since 2026/8/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    private Integer code;

    private String message;

    private T data;

    public static <T> R<T> success(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> fail(Integer code, String message) {
        return new R<>(code, message, null);
    }
}
