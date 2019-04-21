package com.finalproject.automated.refactoring.tool.code.smells.detection;

import com.finalproject.automated.refactoring.tool.code.smells.detection.service.CodeSmellsDetection;
import com.finalproject.automated.refactoring.tool.model.CodeSmellName;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 21 April 2019
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private CodeSmellsDetection codeSmellsDetection;

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SECOND_INDEX = 1;
    private static final Integer LONG_METHOD_COUNT = 1;
    private static final Integer LONG_PARAMETER_METHOD_COUNT = 1;
    private static final Integer NORMAL_METHOD_COUNT = 0;

    private List<MethodModel> methodModels;

    @Before
    public void setUp() {
        methodModels = createMethodModels();
    }

    @Test
    public void detect_singleMethod_success_longParameterMethod() {
        codeSmellsDetection.detect(methodModels.get(FIRST_INDEX));

        assertEquals(LONG_PARAMETER_METHOD_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_PARAMETER_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(FIRST_INDEX));
    }

    @Test
    public void detect_singleMethod_success_longMethod() {
        codeSmellsDetection.detect(methodModels.get(SECOND_INDEX));

        assertEquals(LONG_METHOD_COUNT.intValue(), methodModels.get(SECOND_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_METHOD, methodModels.get(SECOND_INDEX).getCodeSmells().get(FIRST_INDEX));
    }

    @Test
    public void detect_singleMethod_success_normalMethod() {
        MethodModel methodModel = methodModels.get(FIRST_INDEX);
        methodModel.setParameters(new ArrayList<>());

        codeSmellsDetection.detect(methodModel);

        assertEquals(NORMAL_METHOD_COUNT.intValue(), methodModel.getCodeSmells().size());
    }

    @Test
    public void detect_multiMethod_success() {
        codeSmellsDetection.detect(methodModels);

        assertEquals(LONG_PARAMETER_METHOD_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(LONG_METHOD_COUNT.intValue(), methodModels.get(SECOND_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_PARAMETER_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(FIRST_INDEX));
        assertEquals(CodeSmellName.LONG_METHOD, methodModels.get(SECOND_INDEX).getCodeSmells().get(FIRST_INDEX));
    }

    @Test
    public void detect_multiMethod_success_longParameterMethod() {
        methodModels.get(SECOND_INDEX)
                .setBody("Hello World!");

        codeSmellsDetection.detect(methodModels);

        assertEquals(LONG_PARAMETER_METHOD_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(NORMAL_METHOD_COUNT.intValue(), methodModels.get(SECOND_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_PARAMETER_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(FIRST_INDEX));
    }

    @Test
    public void detect_multiMethod_success_longMethod() {
        methodModels.get(FIRST_INDEX)
                .setParameters(new ArrayList<>());

        codeSmellsDetection.detect(methodModels);

        assertEquals(NORMAL_METHOD_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(LONG_METHOD_COUNT.intValue(), methodModels.get(SECOND_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_METHOD, methodModels.get(SECOND_INDEX).getCodeSmells().get(FIRST_INDEX));
    }

    @Test
    public void detect_multiMethod_success_normalMethod() {
        methodModels.get(FIRST_INDEX)
                .setParameters(new ArrayList<>());
        methodModels.get(SECOND_INDEX)
                .setBody("Hello World!");

        codeSmellsDetection.detect(methodModels);

        assertEquals(NORMAL_METHOD_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(NORMAL_METHOD_COUNT.intValue(), methodModels.get(SECOND_INDEX).getCodeSmells().size());
    }

    @Test(expected = NullPointerException.class)
    public void detect_singleMethod_failed_emptyMethod() {
        MethodModel methodModel = null;
        codeSmellsDetection.detect(methodModel);
    }

    @Test(expected = NullPointerException.class)
    public void detect_multiMethod_failed_emptyMethod() {
        methodModels = null;
        codeSmellsDetection.detect(methodModels);
    }

    private List<MethodModel> createMethodModels() {
        List<MethodModel> methodModels = new ArrayList<>();

        methodModels.add(MethodModel.builder()
                .keywords(Collections.singletonList("public"))
                .name("EmailHelp")
                .parameters(Arrays.asList(
                        PropertyModel.builder()
                                .type("String")
                                .name("emailDestination")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailCc")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailBcc")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailSubject")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailContent")
                                .build()))
                .exceptions(Arrays.asList("Exception", "IOException"))
                .body("\n" +
                        "       mEmailSubject = emailDestination;\n" +
                        "       mEmailSubject = emailCc;\n" +
                        "       mEmailSubject = emailBcc;\n" +
                        "       mEmailSubject = emailSubject;\n" +
                        "       mEmailContent = emailContent;\n" +
                        "\n")
                .build());

        methodModels.add(MethodModel.builder()
                .keywords(Collections.singletonList("public"))
                .returnType("MyResponse<Integer>")
                .name("addGiftInfoCategory")
                .parameters(Collections.singletonList(
                        PropertyModel.builder()
                                .type("GiftInfoCategory")
                                .name("giftInfoCategory")
                                .build()))
                .body("\n" +
                        "        String message;\n" +
                        "        int response;\n" +
                        "\n" +
                        "        try {\n" +
                        "            giftInfoCategory = mGiftInfoCategoryService.addGiftInfoCategory(giftInfoCategory);\n" +
                        "\n" +
                        "            boolean isSuccess = giftInfoCategory != null;\n" +
                        "            message = isSuccess ? \"Gift info category add success\" : \"Gift info category add failed\";\n" +
                        "            response = isSuccess ? 1 : 0;\n" +
                        "        } catch (DataIntegrityViolationException e) {\n" +
                        "            message = \"Gift info category add failed - Gift info category already exists\";\n" +
                        "            response = 0;\n" +
                        "        } catch (Exception e) {\n" +
                        "            message = \"Gift info category add failed - Internal Server Error\";\n" +
                        "            response = 0;\n" +
                        "        }\n" +
                        "\n" +
                        "        return new MyResponse<>(message, response);\n" +
                        "\n")
                .build());

        return methodModels;
    }
}
