package com.diy.framework.context;

public record RequestMappingInfo(
        String url,
        RequestMethod method
) {
}
