package com.rest_api.apigateway.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Component
@Slf4j
public class DemogatewayApplication {

    @Bean
    public RouterFunction<ServerResponse> customRoutes(){
        log.info("DemogatewayApplication has been started: ");
        RouterFunction<ServerResponse> routerFunction= route("test_route")
                .route(path("/users/**"),http())
                .filter((request,next)->{
                    log.info(request.headers().firstHeader("X-API-KEY"));
                    if(request.headers().firstHeader("X-API-KEY")==null){

                        return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                    }
                    return next.handle(request);
                })
                .before(uri("https://jsonplaceholder.typicode.com/users/1"))
                .build();

        log.info(routerFunction.toString());

        return routerFunction;
    }
}
