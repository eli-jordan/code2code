package code2code.ui.wizards.generate;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import code2code.core.generator.Generator;
import code2code.core.generator.Template;
import code2code.utils.Console;
import code2code.utils.EclipseGuiUtils;
import code2code.utils.FileUtils;

/**
 * The wizard to generate templated files
 */
public class GenerateFilesWizard extends Wizard implements INewWizard
{
   /** the current selection */
   private IStructuredSelection m_selection;

   /** the page used to select the generator to use */
   private GeneratorSelectionPage m_generatorSelectionPage;

   /** the page to fill in the generator parameters */
   private GeneratorParametersPage m_generatorParametersPage;

   /** the file location customisation page */
   private GenerationCustomizationPage m_generationCustomizationPage;
   
   /** used to display error messages */
   private IWorkbench m_workbench;
   
   /**
    * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
    */
   @Override
   public void init(IWorkbench p_workbench, IStructuredSelection p_selection)
   {
      m_workbench = p_workbench;
      m_selection = p_selection;
   }

   /**
    * @see org.eclipse.jface.wizard.Wizard#performFinish()
    */
   @Override
   public boolean performFinish()
   {
      Generator selectedGenerator = m_generatorSelectionPage.getSelectedGenerator();

      if (selectedGenerator == null)
      {
         return false;
      }

      try
      {
         Console.write("Processing generator: " + selectedGenerator.getName());

         IProject project = selectedGenerator.getGeneratorFolder().getProject();

         for (Template template : selectedGenerator.getTemplatesToInstantiate())
         {
            String destination = template.getOutputLocation();

            if (destination.equals(""))
            {
               Console.write("Generating " + template.getTemplateName() + " to console:");
               Console.write("-------------------------------------------------------------");
               Console.write(FileUtils.toString(template.instantiate()));
               Console.write("-------------------------------------------------------------");
            }
            else
            {
               Path destinationPath = new Path(destination);
               if (project.exists(destinationPath))
               {
                  Console.write("File already exists. Skipping: " + destinationPath);
                  continue;
               }

               Console.write("Generating: " + template.getTemplateName() + " to " + destination);

               IFile file = project.getFile(destinationPath);

               FileUtils.createParentFolders(file);

               file.create(template.instantiate(), false, null);

            }

         }

         Console.write("Done");

      }
      catch (Exception e)
      {
         try
         {
            Console.write("An error ocurred. See Error Log for details");
         }
         catch (Exception e2)
         {
            EclipseGuiUtils.showErrorDialog(m_workbench.getActiveWorkbenchWindow().getShell(), e2);
            throw new RuntimeException(e2);
         }
         EclipseGuiUtils.showErrorDialog(m_workbench.getActiveWorkbenchWindow().getShell(), e);
         throw new RuntimeException(e);
      }

      return true;
   }
   
   private Map<String, String> definePresetParameters()
   {
      Map<String, String> presets = new HashMap<String, String>();
      
      IResource selectedResource = (IResource) ((IAdaptable) m_selection.getFirstElement()).getAdapter(IResource.class);
      String projectPath = selectedResource.getProjectRelativePath().toString();
      presets.put("selection_relative_path", projectPath);
      
      return presets;
   }

   /**
    * @see org.eclipse.jface.wizard.Wizard#addPages()
    */
   @Override
   public void addPages()
   {
      if (m_selection.size() == 0)
      {
         MessageDialog.openError(m_workbench.getActiveWorkbenchWindow().getShell(), "Oops", "Oops... No selection");
         throw new IllegalStateException("A resource must be selected to scope the available generators");
      }
      
      IProject project = ((IResource) ((IAdaptable) m_selection.getFirstElement()).getAdapter(IResource.class)).getProject();

      m_generatorSelectionPage = new GeneratorSelectionPage(project, definePresetParameters());
      m_generatorParametersPage = new GeneratorParametersPage(m_generatorSelectionPage);
      m_generationCustomizationPage = new GenerationCustomizationPage(m_generatorParametersPage);

      addPage(m_generatorSelectionPage);
      addPage(m_generatorParametersPage);
      addPage(m_generationCustomizationPage);
   }
}