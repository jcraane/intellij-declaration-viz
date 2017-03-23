package nl.capaxit.idea.plugins.usagevisualizer;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

/**
 * todo probably not needed
 * Created by jamiecraane on 22/03/2017.
 */
public class VisualiseDeclarationHandler implements GotoDeclarationHandler {
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable final PsiElement sourceElement, final int offset, final Editor editor) {
        return new PsiElement[0];
    }

    @Nullable
    @Override
    public String getActionText(final DataContext context) {
        return null;
    }
}
