package com.shankh.intellij.plugin.apache.eht.builders;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

public class EqualsBuilder {
    public static final String APACHE_EQUALS_BUILDER = "org.apache.commons.lang3.builder.EqualsBuilder";

    public String generateEqualsMethod(PsiClass psiClass, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("@Override\npublic boolean equals(");
        builder.append("Object obj) {\n");
        builder.append("if (obj == null) { return false; }\n");
        builder.append("if (obj == this) { return true; }\n");
        builder.append("if (obj.getClass() != getClass()) { \n");
        builder.append("return false; \n}\n");
        builder.append(psiClass.getName()).append(" rhs = (").append(psiClass.getName());
        builder.append(") obj;\n").append("return new ").append(APACHE_EQUALS_BUILDER).append("()\n");

        final PsiClass superClass = psiClass.getSuperClass();
        if ((superClass != null) &&!("Object".equals(superClass.getName()))) {
            builder.append(".appendSuper (super.equals(obj))\n");
        }
        for (PsiField field : fields) {
            builder.append(".append(this.").append(field.getName()).append(", rhs.");
            builder.append(field.getName()).append(")\n");
        }
        builder.append(".isEquals();\n}");
        return builder.toString();
    }
}