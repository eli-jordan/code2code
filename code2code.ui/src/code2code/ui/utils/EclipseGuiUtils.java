package code2code.ui.utils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility to perform some common UI tasks
 */
public class EclipseGuiUtils
{

// TODO: Eli: Might use this to open a file once the template has been generated
//   public static void openResource(final IFile resource)
//   {
//      final IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//
//      if (activePage != null)
//      {
//         final Display display = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
//         if (display != null)
//         {
//            display.asyncExec(new Runnable()
//            {
//               public void run()
//               {
//                  try
//                  {
//                     IDE.openEditor(activePage, resource, true);
//                  }
//                  catch (PartInitException e)
//                  {
//                     EclipseGuiUtils.showErrorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), e);
//                     throw new RuntimeException(e);
//                  }
//               }
//            });
//         }
//      }
//   }

   /**
    * Helper to show an error message in the UI.
    * @param shell
    * @param e
    */
   public static void showErrorDialog(Shell shell, Exception e)
   {
      MessageDialog.openError(shell,
         "An Error ocurred",
         "An Error ocurred: " + e.getMessage() +
            "\nSee Error Log view for more details.");

   }

   public static void scaleShellToClientArea(Shell shell, double ratio)
   {
//
//      Rectangle clientArea = Display.getCurrent().getClientArea();
//
//      int relativeWidth = (int) (clientArea.width * ratio);
//      int relativeHeight = (int) (clientArea.height * ratio);
//
//      int hPadding = (clientArea.width - relativeWidth) / 2;
//      int vPadding = (clientArea.height - relativeHeight) / 2;
//
//      Rectangle bounds = new Rectangle(clientArea.x + hPadding, clientArea.y + vPadding, relativeWidth, relativeHeight);
//
//      shell.setBounds(bounds);
   }
}
