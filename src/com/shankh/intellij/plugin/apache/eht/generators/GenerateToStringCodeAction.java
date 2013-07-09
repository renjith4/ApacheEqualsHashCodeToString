package com.shankh.intellij.plugin.apache.eht.generators;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.shankh.intellij.plugin.apache.eht.utilities.PsiUtility;
import com.shankh.intellij.plugin.apache.eht.builders.ToStringBuilder;

import java.util.List;

public class GenerateToStringCodeAction extends AnAction {
    private static final String TO_STRING_TITLE = "Select fields for toString()";
    private static final String TO_STRING_LABEL_TEXT = "Fields to include in toString():";

    private final ToStringBuilder toStringBuilder = new ToStringBuilder();

    private final PsiUtility psiUtility = new PsiUtility();

    public void actionPerformed(AnActionEvent e) {
        PsiClass psiClass = psiUtility.getPsiClassFromContext(e);
        GenerateDialog dlg = new GenerateDialog(psiClass, TO_STRING_TITLE, TO_STRING_LABEL_TEXT);
        dlg.show();
        if (dlg.isOK()) {
            generateToString(psiClass, dlg.getFields());
        }
    }

    public void generateToString(final PsiClass psiClass, final List<PsiField> fields) {
        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()) {

            @Override
            protected void run() throws Throwable {
                PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(psiClass.getProject());
                final JavaCodeStyleManager javaCodeStyleManager = JavaCodeStyleManager.getInstance(psiClass.getProject());

                final String toStringMethodAsString = toStringBuilder.generateToStringMethod(psiClass, fields);
                PsiMethod toStringMethod = elementFactory.createMethodFromText(toStringMethodAsString, psiClass);
                PsiElement toStringElement = psiClass.add(toStringMethod);
                javaCodeStyleManager.shortenClassReferences(toStringElement);
            }

        }.execute();
    }

    @Override
    public void update(AnActionEvent e) {
        PsiClass psiClass = psiUtility.getPsiClassFromContext(e);
        e.getPresentation().setEnabled(psiClass != null);
    }
}
