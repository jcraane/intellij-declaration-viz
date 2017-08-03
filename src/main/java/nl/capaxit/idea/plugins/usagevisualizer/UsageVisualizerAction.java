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
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.UsageVisualization;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.VisualizationFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
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
                .map(reference -> VisualizationFactory.create(editor, verticalScrollOffset, declarationPoint, reference, FIXED_X_OFFSET))
                .collect(Collectors.toList());

        final AtomicInteger counter = new AtomicInteger(1);
        SwingUtilities.invokeLater(() -> visualizations
                .forEach(line -> {
                    line.draw((Graphics2D) editor.getComponent().getGraphics(), counter.getAndIncrement());
                }));
    }

    @Override
    public void update(final AnActionEvent e) {
        doWork(e);
    }
}
