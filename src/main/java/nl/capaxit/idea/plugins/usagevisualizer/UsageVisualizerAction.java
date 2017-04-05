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
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Simple action for testing various Idea plugin parts. Action should be invoked when hovering over
 * an element in the editor, perhaps by holding the control button.
 * <p>
 * Created by jamiecraane on 25/10/2016.
 */
public class UsageVisualizerAction extends AnAction {
    // todo there must be a better way to determine this offset.
    public static final int FIXED_X_OFFSET = 100;

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

        final PsiElement targetElement = TargetElementUtil.findTargetElement(editor, TargetElementUtil.getInstance().getReferenceSearchFlags());
        if (targetElement != null) {
            findAndDrawUsagesLines(editor, targetElement);
        }
    }

    private void findAndDrawUsagesLines(final Editor editor, final PsiElement declaration) {
        final int verticalScrollOffset = editor.getScrollingModel().getVerticalScrollOffset();
        final int elementXOffset = FIXED_X_OFFSET;

        final Point declarationPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(declaration.getTextOffset()));
        final Point declarationPoint = new Point(declarationPosition.x + elementXOffset, declarationPosition.y - verticalScrollOffset);
        final Collection<PsiReference> references = ReferencesSearch.search(declaration).findAll();
        SwingUtilities.invokeLater(() -> references.stream()
                .map(reference -> createUsageLineSpec(editor, verticalScrollOffset, elementXOffset, declarationPoint, reference))
                .forEach(line -> line.draw((Graphics2D) editor.getComponent().getGraphics())));
    }

    @NotNull
    private Arrow createUsageLineSpec(final Editor editor, final int verticalScrollOffset, final int elementXOffset, final Point declarationPoint, final PsiReference reference) {
        final PsiElement element = reference.getElement();
        final Point elementPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(element.getTextOffset()));
        return Arrow.create(declarationPoint, new Point(elementPosition.x + elementXOffset, elementPosition.y - verticalScrollOffset));
    }

    @Override
    public void update(final AnActionEvent e) {
        doWork(e);
    }
}
