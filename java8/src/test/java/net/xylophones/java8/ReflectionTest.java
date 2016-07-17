package net.xylophones.java8;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ReflectionTest {

    private static Map<Class<?>,Object> TEST_TYPE_VALUES = new HashMap<>();
    static {
        TEST_TYPE_VALUES.put(String.class, "Banana");
        TEST_TYPE_VALUES.put(int.class, 1);
        TEST_TYPE_VALUES.put(List.class, new ArrayList<>());
    }

    @Test
    public void checkEveryBuilderFieldIsCopiedInCopyConstructor() throws InvocationTargetException, IllegalAccessException {
        // given a domain object with all fields set
        DomainObject original = createDomainObjectInstanceWithAllFieldsSetToTestValues();

        // when the object is cloned using the builder copy method
        DomainObject copy = DomainObject.builder(original).build();

        // then all fields are the same in the original and copy
        assertFieldsAreTheSame(original, copy);
    }

    private DomainObject createDomainObjectInstanceWithAllFieldsSetToTestValues() throws IllegalAccessException, InvocationTargetException {
        Class<DomainObject.Builder> domainObjectClass = DomainObject.Builder.class;
        DomainObject.Builder builderInstance = DomainObject.builder();

        for (Method method : domainObjectClass.getDeclaredMethods()) {
            if (isSetter(method)) {
                Class<?> paramterType = method.getParameterTypes()[0];
                Object testValue = TEST_TYPE_VALUES.get(paramterType);
                if (testValue == null) {
                    fail("Please specify a test value for type " + paramterType.getSimpleName() +
                            " in TEST_TYPE_VALUES so copy Builder correctness can be tested");
                }
                method.invoke(builderInstance, testValue);
            }
        }

        return builderInstance.build();
    }

    private void assertFieldsAreTheSame(DomainObject original, DomainObject copy) throws IllegalAccessException, InvocationTargetException {
        for (Method method : original.getClass().getDeclaredMethods()) {
            if (isGetter(method)) {
                Object originalValue = method.invoke(original);
                Object copyValue = method.invoke(copy);

                assertEquals(method.getName() + " returns different value after object is copied. "
                        +"Did you set the value from the copy in DomainObject.Builder.Builder(DomainObject)",
                        originalValue, copyValue);
            }
        }
    }

    private boolean isGetter(Method method) {
        return method.getName().startsWith("get") && method.getParameterTypes().length == 0;
    }

    private boolean isSetter(Method declaredMethod) {
        return declaredMethod.getName().startsWith("set") && declaredMethod.getParameterTypes().length == 1;
    }
}