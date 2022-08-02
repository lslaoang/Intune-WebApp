package com.testco.intunewebapp.service;

import com.testco.intunewebapp.service.recieve.FileCheckImpl;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;

public class FileCheckTest {

    @Test
    public void testShitWithPowerMock() throws Exception {

        FileCheckImpl fileCheck = new FileCheckImpl();
        String result = Whitebox.invokeMethod(fileCheck, "literalShit", 12);
        assertEquals("Good", result);
    }
}
