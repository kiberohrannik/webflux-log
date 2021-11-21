package com.kiberohrannik.webflux_addons.logging.filter;

import com.kiberohrannik.webflux_addons.logging.creator.*;

import java.util.ArrayList;
import java.util.List;

public class LogRequestFilterFactory {

    public static LogRequestFilter defaultFilter(LoggingProperties loggingProperties) {
        List<RequestDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());
        formatters.add(new CookieMessageFormatter());
        formatters.add(new BodyMessageFormatter());

        return new BaseLogRequestFilter(new BaseRequestMessageCreator(loggingProperties, formatters));
    }

    public static LogRequestFilter defaultFilter(RequestMessageCreator requestMessageCreator) {
        return new BaseLogRequestFilter(requestMessageCreator);
    }
}