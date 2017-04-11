package nl.capaxit.idea.plugins.usagevisualizer;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.BezierCurve;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.UsageVisualization;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Simple action for testing various Idea plugin parts. Action should be invoked when hovering over
 * an element in the editor, perhaps by holding the control button.
 * <p>
 * Created by jamiecraane on 25/10/2016.
 */
public class UsageVisualizerAction extends AnAction {
    // todo there must be a better way to determine this offset.
    public static final int FIXED_X_OFFSET = 75;

    @Override
    public void actionPerformed(final AnActionEvent e) {
        doWork(e);
    }

    private void doWork(final AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (project == null || editor == null) {
            return;
        }

//        final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
//        final PsiElement elementAtCaret = psiFile.findElementAt(editor.getCaretModel().getOffset());

        final PsiElement targetElement = TargetElementUtil.findTargetElement(editor, TargetElementUtil.getInstance().getReferenceSearchFlags());
        if (targetElement != null) {
            findAndDrawUsagesLines(editor, targetElement);
        }
    }

    private void findAndDrawUsagesLines(final Editor editor, final PsiElement declaration) {

//        final PsiElement startElement = elementAtCaret != null ? elementAtCaret : declaration;

        final int verticalScrollOffset = editor.getScrollingModel().getVerticalScrollOffset();
//        final int elementXOffset = FIXED_X_OFFSET;

        final Point declarationPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(declaration.getTextOffset()));
//        final Point declarationPoint = new Point(declarationPosition.x + elementXOffset, declarationPosition.y - verticalScrollOffset);
        final Point declarationPoint = new Point(declarationPosition.x + FIXED_X_OFFSET, declarationPosition.y - verticalScrollOffset);
        final Collection<PsiReference> references = ReferencesSearch.search(declaration).findAll();

        final java.util.List<UsageVisualization> visualizations = references.stream()
                .map(reference -> createUsageLineSpec(editor, verticalScrollOffset, declarationPoint, reference))
                .collect(Collectors.toList());

        SwingUtilities.invokeLater(() -> visualizations
                .forEach(line -> line.draw((Graphics2D) editor.getComponent().getGraphics())));
    }

    @NotNull
    private UsageVisualization createUsageLineSpec(final Editor editor, final int verticalScrollOffset, final Point declarationPoint, final PsiReference reference) {
        final PsiElement element = reference.getElement();
        final Point elementPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(element.getTextOffset()));

        //        todo move this creation to a factory class.
        // Arrow
//        return Arrow.create(declarationPoint, new Point(elementPosition.x + elementXOffset, elementPosition.y - verticalScrollOffset));
//        return Arrow.create(declarationPoint, new Point(elementPosition.x + FIXED_X_OFFSET, elementPosition.y - verticalScrollOffset));

//        Bezier curve
//        final int lineEndOffset = DocumentUtil.getLineEndOffset(element.getTextOffset(), editor.getDocument());
//        final int lineStartOffset = lineEndOffset - DocumentUtil.getLineStartOffset(element.getTextOffset(), editor.getDocument());
//        final Point2D point2D = editor.visualPositionToXY(new VisualPosition(1, lineStartOffset));
        return new BezierCurve(declarationPoint, new Point((int) elementPosition.getX() + FIXED_X_OFFSET, elementPosition.y - verticalScrollOffset));
    }

    @Override
    public void update(final AnActionEvent e) {
        doWork(e);
    }
}
