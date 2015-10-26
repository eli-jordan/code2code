package code2code.ui.wizards.generate;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import code2code.core.generator.Generator;
import code2code.core.generator.Parameter;
import code2code.core.generator.Parameters;
import code2code.ui.utils.EclipseGuiUtils;

/**
 * The wizard page that allows the parameters to be specified
 */
public class GeneratorParametersPage extends WizardPage
{
   private final GenerateFilesWizardModel m_model;
   
   /** the main container for this page */
   private Composite m_container;

   /** the container for the parameters input */
   private Composite m_paramsContainer;

   /** the generator that was select on the GeneratorSelectionPage */
   private Generator m_selectedGenerator;

   /**
    * Constructor
    * @param p_model 
    */
   public GeneratorParametersPage( GenerateFilesWizardModel p_model)
   {
      super("Generator Parameters", "Configure Params", null);
      m_model = p_model;
      setPageComplete(false);
   }

   /**
    * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
    */
   @Override
   public void createControl(Composite p_parent)
   {
      ScrolledComposite scrolledComposite = new ScrolledComposite(p_parent, SWT.H_SCROLL | SWT.V_SCROLL);
      m_container = new Composite(scrolledComposite, SWT.NONE);
      scrolledComposite.setContent(m_container);

      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      m_container.setLayout(layout);

      setControl(scrolledComposite);
   }

   /**
    * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
    */
   @Override
   public void setVisible(boolean visible)
   {
      if (visible)
      {
         try
         {
            recreatePageIfNecessary();
         }
         catch (Exception e)
         {
            EclipseGuiUtils.showErrorDialog(m_container.getShell(), e);
            throw new RuntimeException(e);
         }
      }

      super.setVisible(visible);
   }

   private void recreatePageIfNecessary() throws Exception
   {
      if (!pageIsCreated() || hasGeneratorChanged())
      {

         m_selectedGenerator = m_model.getGenerator();

         if (m_paramsContainer != null)
         {
            m_paramsContainer.dispose();
         }

         createPage();
      }
   }

   private boolean hasGeneratorChanged()
   {
      return m_selectedGenerator != m_model.getGenerator();
   }

   private boolean pageIsCreated()
   {
      return m_paramsContainer != null;
   }

   /**
    * Create the composite that represents this page
    * @throws Exception
    */
   private void createPage() throws Exception
   {
      m_paramsContainer = new Composite(m_container, SWT.NULL);

      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      m_paramsContainer.setLayout(layout);

      Generator selected = m_model.getGenerator();
      Parameters parameters = selected.getParameters();

      setDescription(selected.getDescription());

      if (!parameters.isEmpty())
      {
         addParametersInputFields(parameters);
      }
      else
      {
         Label label = new Label(m_paramsContainer, SWT.NONE);
         label.setText("This Generator has no params to configure");
      }

      m_paramsContainer.pack();
      m_paramsContainer.setVisible(true);

      setPageComplete(true);

      m_container.setSize(m_container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

   }

   /**
    * Adds the UI elements for all the parameters to the generator
    * @param params
    */
   private void addParametersInputFields(Parameters p_parameters)
   {
      for (Parameter parameter : p_parameters)
      {
         Label label = new Label(m_paramsContainer, SWT.NULL);
         label.setText(parameter.name());
         label.setToolTipText(parameter.name());

         GridData data = new GridData();
         data.verticalAlignment = SWT.TOP;
         label.setLayoutData(data);

         addParameterInput(parameter);
      }
   }
   
   /**
    * Adds the UI element that allows the specified parameter to be edited
    * @param p_key
    * @param p_defaultValue
    */
   private void addParameterInput(final Parameter p_parameter)
   {
      final Text text = new Text(m_paramsContainer, SWT.BORDER | SWT.MULTI);
      text.setText(p_parameter.defaultValue().toString());

      text.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            m_model.setParameter(p_parameter.name(), text.getText());
         }
      });

      GridData data2 = new GridData();
      data2.widthHint = 600;
      data2.heightHint = 50;
      text.setLayoutData(data2);
   }
}