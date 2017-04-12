package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

import java.awt.*;

/**
 * Created by jamiecraane on 10/04/2017.
 */
public final class VisualizationFactory {
//    todo factore should determine based on settings which visualization to create.
    public static UsageVisualization create(final Editor editor, final int verticalScrollOffset, final Point declarationPoint, final PsiReference reference, final int fixedXOffset) {
        final PsiElement element = reference.getElement();
        final Point elementPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(element.getTextOffset()));

        // Arrow
//        return Arrow.create(declarationPoint, new Point(elementPosition.x + elementXOffset, elementPosition.y - verticalScrollOffset));
//        return Arrow.create(declarationPoint, new Point(elementPosition.x + FIXED_X_OFFSET, elementPosition.y - verticalScrollOffset));

//        Bezier curve
//        final int lineEndOffset = DocumentUtil.getLineEndOffset(element.getTextOffset(), editor.getDocument());
//        final int lineStartOffset = lineEndOffset - DocumentUtil.getLineStartOffset(element.getTextOffset(), editor.getDocument());
//        final Point2D point2D = editor.visualPositionToXY(new VisualPosition(1, lineStartOffset));
        return new BezierCurve(declarationPoint, new Point((int) elementPosition.getX() + fixedXOffset, elementPosition.y - verticalScrollOffset));
    }
}
