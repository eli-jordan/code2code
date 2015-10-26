package code2code.ui.wizards.generate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import code2code.core.generator.Generator;
import code2code.core.generator.GeneratorLocator;
import code2code.core.templateengine.TemplateEngineFactory;

class GeneratorSelectionPage extends WizardPage
{
   /** the wizard model */
   private final GenerateFilesWizardModel m_model;
   
   /** the radio buttons used to select a generator */
   private List<Button> m_generatorRadioButtons = new ArrayList<Button>();

   /** the project used to create the list of generators */
   private final IProject m_project;

   /**
    * Constructor
    * @param p_project
    */
   GeneratorSelectionPage(IProject p_project, GenerateFilesWizardModel p_model)
   {
      super("Generator Selection", "Select a Generator", null);
      m_model = p_model;
      m_project = p_project;
      setPageComplete(false);
   }
   
   /**
    * @see org.eclipse.jface.wizard.WizardPage#isPageComplete()
    */
   @Override
   public boolean isPageComplete()
   {
      return m_model.getGenerator() != null;
   }

   /**
    * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
    */
   @Override
   public void createControl(Composite parent)
   {
      ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
      Composite container = new Composite(scrolledComposite, SWT.NONE);
      scrolledComposite.setContent(container);

      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      container.setLayout(layout);

      Set<Generator> generators = getGenerators();

      if (generators.isEmpty())
      {
         Label label = new Label(container, SWT.NONE);
         label.setText("No generators found.");
      }

      for (Generator generator : generators)
      {
         addRadioButton(container, generator);
      }

      container.setSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

      setControl(scrolledComposite);
   }
   
   private void addRadioButton(Composite p_container, Generator p_generator)
   {
      Button button = new Button(p_container, SWT.RADIO);
      button.setText(p_generator.getDisplayName());
      button.setData("generator", p_generator);

      button.addSelectionListener(new SelectionAdapter()
      {
         @Override
         public void widgetSelected(SelectionEvent e)
         {
            Generator generator = (Generator) ((Button) e.getSource()).getData("generator");
            m_model.setGenerator(generator);
            setPageComplete(true);
         }
      });

      m_generatorRadioButtons.add(button);
   }
   
   private Set<Generator> getGenerators()
   {
      try
      {
         GeneratorLocator locator = new GeneratorLocator(new TemplateEngineFactory());
         Set<Generator> generators = locator.findGenerators(getProjectLocation());
         return generators;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private File getProjectLocation()
   {
      IPath location = m_project.getRawLocation();
      if(location == null)
      {
         location = m_project.getLocation();
      }
      
      return location.toFile();
   }
}