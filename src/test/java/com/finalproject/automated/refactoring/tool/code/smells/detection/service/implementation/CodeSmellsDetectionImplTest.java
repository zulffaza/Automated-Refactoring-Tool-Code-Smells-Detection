package com.finalproject.automated.refactoring.tool.code.smells.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.code.smells.detection.service.CodeSmellsDetection;
import com.finalproject.automated.refactoring.tool.longg.methods.detection.service.LongMethodsDetection;
import com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.LongParameterMethodsDetection;
import com.finalproject.automated.refactoring.tool.model.CodeSmellName;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 21 April 2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeSmellsDetectionImplTest {

    @Autowired
    private CodeSmellsDetection codeSmellsDetection;

    @MockBean
    private LongMethodsDetection longMethodsDetection;

    @MockBean
    private LongParameterMethodsDetection longParameterMethodsDetection;

    @Value("${threshold.long.methods}")
    private Long longMethodsThreshold;

    @Value("${threshold.long.parameter.methods}")
    private Long longParameterMethodsThreshold;

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SECOND_INDEX = 1;
    private static final Integer CODE_SMELLS_COUNT = 2;
    private static final Integer ONCE_INVOCATION = 1;

    private List<MethodModel> methodModels;
    private List<MethodModel> nullMethodModels;
    private List<MethodModel> nullMethodMethodModels;

    @Before
    public void setUp() {
        methodModels = Collections.singletonList(MethodModel.builder().build());
        nullMethodModels = null;
        nullMethodMethodModels = Collections.singletonList(null);

        doAnswer(this::stubLongMethodsDetection)
                .when(longMethodsDetection)
                .detect(eq(methodModels), eq(longMethodsThreshold));

        doAnswer(this::stubLongParameterMethodsDetection)
                .when(longParameterMethodsDetection)
                .detect(eq(longMethods()), eq(longParameterMethodsThreshold));

        doThrow(NullPointerException.class)
                .when(longMethodsDetection)
                .detect(eq(nullMethodModels), eq(longMethodsThreshold));

        doThrow(NullPointerException.class)
                .when(longMethodsDetection)
                .detect(eq(nullMethodMethodModels), eq(longMethodsThreshold));
    }

    @Test
    public void detect_singleMethod_success() {
        codeSmellsDetection.detect(methodModels.get(FIRST_INDEX));

        assertEquals(CODE_SMELLS_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(FIRST_INDEX));
        assertEquals(CodeSmellName.LONG_PARAMETER_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(SECOND_INDEX));

        verify(longMethodsDetection, times(ONCE_INVOCATION))
                .detect(eq(methodModels), eq(longMethodsThreshold));
        verify(longParameterMethodsDetection, times(ONCE_INVOCATION))
                .detect(eq(methodModels), eq(longParameterMethodsThreshold));
        verifyNoMoreInteractions(longMethodsDetection);
        verifyNoMoreInteractions(longParameterMethodsDetection);
    }

    @Test
    public void detect_multiMethod_success() {
        codeSmellsDetection.detect(methodModels);

        assertEquals(CODE_SMELLS_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(FIRST_INDEX));
        assertEquals(CodeSmellName.LONG_PARAMETER_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(SECOND_INDEX));

        verify(longMethodsDetection, times(ONCE_INVOCATION))
                .detect(eq(methodModels), eq(longMethodsThreshold));
        verify(longParameterMethodsDetection, times(ONCE_INVOCATION))
                .detect(eq(methodModels), eq(longParameterMethodsThreshold));
        verifyNoMoreInteractions(longMethodsDetection);
        verifyNoMoreInteractions(longParameterMethodsDetection);
    }

    @Test(expected = NullPointerException.class)
    public void detect_singleMethod_failed_emptyMethod() {
        MethodModel methodModel = null;
        codeSmellsDetection.detect(methodModel);

        verify(longMethodsDetection, times(ONCE_INVOCATION))
                .detect(eq(nullMethodModels), eq(longMethodsThreshold));
        verifyNoMoreInteractions(longMethodsDetection);
        verifyNoMoreInteractions(longParameterMethodsDetection);
    }

    @Test(expected = NullPointerException.class)
    public void detect_multiMethod_failed_emptyMethod() {
        methodModels = null;
        codeSmellsDetection.detect(methodModels);

        verify(longMethodsDetection, times(ONCE_INVOCATION))
                .detect(eq(nullMethodMethodModels), eq(longMethodsThreshold));
        verifyNoMoreInteractions(longMethodsDetection);
        verifyNoMoreInteractions(longParameterMethodsDetection);
    }

    private Object stubLongMethodsDetection(InvocationOnMock invocationOnMock) {
        List<MethodModel> methodModels = invocationOnMock.getArgument(FIRST_INDEX);
        methodModels.get(FIRST_INDEX)
                .getCodeSmells()
                .add(CodeSmellName.LONG_METHOD);

        return null;
    }

    private Object stubLongParameterMethodsDetection(InvocationOnMock invocationOnMock) {
        List<MethodModel> methodModels = invocationOnMock.getArgument(FIRST_INDEX);
        methodModels.get(FIRST_INDEX)
                .getCodeSmells()
                .add(CodeSmellName.LONG_PARAMETER_METHOD);

        return null;
    }

    private List<MethodModel> longMethods() {
        return Collections.singletonList(MethodModel.builder()
                .codeSmells(Collections.singletonList(CodeSmellName.LONG_METHOD))
                .build());
    }
}