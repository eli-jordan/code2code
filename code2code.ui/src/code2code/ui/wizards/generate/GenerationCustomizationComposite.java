package code2code.ui.wizards.generate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import code2code.core.generator.Template;
import code2code.ui.utils.EclipseGuiUtils;

/**
 * The UI for the customisation page.
 * 
 * TODO: Eli: does this need to be a subclass of Composite?
 */
public class GenerationCustomizationComposite extends Composite
{
   /** the wizard model */
   private final GenerateFilesWizardModel m_model;

   /**
    * Constructor
    * @param p_model
    * @param p_parent
    * @param p_style
    * @throws Exception
    */
   public GenerationCustomizationComposite(GenerateFilesWizardModel p_model, Composite p_parent, int p_style) throws Exception
   {
      super(p_parent, p_style);
      m_model = p_model;

      this.setLayout(new FillLayout(SWT.VERTICAL | SWT.HORIZONTAL));

      ScrolledComposite scroll = new ScrolledComposite(this, SWT.V_SCROLL | SWT.H_SCROLL);

      scroll.setExpandVertical(true);
      scroll.setExpandHorizontal(true);

      Composite container = new Composite(scroll, SWT.NONE);

      GridLayout layout = new GridLayout();
      layout.numColumns = 3;
      container.setLayout(layout);

      for (Template template : m_model.getGenerator().getTemplates())
      {
         addTemplateUiRow(container, template);
      }

      scroll.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
      scroll.setContent(container);
   }
   
   private void addTemplateUiRow(Composite p_container, Template p_template) throws Exception
   {
      addEnablementCheckbox(p_container, p_template);
      addFileLocationTextBox(p_container, p_template);
      addPreviewButton(p_container, p_template);
   }
   
   /**
    * Adds the checkbox that allows the generation of a template to be enabled/disabled
    * @param p_container
    * @param p_template
    */
   private void addEnablementCheckbox(Composite p_container, final Template p_template)
   {
      final String templateName = p_template.getTemplateName();

      Button checkbox = new Button(p_container, SWT.CHECK);
      checkbox.setSelection(true);
      
      // set the template as selected to be generated using the checkbox
      checkbox.addSelectionListener(new SelectionAdapter()
      {
         @Override
         public void widgetSelected(SelectionEvent event)
         {
            try
            {
               boolean selected = ((Button) event.widget).getSelection();
               if(selected)
               {
                  m_model.selectTemplate(p_template);
               }
               else
               {
                  m_model.unselectTemplate(p_template);
               }
            }
            catch (Exception e)
            {
               EclipseGuiUtils.showErrorDialog(GenerationCustomizationComposite.this.getShell(), e);
               throw new RuntimeException(e);
            }
         }
      });

      checkbox.setText(templateName);
      checkbox.setToolTipText(templateName);
      checkbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
   }
   
   /**
    * Adds the text box that allows the user to select where the file is written
    * @param p_container
    * @param p_template
    * @throws Exception
    */
   private void addFileLocationTextBox(Composite p_container, final Template p_template) throws Exception
   {
      Text text = new Text(p_container, SWT.BORDER);
      text.setText(p_template.getOutputLocation(m_model.getGenerator().getParameters().asMap()));

      text.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            p_template.overrideLocation(((Text) e.widget).getText());
         }
      });
      text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
   }
   
   /**
    * Add the button that allows the templated output to be previewed
    * @param p_container
    * @param p_template
    */
   private void addPreviewButton(Composite p_container, final Template p_template)
   {
      Button previewButton = new Button(p_container, SWT.PUSH);
      previewButton.setText("Preview...");

      previewButton.addSelectionListener(new SelectionAdapter()
      {
         @Override
         public void widgetSelected(SelectionEvent e)
         {

            PreviewDialog previewDialog;
            try
            {
               previewDialog = new PreviewDialog(GenerationCustomizationComposite.this.getShell(), m_model, p_template);
            }
            catch (Exception e1)
            {
               EclipseGuiUtils.showErrorDialog(GenerationCustomizationComposite.this.getShell(), e1);
               throw new RuntimeException(e1);
            }

            previewDialog.setBlockOnOpen(true);

            previewDialog.create();
            previewDialog.open();

         }
      });
   }
}