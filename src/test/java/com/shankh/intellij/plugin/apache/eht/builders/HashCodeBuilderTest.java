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
public class HashCodeBuilderTest {

    private HashCodeBuilder underTest;
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

        underTest = new HashCodeBuilder();
    }

    @Test
    public void generateHashCodeMethodShouldReturnHashCodeInRightFormatIfItExtendsAnotherClass() throws Exception {

        when(psiClass.getSuperClass()).thenReturn(superClass);
        when(superClass.getName()).thenReturn("MySuperClass");

        when(psiField1.getName()).thenReturn("id");
        when(psiField2.getName()).thenReturn("name");

        final String generatedHashCode = underTest.generateHashCodeMethod(psiClass, fields);

        final String expectedHashCode = "@Override\npublic int hashCode() {\n"
                + "return new org.apache.commons.lang3.builder.HashCodeBuilder()\n"
                + ".appendSuper (super.hashCode())\n"
                + ".append(id)\n"
                + ".append(name)\n"
                + ".toHashCode();\n}";

        assertThat(generatedHashCode, is(expectedHashCode));
    }

    @Test
    public void generateHashCodeMethodShouldReturnHashCodeInRightFormatIfItDoesNotExtendAnotherClass() throws Exception {

        when(psiClass.getSuperClass()).thenReturn(superClass);
        when(superClass.getName()).thenReturn("Object");

        when(psiField1.getName()).thenReturn("id");
        when(psiField2.getName()).thenReturn("name");

        final String generatedHashCode = underTest.generateHashCodeMethod(psiClass, fields);

        final String expectedHashCode = "@Override\npublic int hashCode() {\n"
                + "return new org.apache.commons.lang3.builder.HashCodeBuilder()\n"
                + ".append(id)\n"
                + ".append(name)\n"
                + ".toHashCode();\n}";

        assertThat(generatedHashCode, is(expectedHashCode));
    }
}
