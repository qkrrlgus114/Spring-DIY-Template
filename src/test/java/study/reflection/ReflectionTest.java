package study.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


public class ReflectionTest {
    private static final Logger log = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("study.reflection.Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        log.debug(carClass.getName());
    }

    @Test
    void testMethodRun() throws Exception {
        Class<Car> carClass = Car.class;
        Car car = carClass.getDeclaredConstructor(String.class, int.class).newInstance("Lia's Car", 5000);

        Method[] declaredMethods = carClass.getDeclaredMethods();
        for (Method method: declaredMethods) {
            if (method.getName().startsWith("test")) {
                // carClass.newInstance(); -> 'newInstance()' is deprecated since version 9
                log.debug(method.invoke(car).toString());
            }
        }
    }

    @Test
    void testAnnotationMethodRun() throws Exception {
        Class<Car> carClass = Car.class;
        Car car = carClass.getDeclaredConstructor(String.class, int.class).newInstance("Lia's Car", 5000);

        Method[] declaredMethods = carClass.getDeclaredMethods();
        for (Method method: declaredMethods) {
            if (method.isAnnotationPresent(PrintView.class)) {
                method.invoke(car);
            }
        }
    }

    @Test
    void privateFieldAccess() throws Exception {
        Class<Car> carClass = Car.class;
        Car car = carClass.getDeclaredConstructor().newInstance();

        Field name = carClass.getDeclaredField("name");
        Field price = carClass.getDeclaredField("price");
        name.setAccessible(true);
        price.setAccessible(true);

        name.set(car, "Lia's Car Ver.2");
        price.set(car, 10000);

        log.debug(car.testGetName());
        log.debug(car.testGetPrice());
    }
}
