package com.shankh.intellij.plugin.apache.eht.builders;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ToStringBuilderTest {

    private ToStringBuilder underTest;

    private List<PsiField> fields = new ArrayList<PsiField>();

    @Mock
    private PsiClass psiClass;
    @Mock
    private PsiField psiField1;
    @Mock
    private PsiField psiField2;
    @Mock
    private PsiClass superClass;

    @Before
    public void setUp() throws Exception {
        fields.add(psiField1);
        fields.add(psiField2);
        underTest = new ToStringBuilder();
    }

    @Test
    public void generateToStringMethodShouldReturnToStringInRightFormatIfItExtendsAnotherClass() throws Exception {

        when(psiClass.getSuperClass()).thenReturn(superClass);
        when(superClass.getName()).thenReturn("My Super class");

        when(psiField1.getName()).thenReturn("id");
        when(psiField2.getName()).thenReturn("name");

        final String generatedToString = underTest.generateToStringMethod(psiClass, fields);

        final String expectedToString = "@Override\npublic String toString() {\n"
                + "return new org.apache.commons.lang3.builder.ToStringBuilder(this)\n"
                + ".appendSuper (super.toString())\n"
                + ".append(\"id\",id)\n"
                + ".append(\"name\",name)\n"
                + ".toString();\n}";
        assertThat(generatedToString, is(expectedToString));

    }

    @Test
    public void generateToStringMethodShouldReturnToStringInRightFormatIfItDoesNotExtendAnotherClass() throws Exception {

        when(psiClass.getSuperClass()).thenReturn(superClass);
        when(superClass.getName()).thenReturn("Object");

        when(psiField1.getName()).thenReturn("id");
        when(psiField2.getName()).thenReturn("name");

        final String generatedToString = underTest.generateToStringMethod(psiClass, fields);

        final String expectedToString = "@Override\npublic String toString() {\n"
                + "return new org.apache.commons.lang3.builder.ToStringBuilder(this)\n"
                + ".append(\"id\",id)\n"
                + ".append(\"name\",name)\n"
                + ".toString();\n}";
        assertThat(generatedToString, is(expectedToString));

    }
}
