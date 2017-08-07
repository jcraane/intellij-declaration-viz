package nl.capaxit.idea.plugins.usagevisualizer.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * Created by jamiecraane on 11/05/2017.
 */
@State(
        name = "UsageVisualizationConfiguration",
        storages = {
                @Storage("UsageVisualizationConfiguration.xml")
        }
)
public class UsageVisualizationConfig implements PersistentStateComponent<UsageVisualizationConfig> {
    public static final String VISUALIZATION_LINE = "straight_line";
    public static final String VISUALIZATION_BEZIER_CURVE = "bezier_curve";
    public static final String DEFAULT_COLOR = "838EFF";

    private String visualiztionType = VISUALIZATION_LINE;
    private String lineColor = DEFAULT_COLOR;

    @Nullable
    @Override
    public UsageVisualizationConfig getState() {
        return this;
    }

    @Override
    public void loadState(final UsageVisualizationConfig state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Nullable
    public static UsageVisualizationConfig getInstance() {
        return ServiceManager.getService(UsageVisualizationConfig.class);
    }

    public String getVisualiztionType() {
        return visualiztionType;
    }

    public void setVisualiztionType(final String visualiztionType) {
        this.visualiztionType = visualiztionType;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(final String lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UsageVisualizationConfig{");
        sb.append("visualiztionType='").append(visualiztionType).append('\'');
        sb.append(", lineColor='").append(lineColor).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
