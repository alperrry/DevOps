package com.bigdata.second.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resourceName, Long id){
        super(resourceName + "Not found with id:" + id);
    }
}
