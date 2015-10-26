package code2code.ui.wizards.generate;

import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import code2code.core.generator.Template;
import code2code.ui.utils.EclipseGuiUtils;

/**
 * Displays the instantiated template before writing the files to disk
 */
class PreviewDialog extends TrayDialog
{
   /** the wizard model */
   private final GenerateFilesWizardModel m_model;
   
   /** the template to preview */
   private final Template m_template;
   
   /** the ui element that displays the text */
   private StyledText m_previewText;
   
   PreviewDialog(Shell shell, GenerateFilesWizardModel p_model, Template result)
   {
      super(shell);
      m_model = p_model;
      m_template = result;
      setShellStyle(getShellStyle() | SWT.RESIZE);
   }
   
   /**
    * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
    */
   @Override
   protected void configureShell(Shell newShell)
   {
      super.configureShell(newShell);

      EclipseGuiUtils.scaleShellToClientArea(newShell, 0.7);
   }

   /**
    * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
    */
   @Override
   protected Control createDialogArea(Composite parent)
   {
      Composite dialogArea = (Composite) super.createDialogArea(parent);

      FillLayout fillLayout = new FillLayout();
      dialogArea.setLayout(fillLayout);

      m_previewText = new StyledText(dialogArea, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

      try
      {
         String instance = m_template.instantiate(m_model.getParameters());
         m_previewText.setText(instance);
      }
      catch (Exception e)
      {
         EclipseGuiUtils.showErrorDialog(getShell(), e);
         throw new RuntimeException(e);
      }

      return dialogArea;
   }

   /**
    * @see org.eclipse.jface.dialogs.Dialog#okPressed()
    */
   @Override
   protected void okPressed()
   {
      //m_template.cacheOutput(m_previewText.getText());

      //TODO: allow the user to edit the text
      super.okPressed();
   }
}