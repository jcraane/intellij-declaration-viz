package nl.capaxit.idea.plugins.usagevisualizer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Simple action for testing various Idea plugin parts. Action should be invoked when hovering over
 * an element in the editor, perhaps by holding the control button.
 *
 * Created by jamiecraane on 25/10/2016.
 */
public class UsageVisualizerAction extends AnAction {
    @Override
    public void actionPerformed(final AnActionEvent e) {
        final Project project = e.getData(CommonDataKeys.PROJECT);
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final CaretModel caretModel = editor.getCaretModel();
        final Caret currentCaret = caretModel.getCurrentCaret();
        final Document document = editor.getDocument();
        final Point point = editor.logicalPositionToXY(currentCaret.getLogicalPosition());
        final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);

        final PsiElement elementAtCaret = psiFile.findElementAt(caretModel.getOffset());

        final PsiReferenceExpression parent = PsiTreeUtil.getParentOfType(elementAtCaret, PsiReferenceExpression.class);
        if (parent != null) {
            final java.util.List<LineSpec> lines = new ArrayList<>(5);

            final int verticalScrollOffset = editor.getScrollingModel().getVerticalScrollOffset();
            final PsiElement declaration = parent.resolve();

            final int elementXOffset = 100;
            final int caretXOffset = 75;
            final int yOffset = 15;
            final Point caretPosition = new Point(point.x + caretXOffset, point.y + yOffset);
            final Point declarationPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(declaration.getTextOffset()));
            final Point declarationPoint = new Point(declarationPosition.x + elementXOffset, declarationPosition.y - verticalScrollOffset);
            final Collection<PsiReference> references = ReferencesSearch.search(declaration).findAll();
            references.forEach(reference -> {
                final PsiElement element = reference.getElement();
                final Point elementPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(element.getTextOffset()));
                lines.add(LineSpec.create(declarationPoint, new Point(elementPosition.x + elementXOffset, elementPosition.y - verticalScrollOffset)));
            });

            final Graphics graphics = editor.getComponent().getGraphics();
            graphics.setColor(new Color(131, 142, 255, 128));
            ((Graphics2D) graphics).setStroke(new BasicStroke(2));
            final int logicalPositionToOffset = editor.logicalPositionToOffset(((EditorImpl) editor).offsetToLogicalPosition(declaration.getTextOffset()));
            // todo there must be a better way to determine these offsets.

//            lines.add(LineSpec.create(caretPosition, declarationPoint));

            drawLines(lines, graphics);
        }
    }

    private void drawLines(final List<LineSpec> lines, final Graphics graphics) {
        lines.forEach(line -> graphics.drawLine(line.getStart().x, line.getStart().y, line.getEnd().x, line.getEnd().y));
    }
}
