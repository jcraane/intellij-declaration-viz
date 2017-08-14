package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import nl.capaxit.idea.plugins.usagevisualizer.configuration.UsageVisualizationConfig;

import java.awt.*;

/**
 * Created by jamiecraane on 10/04/2017.
 */
public final class VisualizationFactory {
    private VisualizationFactory() {
    }

    public static UsageVisualization create(final Editor editor, final int verticalScrollOffset, final Point declarationPoint, final PsiReference reference, final int fixedXOffset) {
        UsageVisualizationConfig config = UsageVisualizationConfig.getInstance();
        if (config == null) {
            config = new UsageVisualizationConfig();
        }

        final PsiElement element = reference.getElement();
        final Point elementPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(element.getTextOffset()));

        if (config.getVisualiztionType().equals(UsageVisualizationConfig.VISUALIZATION_LINE)) {
            return Arrow.create(declarationPoint, new Point(elementPosition.x + fixedXOffset, elementPosition.y - verticalScrollOffset), config);
        } else {
            return new BezierCurve(declarationPoint, new Point((int) elementPosition.getX() + fixedXOffset, elementPosition.y - verticalScrollOffset), config);
        }
    }
}
