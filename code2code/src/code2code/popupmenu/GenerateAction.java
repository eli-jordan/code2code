package code2code.popupmenu;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.eclipse.ui.wizards.IWizardRegistry;

import code2code.utils.EclipseGuiUtils;

/**
 * The UI action that launches the generate wizard
 */
public class GenerateAction implements IObjectActionDelegate
{
   /** the current selection */
   private ISelection m_selection;

   /** the target part */
   private IWorkbenchPart m_targetPart;

   /**
    * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
    */
   @Override
   public void run(IAction p_action)
   {
      IWizardRegistry newWizardRegistry = PlatformUI.getWorkbench().getNewWizardRegistry();
      IWizardDescriptor descriptor = newWizardRegistry.findWizard("code2code.newWizard");

      try
      {
         IWorkbenchWizard wizard = descriptor.createWizard();

         wizard.init(m_targetPart.getSite().getWorkbenchWindow().getWorkbench(), (IStructuredSelection) m_selection);
         WizardDialog dialog = new WizardDialog(m_targetPart.getSite().getShell(), wizard);
         dialog.open();

      }
      catch (CoreException e)
      {
         EclipseGuiUtils.showErrorDialog(m_targetPart.getSite().getShell(), e);
         throw new RuntimeException(e);
      }
   }

   /**
    * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
    */
   @Override
   public void setActivePart(IAction p_action, IWorkbenchPart p_targetPart)
   {
      m_targetPart = p_targetPart;
   }
   
   /**
    * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
    */
   @Override
   public void selectionChanged(IAction p_action, ISelection p_selection)
   {
      m_selection = p_selection;
   }
}