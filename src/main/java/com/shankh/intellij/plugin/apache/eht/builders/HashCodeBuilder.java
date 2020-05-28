package com.shankh.intellij.plugin.apache.eht.builders;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.List;

public class HashCodeBuilder {
    public static final String APACHE_HASH_CODE_BUILDER = "org.apache.commons.lang3.builder.HashCodeBuilder";

    public String generateHashCodeMethod(PsiClass psiClass, List<PsiField> fields) {
        StringBuilder builder = new StringBuilder("@Override\npublic int hashCode() {\n");
        builder.append("return new ").append(APACHE_HASH_CODE_BUILDER).append("()\n");
        final PsiClass superClass = psiClass.getSuperClass();
        if ((superClass != null) && !("Object".equals(superClass.getName()))) {
            builder.append(".appendSuper (super.hashCode())\n");
        }
        for (PsiField field : fields) {
            builder.append(".append(").append(field.getName()).append(")\n");
        }
        builder.append(".toHashCode();\n}");
        return builder.toString();
    }
}
