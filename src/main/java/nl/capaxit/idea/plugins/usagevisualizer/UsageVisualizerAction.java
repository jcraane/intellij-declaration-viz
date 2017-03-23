package nl.capaxit.idea.plugins.usagevisualizer;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceExpression;
import com.intellij.psi.util.PsiTreeUtil;

import java.awt.*;

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
            final int verticalScrollOffset = editor.getScrollingModel().getVerticalScrollOffset();
            final PsiElement declaration = parent.resolve();
            final Point declarationPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(declaration.getTextOffset()));
            final Graphics graphics = editor.getComponent().getGraphics();
            graphics.setColor(new Color(131, 142, 255, 128));
            ((Graphics2D) graphics).setStroke(new BasicStroke(3));
            final int xOffset = 25;
            final int elementXOffSet = 50; // todo there must be a better way to determine this offset.
            final int yOffset = 0;
            graphics.drawLine(point.x + xOffset, point.y + yOffset - verticalScrollOffset, declarationPosition.x + elementXOffSet, declarationPosition.y - verticalScrollOffset);
        }
    }
}
