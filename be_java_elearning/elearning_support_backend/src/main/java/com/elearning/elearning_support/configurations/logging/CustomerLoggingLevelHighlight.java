package com.elearning.elearning_support.configurations.logging;

import java.util.Objects;
import org.springframework.context.annotation.Configuration;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

@Configuration
public class CustomerLoggingLevelHighlight extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        if (Objects.nonNull(level)) {
            switch (level.toInt()) {
                case Level.ERROR_INT:
                    return ANSIConstants.BOLD + ANSIConstants.RED_FG;
                case Level.WARN_INT:
                    return ANSIConstants.BOLD + ANSIConstants.YELLOW_FG;
                case Level.INFO_INT:
                    return ANSIConstants.BOLD + ANSIConstants.CYAN_FG;
                default:
                    return ANSIConstants.BOLD + ANSIConstants.DEFAULT_FG;
            }
        } else {
            return ANSIConstants.BOLD + ANSIConstants.DEFAULT_FG;
        }
    }
}
