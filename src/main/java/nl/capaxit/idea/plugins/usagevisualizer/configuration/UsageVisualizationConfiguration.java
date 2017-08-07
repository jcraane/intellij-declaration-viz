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
        settingsUi = new JPanel();
        final VerticalBox root = new VerticalBox();
        final HorizontalBox lineTypeHolder = new HorizontalBox();
        lineTypeHolder.add(new JLabel("Line type"));
        lineTypeHolder.add(new ComboBox<>(new String[]{UsageVisualizationConfig.VISUALIZATION_LINE, UsageVisualizationConfig.VISUALIZATION_BEZIER_CURVE}));
        root.add(lineTypeHolder);

        final HorizontalBox colorHolder = new HorizontalBox();
        colorHolder.add(new JLabel("Line color"));
        colorHolder.add(new ColorPicker(() -> {
        }, new Color(100, 100, 100), true));
        root.add(colorHolder);

        settingsUi.add(root);
        return settingsUi;
    }

    @Override
    public void apply() throws ConfigurationException {
//        todo implement
    }

    @Override
    public void reset() {
//        todo implement
    }

    @Override
    public boolean isModified() {
//        todo implement
        return true;
    }
}
