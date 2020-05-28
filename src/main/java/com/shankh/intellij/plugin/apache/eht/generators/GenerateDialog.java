package com.shankh.intellij.plugin.apache.eht.generators;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifierList;
import com.intellij.psi.PsiType;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class GenerateDialog extends DialogWrapper {
    private final CollectionListModel<PsiField> myFields;
    private final LabeledComponent<JPanel> myComponent;

    @SuppressWarnings("unchecked")
    public GenerateDialog(PsiClass psiClass, String title, String labelText) {
        super(psiClass.getProject());
        setTitle(title);

        myFields = new CollectionListModel<PsiField>(fieldsFrom(psiClass));
        JList fieldList = new JBList(myFields);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        JPanel panel = decorator.createPanel();
        myComponent = LabeledComponent.create(panel, labelText);

        init();
    }

    private PsiField[] fieldsFrom(final PsiClass psiClass) {
        final List<PsiField> filteredFields = new ArrayList<PsiField>();
        for (PsiField psiField : psiClass.getFields()) {
            if (isNotSerialVersionUIDField(psiField)
                    && isNotStaticFinalField(psiField)) {
                filteredFields.add(psiField);
            }
        }

        return filteredFields.toArray(new PsiField[filteredFields.size()]);
    }

    private boolean isNotStaticFinalField(final PsiField psiField) {
        return !(hasModifier(psiField, "final") && hasModifier(psiField, "static"));
    }

    private boolean isNotSerialVersionUIDField(final PsiField psiField) {
        return !("serialVersionUID".equals(psiField.getName())
                && hasModifier(psiField, "static")
                && hasModifier(psiField, "final")
                && PsiType.LONG.equals(psiField.getType()));
    }

    private boolean hasModifier(final PsiField field, final String modifier) {
        final PsiModifierList modifierList = field.getModifierList();
        return modifierList != null && modifierList.hasModifierProperty(modifier);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return myComponent;
    }

    public List<PsiField> getFields() {
        return myFields.getItems();
    }
}

