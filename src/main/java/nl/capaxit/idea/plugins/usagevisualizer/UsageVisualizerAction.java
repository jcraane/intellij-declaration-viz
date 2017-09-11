package nl.capaxit.idea.plugins.usagevisualizer;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionManager;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.actionSystem.TypedActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import nl.capaxit.idea.plugins.usagevisualizer.configuration.UsageVisualizationConfig;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.BaseVisualization;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.QuickJumpHandler;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.UsageVisualization;
import nl.capaxit.idea.plugins.usagevisualizer.visualizations.VisualizationFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private TypedActionHandler previousHandler;

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
            UsageVisualizationConfig config = UsageVisualizationConfig.getInstance();
            if (config == null) {
                config = new UsageVisualizationConfig();
            }
            findAndDrawUsagesLines(editor, targetElement, config);
        }
    }

    private void findAndDrawUsagesLines(final Editor editor, final PsiElement declaration, final UsageVisualizationConfig config) {
        final int verticalScrollOffset = editor.getScrollingModel().getVerticalScrollOffset();
        final Point declarationPosition = editor.visualPositionToXY(editor.offsetToVisualPosition(declaration.getTextOffset()));
        final Point declarationPoint = new Point(declarationPosition.x + FIXED_X_OFFSET, declarationPosition.y - verticalScrollOffset);
        final List<PsiReference> references = new ArrayList<>(ReferencesSearch.search(declaration).findAll());

        final java.util.List<UsageVisualization> visualizations = references.stream()
                .map(reference -> VisualizationFactory.create(editor, verticalScrollOffset, declarationPoint, reference, FIXED_X_OFFSET))
                .collect(Collectors.toList());

        if (config.isQuickJumpEnabled()) {
            setupQuickJump(references);
        }

        final AtomicInteger identifierIndex = new AtomicInteger(0);
        SwingUtilities.invokeLater(() -> visualizations
                .forEach(line -> line.draw((Graphics2D) editor.getComponent().getGraphics(), identifierIndex.getAndIncrement())));
    }

    private void setupQuickJump(final List<PsiReference> references) {
        final HashMap<Character, PsiReference> characterPsiReferenceMap = new HashMap<>();
        for (int i = 0; i < references.size(); i++) {
            characterPsiReferenceMap.put(BaseVisualization.getIdentifier(i).charAt(0), references.get(i));
        }

        final EditorActionManager actionManager = EditorActionManager.getInstance();
        final TypedAction typedAction = actionManager.getTypedAction();
        if (previousHandler == null) {
            previousHandler = actionManager.getTypedAction().getHandler();
        }
        typedAction.setupHandler(new QuickJumpHandler(characterPsiReferenceMap, previousHandler));
    }

    @Override
    public void update(final AnActionEvent e) {
        doWork(e);
    }
}
