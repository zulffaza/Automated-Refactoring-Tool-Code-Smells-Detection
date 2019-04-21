package com.finalproject.automated.refactoring.tool.code.smells.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.code.smells.detection.service.CodeSmellsDetection;
import com.finalproject.automated.refactoring.tool.longg.methods.detection.service.LongMethodsDetection;
import com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.LongParameterMethodsDetection;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Faza Zulfika P P
 * @version 1.0.0
 * @since 21 April 2019
 */

@Service
public class CodeSmellsDetectionImpl implements CodeSmellsDetection {

    @Autowired
    private LongMethodsDetection longMethodsDetection;

    @Autowired
    private LongParameterMethodsDetection longParameterMethodsDetection;

    @Value("${threshold.long.methods}")
    private Long longMethodsThreshold;

    @Value("${threshold.long.parameter.methods}")
    private Long longParameterMethodsThreshold;

    @Override
    public void detect(MethodModel methodModel) {
        detect(Collections.singletonList(methodModel));
    }

    @Override
    public void detect(List<MethodModel> methodModels) {
        detectBloaters(methodModels);
    }

    private void detectBloaters(List<MethodModel> methodModels) {
        longMethodsDetection.detect(methodModels, longMethodsThreshold);
        longParameterMethodsDetection.detect(methodModels, longParameterMethodsThreshold);
    }
}
