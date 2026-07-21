package org.mcpservergenerate.model.enums;


public enum HttpParameterLocation {

    /**
     * @PathVariable
     */
    PATH,

    /**
     * @RequestParam，放在URL查询字符串
     */
    QUERY,

    /**
     * application/x-www-form-urlencoded表单字段
     */
    FORM,

    /**
     * @RequestHeader
     */
    HEADER,

    /**
     * @CookieValue
     */
    COOKIE,

    /**
     * @RequestBody
     */
    BODY,

    /**
     * @RequestPart或文件上传
     */
    MULTIPART
}