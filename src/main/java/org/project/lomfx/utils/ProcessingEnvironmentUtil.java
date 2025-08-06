package org.project.lomfx.utils;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;

import javax.annotation.processing.ProcessingEnvironment;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProcessingEnvironmentUtil {

    public static JavacProcessingEnvironment getJavacProcessingEnvironment(ProcessingEnvironment procEnv) {
        if (procEnv instanceof JavacProcessingEnvironment javacProcEnv) {
            return javacProcEnv;
        }

        for (Class<?> procEnvClass = procEnv.getClass(); procEnvClass != null; procEnvClass = procEnvClass.getSuperclass()) {
            try {
                Object delegate = tryGetDelegateField(procEnvClass, procEnv);

                if (delegate == null) {
                    delegate = tryGetProcessingEnvField(procEnvClass, procEnv);
                }

                if (delegate == null) {
                    delegate = tryGetProxyDelegateToField(procEnv);
                }

                if (delegate instanceof ProcessingEnvironment p) {
                    return getJavacProcessingEnvironment(p);
                }
            } catch (final Exception e) {
                // no valid delegate, try superclass
            }
        }

        return null;
    }

    private static Object tryGetDelegateField(Class<?> delegateClass, Object instance) {
        try {
            return getField(delegateClass, "delegate").get(instance);
        } catch (Exception e) {
            return null;
        }
    }

    private static Object tryGetProcessingEnvField(Class<?> delegateClass, Object instance) {
        try {
            return getField(delegateClass, "processingEnv").get(instance);
        } catch (Exception e) {
            return null;
        }
    }

    private static Object tryGetProxyDelegateToField(Object instance) {
        try {
            InvocationHandler handler = Proxy.getInvocationHandler(instance);
            return getField(handler.getClass(), "val$delegateTo").get(handler);
        } catch (Exception e) {
            return null;
        }
    }

    private static Field getField(Class<?> aClass, String fieldName) {
        Field f = null;
        Class<?> d = aClass;

        while (d != null) {
            try {
                f = d.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException ignored) {

            }

            d = d.getSuperclass();
        }

        if (f != null) {
            f.setAccessible(true);
        }

        return f;
    }

}
