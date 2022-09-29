package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    private final Reflections reflections;

    public ControllerScanner(final Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getControllers() {
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(classes);
    }

    private Map<Class<?>, Object> instantiateControllers(final Set<Class<?>> classes) {
        final Map<Class<?>, Object> controllers = new HashMap<>();
        for (final Class<?> clazz : classes) {
            addController(controllers, clazz);
        }
        return controllers;
    }

    private void addController(Map<Class<?>, Object> controllers, Class<?> clazz) {
        try {
            controllers.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            log.error("class constructor error");
            throw new RuntimeException();
        }
    }
}