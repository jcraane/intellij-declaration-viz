package nl.capaxit.idea.plugins.usagevisualizer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * todo probably not needed.
 * Created by jamiecraane on 22/03/2017.
 */
public class VizReferencePovider extends PsiReferenceProvider {
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull final PsiElement element, @NotNull final ProcessingContext context) {
        return new PsiReference[0];
    }
}
