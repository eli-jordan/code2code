package code2code.ui.wizards.generate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import code2code.core.generator.OldGenerator;
import code2code.utils.EclipseGuiUtils;

/**
 * The wizard page that allows the parameters to be specified
 */
public class GeneratorParametersPage extends WizardPage
{
   /** the main container for this page */
   private Composite m_container;

   /** the container for the parameters input */
   private Composite m_paramsContainer;

   /** the UI elements that allow the user to specify parameter values */
   private List<Text> m_parameterInput;

   /** the wizard page that allows the generator to be selected */
   private final GeneratorSelectionPage m_generatorSelectionPage;

   /** the generator that was select on the GeneratorSelectionPage */
   private OldGenerator m_selectedGenerator;

   /**
    * Constructor
    * @param generatorSelectionPage
    */
   public GeneratorParametersPage(GeneratorSelectionPage generatorSelectionPage)
   {
      super("Generator Parameters", "Configure Params", null);
      this.m_generatorSelectionPage = generatorSelectionPage;
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

         m_selectedGenerator = m_generatorSelectionPage.getSelectedGenerator();

         if (m_paramsContainer != null)
         {
            m_paramsContainer.dispose();
         }

         createPage();
      }
   }

   private boolean hasGeneratorChanged()
   {
      return m_selectedGenerator != m_generatorSelectionPage.getSelectedGenerator();
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
      m_parameterInput = new ArrayList<Text>();

      m_paramsContainer = new Composite(m_container, SWT.NULL);

      GridLayout layout = new GridLayout();
      layout.numColumns = 2;
      m_paramsContainer.setLayout(layout);

      Map<String, String> params = m_generatorSelectionPage.getSelectedGenerator().getRequiredParameters();

      setDescription(m_generatorSelectionPage.getSelectedGenerator().getDescription());

      if (params.size() > 0)
      {
         addParametersInputFields(params);
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
   private void addParametersInputFields(Map<String, String> params)
   {
      for (Entry<String, String> entry : params.entrySet())
      {
         String paramName = entry.getKey();

         Label label = new Label(m_paramsContainer, SWT.NULL);
         label.setText(paramName);

         GridData data = new GridData();
         data.verticalAlignment = SWT.TOP;
         label.setLayoutData(data);

         addParameterInput(entry.getKey(), entry.getValue());
      }
   }
   
   /**
    * Adds the UI element that allows the specified parameter to be edited
    * @param p_key
    * @param p_defaultValue
    */
   private void addParameterInput(String p_key, String p_defaultValue)
   {
      Text text = new Text(m_paramsContainer, SWT.BORDER | SWT.MULTI);

      text.setData("paramName", p_key);
      text.setText(p_defaultValue);

      text.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            try
            {
               getSelectedGenerator().setUserConfiguredParams(createParamsMap());
            }
            catch (Exception e1)
            {
               EclipseGuiUtils.showErrorDialog(m_container.getShell(), e1);
               throw new RuntimeException(e1);
            }
         }
      });

      m_parameterInput.add(text);

      GridData data2 = new GridData();
      data2.widthHint = 600;
      data2.heightHint = 50;
      text.setLayoutData(data2);
   }

   private Map<String, String> createParamsMap()
   {
      Map<String, String> paramsMap = new LinkedHashMap<String, String>();

      for (Text text : m_parameterInput)
      {
         paramsMap.put((String) text.getData("paramName"), text.getText());
      }

      return paramsMap;
   }

   /**
    * @return
    *   the selected generator
    */
   public OldGenerator getSelectedGenerator()
   {
      return m_generatorSelectionPage.getSelectedGenerator();
   }
}