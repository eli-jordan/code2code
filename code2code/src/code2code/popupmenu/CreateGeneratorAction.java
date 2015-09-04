package code2code.popupmenu;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import code2code.ui.wizards.generatorfromfiles.GeneratorFromFilesWizard;

/**
 * Configures a generator based on the selected files 
 */
public class CreateGeneratorAction implements IObjectActionDelegate
{
   /** the current selection */
   private ISelection m_selection;

   /** the target part */
   private IWorkbenchPart m_targetPart;

   /**
    * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
    */
   @Override
   public void setActivePart(IAction action, IWorkbenchPart targetPart)
   {
      this.m_targetPart = targetPart;
   }

   /**
    * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
    */
   @Override
   public void run(IAction action)
   {
      IWorkbenchWindow window = m_targetPart.getSite().getWorkbenchWindow();
      GeneratorFromFilesWizard wizard = new GeneratorFromFilesWizard();
      wizard.init(window.getWorkbench(), (IStructuredSelection) m_selection);
      WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
      dialog.open();
   }

   /**
    * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
    */
   @Override
   public void selectionChanged(IAction action, ISelection selection)
   {
      this.m_selection = selection;
   }
}