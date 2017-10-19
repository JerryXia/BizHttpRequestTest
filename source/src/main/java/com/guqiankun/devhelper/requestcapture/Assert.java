package com.guqiankun.devhelper.requestcapture;

final class Assert {
    /**
     * Cast an object to the given class and return it, or throw
     * IllegalArgumentException if it's not assignable to that class.
     *
     * @param clazz
     *            the class to cast to
     * @param value
     *            the value to cast
     * @param errorMessage
     *            the error message to include in the exception
     * @param <T>
     *            the Class type
     * @return value cast to clazz
     * @throws IllegalArgumentException
     *             if value is not assignable to clazz
     */
    @SuppressWarnings("unchecked")
    public static <T> T convertToType(final Class<T> clazz, final Object value, final String errorMessage) {
        if (!clazz.isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException(errorMessage);
        }
        return (T) value;
    }

    /**
     * Throw IllegalStateException if the condition if false.
     *
     * @param name
     *            the name of the state that is being checked
     * @param condition
     *            the condition about the parameter to check
     * @throws IllegalStateException
     *             if the condition is false
     */
    public static void isTrue(final String name, final boolean condition) {
        if (!condition) {
            throw new IllegalStateException("state should be: " + name);
        }
    }

    /**
     * Throw IllegalArgumentException if the condition if false.
     *
     * @param name
     *            the name of the state that is being checked
     * @param condition
     *            the condition about the parameter to check
     * @throws IllegalArgumentException
     *             if the condition is false
     */
    public static void isTrueArgument(final String name, final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("state should be: " + name);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }

    /**
     * Throw IllegalArgumentException if the value is null.
     *
     * @param name
     *            the parameter name
     * @param value
     *            the value that should not be null
     * @param <T>
     *            the value type
     * @return the value
     * @throws IllegalArgumentException
     *             if value is null
     */
    public static <T> T notNull(final String name, final T value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " can not be null");
        }
        return value;
    }

    public static void hasLength(String text, String message) {
        if (text == null || text.length() == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasLength(String text) {
        hasLength(text, "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    public static void hasText(String text, String message) {
        boolean flag = false;
        if (text == null || text.length() == 0) {
            flag = false;
        } else {
            int strLen = text.length();
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(text.charAt(i))) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void hasText(String text) {
        hasText(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void noNullElements(Object[] array) {
        noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }
}
