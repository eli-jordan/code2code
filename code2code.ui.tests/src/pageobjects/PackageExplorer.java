package pageobjects;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.ChildrenControlFinder;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.hamcrest.Matcher;

import utils.Driver;

public class PackageExplorer
{

   public static void rightClick(IProject project, String menuItem) throws Exception
   {
      SWTBotTree swtBotTree = selectProject(project);
      SWTBotMenu generateMenuItem = swtBotTree.contextMenu(menuItem);
      generateMenuItem.click();
   }

   public static SWTBotTree selectProject(IProject project) throws WidgetNotFoundException
   {
      SWTBotView packageExplorerView = Driver.bot().viewByTitle("Project Explorer");

      Widget widget = packageExplorerView.getWidget();
      Matcher<Tree> widgetOfType = WidgetMatcherFactory.widgetOfType(Tree.class);
      
      Tree projectsTree = new ChildrenControlFinder(widget).findControls(widgetOfType).get(0);
      SWTBotTree swtBotTree = new SWTBotTree(projectsTree);
      swtBotTree.select(project.getName());
      swtBotTree.setFocus();
      return swtBotTree;
   }
}