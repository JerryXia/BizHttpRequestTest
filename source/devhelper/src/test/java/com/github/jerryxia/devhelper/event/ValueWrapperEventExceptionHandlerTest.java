package com.github.jerryxia.devhelper.event;

import org.junit.Before;
import org.junit.Test;

public class ValueWrapperEventExceptionHandlerTest {

    private ValueWrapperEventExceptionHandler exceptionHandler;
    
    @Before
    public void init() {
        exceptionHandler = new ValueWrapperEventExceptionHandler();
    }

    @Test
    public void test_handleEventException_print_is_ok() {
        RuntimeException ex = new RuntimeException("test throw ex");
        long sequence = 1L;
        ValueWrapperEvent event = new ValueWrapperEvent();
        exceptionHandler.handleEventException(ex, sequence, event);
    }
}