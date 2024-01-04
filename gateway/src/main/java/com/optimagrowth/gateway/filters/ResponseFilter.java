package com.optimagrowth.gateway.filters;

import brave.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Configuration
public class ResponseFilter {
    final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    Tracer tracer;


    @Bean
    public GlobalFilter globalFilter(){
        final String traceId = Optional.ofNullable(tracer.currentSpan())
                .orElse(tracer.nextSpan()).context().traceIdString();
        //todo try micrometer Tracer and brave.Zipkin tracer
        return (exchange, chain) ->  {
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
//                        HttpHeaders headers = exchange.getRequest().getHeaders();
//                        final String correlationId = filterUtils.getCorrelationId(headers);

                        logger.debug("Adding the correlation id to the outbound headers. {}", traceId);
                        exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
                        logger.debug("Completing outgoing request for {}.", exchange.getRequest().getURI());
                    }));
        };
    }
}
