package com.diy.framework.web.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 빈 관련된 모든 것을 담아두겠다. 하하
 * <p>
 * - 빈 스캐너도 여기 두고..
 * - 또 뭐 있으려나
 */
public class BeanFactory {

    private static final Logger log = LoggerFactory.getLogger(BeanFactory.class);
    private Set<Class<?>> classes;

    private final Map<Class<?>, Object> store = new HashMap<>();

    public void start() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        BeanScanner beanScanner = new BeanScanner("com.diy");
        classes = beanScanner.scanClassesTypeAnnotatedWith(Component.class);
        for (Class<?> clazz : classes) {
            createBean(clazz);
        }

    }

    // 오토와이어드 붙은 생성자 찾기
    private Constructor<?> findConstructor(Class<?> clazz) throws NoSuchMethodException {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                return constructor;
            }
        }
        return clazz.getDeclaredConstructor();
    }

    private Object createBean(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 이미 store에 있다!!! 그러면 이미 등록된 빈이래요~
        if (store.containsKey(clazz)) {
            return store.get(clazz);
        }

        Constructor<?> constructor = findConstructor(clazz);
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        // 기본 생성자다
        if (parameterTypes.length == 0) {
            Object instance = constructor.newInstance();
            store.put(clazz, instance);
            return instance;
        }

        // 파라미터 있다~?(재귀를 태워라~)
        Object[] params = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            // 인터페이스면!!!!!!!!!!
            if (parameterType.isInterface()) {
                for (Class<?> candidate : classes) {
                    for (Class<?> iface : candidate.getInterfaces()) {
                        if (iface == parameterType) {
                            params[i] = createBean(candidate);
                            break;
                        }
                    }
                }
            } else {
                params[i] = createBean(parameterTypes[i]);
            }
        }

        Object instance = constructor.newInstance(params);
        store.put(clazz, instance);
        return instance;
    }
}
