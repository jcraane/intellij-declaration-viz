package nl.capaxit.idea.plugins.usagevisualizer.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
    public static final int DEFAULT_LINE_WIDTH = 2;
    private static final String DEFAULT_COLOR = "838EFF";

    private String visualiztionType = VISUALIZATION_LINE;
    private String lineColor = DEFAULT_COLOR;
    private boolean quickJumpEnabled = true;
    private int lineWidth = DEFAULT_LINE_WIDTH;

    public UsageVisualizationConfig() {
        visualiztionType = VISUALIZATION_LINE;
        lineColor = DEFAULT_COLOR;
    }

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

    public boolean isQuickJumpEnabled() {
        return quickJumpEnabled;
    }

    public void setQuickJumpEnabled(final boolean quickJumpEnabled) {
        this.quickJumpEnabled = quickJumpEnabled;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(final int lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UsageVisualizationConfig that = (UsageVisualizationConfig) o;
        return Objects.equals(visualiztionType, that.visualiztionType) &&
                Objects.equals(lineColor, that.lineColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(visualiztionType, lineColor);
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
