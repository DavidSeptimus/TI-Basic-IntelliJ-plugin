package nl.petertillema.tibasic.editor.dcs;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

@Service(Service.Level.APP)
public final class DCSIconEditorService {

    public static DCSIconEditorService getInstance() {
        return ApplicationManager.getApplication().getService(DCSIconEditorService.class);
    }

    public void showPopup(RelativePoint relativePoint, String iconData, Consumer<String> onSave) {
        // Setup main panels
        DCSIconEditorPanel editorPanel = new DCSIconEditorPanel(iconData);
        DCSColorPalettePanel palettePanel = new DCSColorPalettePanel();
        palettePanel.setSelectedIndex(editorPanel.getSelectedColorIndex());
        palettePanel.setSelectionListener(editorPanel::setSelectedColorIndex);

        // Create the "Save" button
        JButton saveButton = new JButton("Save");
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(palettePanel);
        southPanel.add(Box.createVerticalStrut(6));
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.add(saveButton);
        southPanel.add(buttonRow);

        JPanel container = new JPanel(new BorderLayout());
        container.add(editorPanel, BorderLayout.CENTER);
        container.add(southPanel, BorderLayout.SOUTH);

        JBPopup popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(container, null)
                .setShowBorder(true)
                .setRequestFocus(true)
                .setResizable(true)
                .createPopup();

        // Wire Save button to callback and close
        saveButton.addActionListener(e -> {
            if (onSave != null) {
                onSave.accept(editorPanel.getIconData());
            }
            popup.cancel();
        });

        popup.show(relativePoint);
    }

}
