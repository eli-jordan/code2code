package pageobjects;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.intro.IIntroManager;

public class Workbench
{
   public static void closeWelcomeView() throws Exception
   {
      Display.getDefault().syncExec(new Runnable()
      {
         public void run()
         {
            IIntroManager introManager = PlatformUI.getWorkbench().getIntroManager();
            if (introManager.getIntro() != null)
            {
               introManager.closeIntro(introManager.getIntro());
            }
         }
      });
   }

}
