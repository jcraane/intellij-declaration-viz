package nl.capaxit.idea.plugins.usagevisualizer.configuration;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.ColorPicker;
import com.intellij.ui.components.panels.HorizontalBox;
import com.intellij.ui.components.panels.VerticalBox;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Configuration component for the Usage Visualizer plugin.
 *
 * Created by jamiecraane on 06/04/2017.
 */
public class UsageVisualizationConfiguration extends BaseConfigurable {
    private JPanel settingsUi;
    private UsageVisualizationConfig config;
    private ComboBox<String> lineTypeComboBox;
    private ColorPicker colorPicker;
    private JCheckBox enableQuickJump;

    @Nls
    @Override
    public String getDisplayName() {
        return "UsageVisualizer";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "UsageVisualizer";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        config = UsageVisualizationConfig.getInstance();
        if (config == null) {
            config = new UsageVisualizationConfig();
        }
        settingsUi = new JPanel();
        final VerticalBox root = new VerticalBox();
        final HorizontalBox lineTypeHolder = new HorizontalBox();
        lineTypeHolder.add(new JLabel("Line type"));
        lineTypeComboBox = new ComboBox<>(new String[]{UsageVisualizationConfig.VISUALIZATION_LINE, UsageVisualizationConfig.VISUALIZATION_BEZIER_CURVE});
        lineTypeComboBox.setSelectedItem(config.getVisualiztionType());
        lineTypeHolder.add(lineTypeComboBox);
        root.add(lineTypeHolder);

        final HorizontalBox quickJumpHolder = new HorizontalBox();
        quickJumpHolder.add(new JLabel("Enable Quick Jump"));
        enableQuickJump = new JCheckBox();
        enableQuickJump.setSelected(config.isQuickJumpEnabled());
        quickJumpHolder.add(enableQuickJump);
        root.add(quickJumpHolder);

        final HorizontalBox colorHolder = new HorizontalBox();
        colorHolder.add(new JLabel("Line color"));
        colorPicker = new ColorPicker(() -> {
        }, Color.decode("#" + config.getLineColor()), true);
        colorHolder.add(colorPicker);
        root.add(colorHolder);

        settingsUi.add(root);
        return settingsUi;
    }

    @Override
    public void apply() throws ConfigurationException {
        config.setVisualiztionType((String) lineTypeComboBox.getSelectedItem());
        config.setLineColor(Integer.toHexString(colorPicker.getColor().getRGB()).substring(2));
        config.setQuickJumpEnabled(enableQuickJump.isSelected());
    }

    @Override
    public void reset() {
        lineTypeComboBox.setSelectedItem(config.getVisualiztionType());
        Color.decode("#" + config.getLineColor());
        enableQuickJump.setSelected(config.isQuickJumpEnabled());
    }

    @Override
    public boolean isModified() {
        boolean modified = false;
        modified |= !lineTypeComboBox.getSelectedItem().equals(config.getVisualiztionType());
        modified |= !colorPicker.getColor().equals(Color.decode("#" + config.getLineColor()));
        modified |= !enableQuickJump.isSelected() == config.isQuickJumpEnabled();
        return modified;
    }
}
