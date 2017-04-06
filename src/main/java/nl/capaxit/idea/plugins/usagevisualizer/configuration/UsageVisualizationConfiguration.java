package nl.capaxit.idea.plugins.usagevisualizer.configuration;

import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jamiecraane on 06/04/2017.
 */
public class UsageVisualizationConfiguration extends BaseConfigurable {
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
        final JPanel jPanel = new JPanel();
        jPanel.add(new TextField());
        jPanel.add(new TextField());
        return jPanel;
    }

    @Override
    public void apply() throws ConfigurationException {
//        todo implement
    }
}
