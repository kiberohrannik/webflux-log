package com.kiberohrannik.webflux_addons.logging.response.filter;


import com.kiberohrannik.webflux_addons.logging.LoggingProperties;
import com.kiberohrannik.webflux_addons.logging.response.message.BaseResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.response.message.ResponseMessageCreator;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.HeaderMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.ReqIdMessageFormatter;
import com.kiberohrannik.webflux_addons.logging.response.message.formatter.ResponseDataMessageFormatter;

import java.util.ArrayList;
import java.util.List;

public class LogResponseFilterFactory {

    public static LogResponseFilter defaultFilter(LoggingProperties loggingProperties) {
        List<ResponseDataMessageFormatter> formatters = new ArrayList<>();
        formatters.add(new ReqIdMessageFormatter());
        formatters.add(new HeaderMessageFormatter());

        return new BaseLogResponseFilter(new BaseResponseMessageCreator(loggingProperties, formatters));
    }

    public static LogResponseFilter defaultFilter(ResponseMessageCreator responseMessageCreator) {
        return new BaseLogResponseFilter(responseMessageCreator);
    }
}