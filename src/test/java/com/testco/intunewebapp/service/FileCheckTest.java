package com.testco.intunewebapp.service;

import com.testco.intunewebapp.service.recieve.FileCheck;
import com.testco.intunewebapp.service.recieve.FileCheckImpl;
import com.testco.iw.models.FileUpload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileCheckImpl.class, FileCheck.class})
public class FileCheckTest {

    @Test(expected = RuntimeException.class)
    public void shitTest() throws Exception {
        FileCheckImpl fileCheck = PowerMockito.spy(new FileCheckImpl());
        when(fileCheck, method(FileCheckImpl.class,"literalShit", String.class))
                .withArguments(anyString())
                .thenReturn("IbangShit");
        fileCheck.validUpload(new FileUpload());
    }
}
