package com.giantmachines.biblio.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

/**
 * Base class for controllers.  Provides some common functionality that I want to occur behind the scenes.
 */
public class AbstractBaseController {

    private static final String APP_NAME = "biblio";

    protected String getPath(){
        String path = this.getClass().getAnnotation(RequestMapping.class).value()[0];
        return path.replace("/", "");
    }

    /**
     * For get requests.  Returns the requested object.
     * @param entity The entity to return
     * @return A ResponseEntity
     */
    ResponseEntity buildOkResponse(Object entity) {
        return ResponseEntity.ok().body(entity);
    }

    /**
     * For saves and deletes
     * @param id The id of the object to update
     * @return A ResponseEntity
     */
    ResponseEntity buildCreatedResponse(Long id) {
        String url = String.format("http:localhost:8080/%s/%s/", APP_NAME, this.getPath());
        URI location = URI.create(url + id);
        return ResponseEntity.created(location).build();
    }
}
