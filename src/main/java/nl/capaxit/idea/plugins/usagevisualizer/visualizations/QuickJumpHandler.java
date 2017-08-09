package nl.capaxit.idea.plugins.usagevisualizer.visualizations;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.ex.IdeDocumentHistory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class QuickJumpHandler implements TypedActionHandler {
    private TypedActionHandler previous;
    private Map<Character, PsiReference> identifierToReferenceMap;

    public QuickJumpHandler(final Map<Character, PsiReference> identifierToReferenceMap, final TypedActionHandler previous) {
        this.identifierToReferenceMap = identifierToReferenceMap != null ? identifierToReferenceMap : new HashMap();
        this.previous = previous;
    }

    @Override
    public void execute(@NotNull final Editor editor, final char charTyped, @NotNull final DataContext dataContext) {
        final PsiReference psiReference = identifierToReferenceMap.get(charTyped);
        if (psiReference != null) {
            final PsiElement element = psiReference.getElement();
            PsiElement navElement = element;
            navElement = TargetElementUtil.getInstance().getGotoDeclarationTarget(element, navElement);
            if (navElement != null) {
                navigateInCurrentEditor(element, element.getContainingFile(), editor);
            }
        }
        resetPreviousHandler();
    }

    private static boolean navigateInCurrentEditor(@NotNull PsiElement element, @NotNull PsiFile currentFile, @NotNull Editor currentEditor) {
        int offset = element.getTextOffset();
        PsiElement leaf = currentFile.findElementAt(offset);
        // check that element is really physically inside the file
        // there are fake elements with custom navigation (e.g. opening URL in browser) that override getContainingFile for various reasons
        if (leaf != null && PsiTreeUtil.isAncestor(element, leaf, false)) {
            Project project = element.getProject();
            CommandProcessor.getInstance().executeCommand(project, () -> {
                IdeDocumentHistory.getInstance(project).includeCurrentCommandAsNavigation();
                new OpenFileDescriptor(project, currentFile.getViewProvider().getVirtualFile(), offset).navigateIn(currentEditor);
            }, "", null);
            return true;
        }

        return false;
    }

    private void resetPreviousHandler() {
        final EditorActionManager actionManager = EditorActionManager.getInstance();
        final TypedAction typedAction = actionManager.getTypedAction();
        typedAction.setupHandler(previous);
    }
}
