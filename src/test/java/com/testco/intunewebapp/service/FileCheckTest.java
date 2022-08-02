package com.testco.intunewebapp.service;

import com.testco.intunewebapp.service.recieve.FileCheckImpl;
import com.testco.iw.models.FileUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileCheckImpl.class)
public class FileCheckTest {

    @Test
    public void testShitWithPowerMock() throws Exception {

        FileCheckImpl fileCheck = new FileCheckImpl();
        String result = Whitebox.invokeMethod(fileCheck, "literalShit", 12);
        assertEquals("Good", result);
    }

    @Test
    public void mockShitWithPowerMock() throws Exception {

        FileCheckImpl spy = PowerMockito.spy(new FileCheckImpl());
        PowerMockito.when(spy, method(FileCheckImpl.class, "literalShit", int.class))
                .withArguments(anyInt())
                .thenReturn("Good");
        spy.validUpload(new FileUpload());
    }
}
