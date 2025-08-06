package org.project.lomfx.utils;

public class FxValueMethodsNameGenerator {

    public static String defaultPropertyName(String name) {
        return name + "Property";
    }

    public static String defaultGetterName(String name) {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    public static String defaultSetterName(String name) {
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
