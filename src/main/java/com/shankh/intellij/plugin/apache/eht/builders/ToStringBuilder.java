package com.shankh.intellij.plugin.apache.eht.builders;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

public class ToStringBuilder {

    public static final String APACHE_TO_STRING_BUILDER = "org.apache.commons.lang3.builder.ToStringBuilder";

    public String generateToStringMethod(PsiClass psiClass, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("@Override\npublic String toString() {\n");
        builder.append("return new ").append(APACHE_TO_STRING_BUILDER).append("(this)\n");
        final PsiClass superClass = psiClass.getSuperClass();
        if ( (superClass != null) && !("Object".equals(superClass.getName()))) {
            builder.append(".appendSuper (super.toString())\n");
        }
        for (PsiField field : fields) {
            builder.append(".append(\"").append(field.getName()).append("\",").append(field.getName()).append(")\n");
        }
        builder.append(".toString();\n}");
        return builder.toString();
    }
}
