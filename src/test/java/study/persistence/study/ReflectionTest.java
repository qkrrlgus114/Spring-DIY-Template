package study.persistence.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import study.reflection.Loopers;
import study.reflection.PrintView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Loopers 객체 정보 가져오기")
    void showClass() {
        Class<Loopers> loopersClass = Loopers.class;
        logger.debug(loopersClass.getName());

        for (Field field : loopersClass.getDeclaredFields()) {
            logger.debug("필드이름 : " + field.getName() + ", 타입 : " + field.getType());
        }

        for (Constructor<?> constructor : loopersClass.getDeclaredConstructors()) {
            logger.debug("생성자 : " + constructor.getName() + ", 파라미터 : " + Arrays.toString(constructor.getParameters()));
        }

        for (Method method : loopersClass.getDeclaredMethods()) {
            logger.debug("메서드 : " + method.getName() + ", 파라미터 : " + Arrays.toString(method.getParameters()));
        }
    }

    @Test
    @DisplayName("test가 붙은 메서드만 실행하랜다 ....")
    void testMethodRun() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 일단 메서드를 다 구해보자
        Class<Loopers> loopersClass = Loopers.class;
        Loopers loopers = Loopers.class.getDeclaredConstructor().newInstance();

        Method[] methods = loopersClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("test")) {
                // invoke를 사용하면 ㅔㅁ서드를 실행한한다. 개쩌네
                Object result = method.invoke(loopers);

                logger.debug(result.toString());
            }
        }

    }

    @Test
    @DisplayName("어노테이션 있으면 실행하랜다...에휴")
    void testAnnotationMethodRun() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 뭐 어노테이션 뽑아냐 하나? 안되네
        Class<Loopers> loopersClass = Loopers.class;
        Loopers loopers = Loopers.class.getDeclaredConstructor().newInstance();

        Method[] methods = loopersClass.getDeclaredMethods();
        for (Method method : methods) {
            // 캬 어노테이션 있으면
            if (method.isAnnotationPresent(PrintView.class)) {
                // 잘 실행한다 굿
                Object invoke = method.invoke(loopers);
            }
        }
    }

    @Test
    @DisplayName("private 필드에 값을 할당하랜다 ㅋㅋ")
    void privateFieldAccess() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Loopers> loopersClass = Loopers.class;
        Loopers loopers = Loopers.class.getDeclaredConstructor().newInstance();

        Field[] declaredFields = loopersClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 그럼 private 필드 레벨을 찾아야겠지? private는 레벨이 2다.
            if (field.getModifiers() == 2) {
                field.setAccessible(true);

                switch (field.getName()) {
                    case "name":
                        field.set(loopers, "박기현");
                        break;
                    case "age":
                        field.set(loopers, 30);
                        break;
                }
            }
        }

        logger.debug(loopers.testGetAge() + ", " + loopers.testGetName());
    }

    @Test
    @DisplayName("특정 인자를 가진 생성자의 인스턴스를 만들라고?")
    void constructorWithArgs() throws Exception {
        Class<Loopers> loopersClass = Loopers.class;

        Constructor<Loopers> constructor = loopersClass.getDeclaredConstructor(String.class, int.class);

        Loopers loopers = constructor.newInstance("박기현", 30);
    }
}
