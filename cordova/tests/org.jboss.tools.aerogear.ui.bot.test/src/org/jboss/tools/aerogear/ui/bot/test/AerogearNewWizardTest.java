package org.jboss.tools.aerogear.ui.bot.test;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;
import static org.junit.Assert.assertEquals;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class AerogearNewWizardTest {

	public static class HMAWBot extends SWTWorkbenchBot {
		public SWTBotView welcome() {
			return DIALOG.viewByTitle("Welcome");
		}

		public void showCordovaWizardFirstStep() {
			menu("File").menu("New").menu("Project...").click();

			SWTBotShell newProjectShell = shell("New Project");
			newProjectShell.activate();
			tree().expandNode("Mobile").select("Hybrid Mobile (Cordova) Application Project");
			button("Next >").click();
		}

		public void fillFirstStepData(String projectName, String appName, String appId) {
			textWithLabel("Project name:").setText(projectName);
			textWithLabel("Name:").setText(appName);
			textWithLabel("ID:").setText(appId);
		}

		public void nextPage() {
			button("Next >").click();
		}

		public void cancel() {
			button("Cancel").click();
		}

		public void cancelModal(SWTBotShell parentShell) {
			button("Cancel").click();
			parentShell.activate();
		}

		public void finish() {
			button("Finish").click();
		}

		public void remove() {
			button("Remove").click();
		}

		public void okModal(SWTBotShell parentShell) {
			button("OK").click();
			parentShell.activate();
		}

		public void ok() {
			button("OK").click();
		}

		public void download(Runnable runable) {
			activateDownload();
			runable.run();
			activateWizard();
		}

		public void activateDownload() {
			button("Download...").click();
			waitUntil(shellIsActive("Download Hybrid Mobile Engine"));
		}

		public void activateWizard() {
			shell("Hybrid Mobile (Cordova) Application Project").activate();
		}

		private String[] availableVersions = new String[] {};

		public int getAvailableVersionsCount() {
			download(new Runnable() {
				@Override
				public void run() {
					availableVersions = version().items();
					cancel();
				}
			});
			return availableVersions.length;
		}

		public String[] getAvailableVersions() {
			getAvailableVersionsCount();
			return availableVersions;
		}

		public SWTBotCombo version() {
			return comboBox(0);
		}

		public void removeAllMobileEngines() {
			while (table(0).rowCount() > 0) {
				removeFirstEngine(table(0));
			}
			activateWizard();
		}

		private void removeFirstEngine(SWTBotTable table) {
			final int count = table(0).rowCount();
			table(0).getTableItem(0).select();
			remove();
			SWTBotShell shell = activateConfirmDelete();
			ok();
			// waitWhile(shellCloses(shell));
			activateWizard();
			// sleep(3000);
			waitWhile(new DefaultCondition() {
				@Override
				public boolean test() throws Exception {
					// TODO Auto-generated method stub
					return table(0).selectionCount() == 1;
				}

				@Override
				public String getFailureMessage() {
					// TODO Auto-generated method stub
					return null;
				}
			});
		}

		public SWTBotShell activateConfirmDelete() {
			waitUntil(shellIsActive("Confirm Delete"));
			SWTBotShell shell = shell("Confirm Delete");
			shell.activate();
			return shell;
		}

		public void addAllMobileEngines() {
			String[] versions = getAvailableVersions();
			for (String string : versions) {
				activateDownload();
				version().setSelection(string);
				waitWhile(new DefaultCondition() {

					@Override
					public boolean test() throws Exception {
						return table(0).rowCount() == 0;
					}

					@Override
					public String getFailureMessage() {
						return "No supported platforms for selected version";
					}
				});
				if (table(0).rowCount() > 0) {
					table(0).getTableItem(0).check();
					ok();
					waitUntil(shellCloses(shell("Download Hybrid Mobile Engine")));
				} else {
					cancel();
				}

				activateWizard();
			}
		}

		public void addFirstMobileEngine() {
			activateDownload();
			version().setSelection(0);
			waitWhile(new DefaultCondition() {

				@Override
				public boolean test() throws Exception {
					return table(0).rowCount() == 0;
				}

				@Override
				public String getFailureMessage() {
					return "No supported platforms for selected version";
				}
			});
			if (table(0).rowCount() > 0) {
				table(0).getTableItem(0).check();
				ok();
				waitUntil(shellCloses(shell("Download Hybrid Mobile Engine")));
			} else {
				cancel();
			}

			activateWizard();
		}
	}

	private static final HMAWBot DIALOG = new HMAWBot();

	@BeforeClass
	public static void beforeClass() throws Exception {
		DIALOG.welcome().close();
	}

	/**
	 * Method supports screencast recording and testing. I case of screencast it posts delays between operations to
	 * record it.
	 * 
	 * @throws Exception
	 *             - in case of error
	 */
	@Test
	public void canCreateANewJavaProject() throws Exception {
		while (true) {
			DIALOG.showCordovaWizardFirstStep();

			DIALOG.fillFirstStepData("p1", "p1", "com.app.p1");
			DIALOG.nextPage();
			assertEquals(7, DIALOG.getAvailableVersionsCount());

			DIALOG.removeAllMobileEngines();
			DIALOG.addAllMobileEngines();
			DIALOG.removeAllMobileEngines();
			DIALOG.addFirstMobileEngine();
			DIALOG.table(0).getTableItem(0).check();
			DIALOG.finish();
			DIALOG.views().get(0).getWidget().getDisplay().syncExec(new Runnable() {
				@Override
				public void run() {
					System.out.println("Sync exec after project is created");
				}
			});

			ResourcesPlugin.getWorkspace().getRoot().getProject("p1").delete(true, true, null);
			DIALOG.waitWhile(new DefaultCondition() {

				@Override
				public boolean test() throws Exception {
					return ResourcesPlugin.getWorkspace().getRoot().getProject("p1").isAccessible();
				}

				@Override
				public String getFailureMessage() {
					// TODO Auto-generated method stub
					return null;
				}
			});
		}
		// bot.tableWithLabel("", 0).getTableItem(0).check();
		// bot.button("OK").click();
		// FIXME: assert that the project is actually created, for later
	}

	@AfterClass
	public static void sleep() {
		DIALOG.sleep(2000);
	}

}
