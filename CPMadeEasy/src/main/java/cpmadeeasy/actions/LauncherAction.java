package cpmadeeasy.actions;

import com.cpmadeeasy.views.Launcher;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;

public class LauncherAction extends AnAction
{
	@Override public void actionPerformed(AnActionEvent e)
	{
		try
		{
			PsiDirectory directory = e.getData(LangDataKeys.IDE_VIEW).getOrChooseDirectory();
			Launcher launcher = new Launcher(directory);

			launcher.setVisible(true);
		}
		catch (NullPointerException e1)
		{
			e1.printStackTrace();
		}
	}

	private PsiClass getPsiClassFromContext(AnActionEvent e)
	{
		PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
		Editor editor = e.getData(PlatformDataKeys.EDITOR);

		if (psiFile == null || editor == null)
			return null;

		int offset = editor.getCaretModel().getOffset();
		PsiElement elementAt = psiFile.findElementAt(offset);

		return PsiTreeUtil.getParentOfType(elementAt, PsiClass.class);
	}

}

/*

action code for plugin.xml :

<action id="LauncherAction" class="com.cpmadeeasy.actions.LauncherAction" text="LauncherAction"
		description="Action class to open codeforces contest list window.">
	<add-to-group group-id="GenerateGroup" anchor="last"/>
</action>

*/
