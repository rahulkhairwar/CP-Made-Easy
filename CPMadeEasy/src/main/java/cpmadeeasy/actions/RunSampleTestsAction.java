package cpmadeeasy.actions;

import com.cpmadeeasy.utils.FileUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.compiler.CompilerManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class RunSampleTestsAction extends AnAction
{
	private static void compileFile(@NotNull final Project project, @NotNull final VirtualFile[] files)
	{
		ApplicationManager.getApplication().invokeLater(new Runnable()
		{
			@Override public void run()
			{
				CompilerManager.getInstance(project).compile(files, null);
			}
		}, project.getDisposed());
	}

	@Override public void actionPerformed(AnActionEvent e)
	{
		VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
		String fileName = (vFile != null ? vFile.getName() : null);

		if (fileName != null)
		{
			int xx = fileName.indexOf("Task");

			if (fileName.length() <= xx + 4)
			{
				System.out.println("File named wrongly! Aborting.");

				return;
			}

			char problem = fileName.charAt(xx + 4);
			Project project = e.getProject();

			if (project == null)
			{
				error();

				return;
			}

			compileFile(project, new VirtualFile[]{vFile});

			VirtualFile parent = vFile.getParent();
			String parentCanonicalPath = parent.getCanonicalPath() + "/";

			boolean isTesterFileCreated = FileUtils.createTesterFile(parentCanonicalPath, problem);

			if (!isTesterFileCreated)
				error();
		}
	}

	private void error()
	{
		System.out.println("Some error occurred. Please try again. Launcher class");
	}

}
