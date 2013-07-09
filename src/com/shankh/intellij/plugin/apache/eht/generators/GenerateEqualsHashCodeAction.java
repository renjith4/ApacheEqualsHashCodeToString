package com.shankh.intellij.plugin.apache.eht.generators;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.shankh.intellij.plugin.apache.eht.utilities.PsiUtility;
import com.shankh.intellij.plugin.apache.eht.builders.EqualsBuilder;
import com.shankh.intellij.plugin.apache.eht.builders.HashCodeBuilder;

import java.util.List;

public class GenerateEqualsHashCodeAction extends AnAction {
    private static final String TITLE = "Select Fields for equals() and hashCode()";
    private static final String LABEL_TEXT = "Fields to include in equals() and hashCode():";

    private final EqualsBuilder equalsBuilder = new EqualsBuilder();
    private final HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    private final PsiUtility psiUtility = new PsiUtility();

    public void actionPerformed(AnActionEvent e) {
        PsiClass psiClass = psiUtility.getPsiClassFromContext(e);
        GenerateDialog dlg = new GenerateDialog(psiClass, TITLE, LABEL_TEXT);
        dlg.show();
        if (dlg.isOK()) {
            generateEqualsHashCode(psiClass, dlg.getFields());
        }
    }

    public void generateEqualsHashCode(final PsiClass psiClass, final List<PsiField> fields) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {

            @Override
            protected void run() throws Throwable {
                PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
                final JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(psiClass.getProject());

                final String equalsMethodAsString = equalsBuilder.generateEqualsMethod(psiClass, fields);
                PsiMethod equalsMethod = elementFactory.createMethodFromText(equalsMethodAsString, psiClass);
                PsiElement equalsElement = psiClass.add(equalsMethod);
                javaCodeStyleManager.shortenClassReferences(equalsElement);

                final String hashCodeMethodAsString = hashCodeBuilder.generateHashCodeMethod(psiClass, fields);
                PsiMethod hashCodeMethod = elementFactory.createMethodFromText(hashCodeMethodAsString, psiClass);
                PsiElement hashCodeElement = psiClass.add(hashCodeMethod);
                javaCodeStyleManager.shortenClassReferences(hashCodeElement);

            }

        }.execute();
    }

    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = psiUtility.getPsiClassFromContext(e);
        e.getPresentation().setEnabled(psiClass != null);
    }
}
