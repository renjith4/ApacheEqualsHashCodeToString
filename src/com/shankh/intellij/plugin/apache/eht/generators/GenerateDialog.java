package com.shankh.intellij.plugin.apache.eht.generators;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;


public class GenerateDialog extends DialogWrapper {
    private CollectionListModel<PsiField> myFields;
    private final LabeledComponent<JPanel> myComponent;


    @SuppressWarnings("unchecked")
    public GenerateDialog(PsiClass psiClass, String title, String labelText) {
        super(psiClass.getProject());
        setTitle(title);

        myFields = new CollectionListModel<PsiField>(psiClass.getFields());
        JList fieldList = new JBList(myFields);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        decorator.disableAddAction();
        JPanel panel = decorator.createPanel();
        myComponent = LabeledComponent.create(panel, labelText);

        init();
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

