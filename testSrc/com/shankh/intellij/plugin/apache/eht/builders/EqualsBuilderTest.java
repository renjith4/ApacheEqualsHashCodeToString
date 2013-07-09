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
public class EqualsBuilderTest {

    private EqualsBuilder underTest;

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
        underTest = new EqualsBuilder();
    }

    @Test
    public void generateEqualsMethodShouldReturnEqualsMethodStringInCorrectFormatIfItExtendsAnotherClass() throws Exception {

        when(psiClass.getName()).thenReturn("MyClass");
        when(psiClass.getSuperClass()).thenReturn(superClass);
        when(superClass.getName()).thenReturn("MySuperClass");

        when(psiField2.getName()).thenReturn("name");
        when(psiField1.getName()).thenReturn("id");


        final String equalsMethodAsString = underTest.generateEqualsMethod(psiClass, fields);
        String expectedEqualsMethodAsString = "@Override\npublic boolean equals(Object obj) {\n"
                + "if (obj == null) { return false; }\n"
                + "if (obj == this) { return true; }\n"
                + "if (obj.getClass() != getClass()) { \nreturn false; \n}\n"
                + "MyClass rhs = (MyClass) obj;\n"
                + "return new org.apache.commons.lang3.builder.EqualsBuilder()\n"
                + ".appendSuper (super.equals(obj))\n"
                + ".append(this.id, rhs.id)\n.append(this.name, rhs.name)\n"
                + ".isEquals();\n}";
        assertThat(equalsMethodAsString, is(expectedEqualsMethodAsString));
    }

    @Test
    public void generateEqualsMethodShouldReturnEqualsMethodStringInCorrectFormatIfItDoesNotExtendAnotherClass() throws Exception {

        when(psiClass.getName()).thenReturn("MyClass");
        when(psiClass.getSuperClass()).thenReturn(superClass);
        when(superClass.getName()).thenReturn("Object");

        when(psiField1.getName()).thenReturn("id");
        when(psiField2.getName()).thenReturn("name");


        final String equalsMethodAsString = underTest.generateEqualsMethod(psiClass, fields);
        String expectedEqualsMethodAsString = "@Override\npublic boolean equals(Object obj) {\n"
                + "if (obj == null) { return false; }\n"
                + "if (obj == this) { return true; }\n"
                + "if (obj.getClass() != getClass()) { \nreturn false; \n}\n"
                + "MyClass rhs = (MyClass) obj;\n"
                + "return new org.apache.commons.lang3.builder.EqualsBuilder()\n"
                + ".append(this.id, rhs.id)\n.append(this.name, rhs.name)\n"
                + ".isEquals();\n}";
        assertThat(equalsMethodAsString, is(expectedEqualsMethodAsString));
    }
}
