package com.midas2018mobile5.serverapp.Model.Internal;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s", resourceName, fieldName, fieldValue));
    }
}
