package code2code.ui.wizards.generate;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import code2code.ui.utils.EclipseGuiUtils;

/**
 * The wizard page that represents that allows the templates to use from the generator to be customized, additionally
 * allows the location of the generated files to be set/
 */
public class GenerationCustomizationPage extends WizardPage
{
   private final GenerateFilesWizardModel m_model;
   
   /** the parent */
   private Composite m_parent;
   
   /** the main container */
   private Composite m_container;

   /** the contents of the container */
   private Composite m_contents;

   /**
    * Constructor
    * @param p_model
    */
   public GenerationCustomizationPage(GenerateFilesWizardModel p_model)
   {
      super("Templates Selection", "Customize Generation", null);
      m_model = p_model;
      setPageComplete(true);
   }
   
   @Override
   public void createControl(Composite parent)
   {
      this.m_parent = parent;
      m_container = new Composite(parent, SWT.NONE);
      m_container.setLayout(new FillLayout(SWT.VERTICAL | SWT.HORIZONTAL));

      setControl(m_container);

      EclipseGuiUtils.scaleShellToClientArea(parent.getShell(), 0.8);
   }

   @Override
   public void setVisible(boolean visible)
   {

      if (visible)
      {
         try
         {
            createPage();
         }
         catch (Exception e)
         {
            EclipseGuiUtils.showErrorDialog(m_container.getShell(), e);
            throw new RuntimeException(e);
         }
      }

      super.setVisible(visible);
   }

   private void createPage() throws Exception
   {
      if (m_contents != null)
      {
         m_contents.dispose();
      }
      
      m_contents = new GenerationCustomizationComposite(m_model, m_container, SWT.NULL);

      m_container.pack();
      m_parent.layout();

      m_container.setVisible(true);
   }
}