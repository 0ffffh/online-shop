package com.k0s.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new HashMap<>();


    public static <T> T getService(Class<T> serviceClass){
        return serviceClass.cast(SERVICES.get(serviceClass));
    }
    public static void addService(Class<?> serviceClass, Object service){
        SERVICES.put(serviceClass, service);
    }
}
