package code2code.ui.wizards.generate;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import code2code.core.generator.Generator;
import code2code.ui.utils.Console;
import code2code.ui.utils.EclipseGuiUtils;

/**
 * The wizard to generate templated files
 */
public class GenerateFilesWizard extends Wizard implements INewWizard
{
   private GenerateFilesWizardModel m_model = new GenerateFilesWizardModel();
   
   /** the current selection */
   private IStructuredSelection m_selection;
   
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
      Generator selectedGenerator = m_model.getGenerator();

      if (selectedGenerator == null)
      {
         return false;
      }

      try
      {
         Console.write("Processing generator: " + selectedGenerator.getName());
         
         selectedGenerator.instantiteTemplates(m_model.getSelectedTemplates()).withContext(m_model.getParameters());

         //IProject project = selectedGenerator.getGeneratorFolder().getProject();
//
//         for (Template template : selectedGenerator.getTemplates())
//         {
//            if(!template.isSelected())
//            {
//               continue;
//            }
//            
//            Map<String, Object> context = selectedGenerator.getParameters().asMap();
//            String destination = template.getOutputLocation(context);
//
//            if (destination.equals(""))
//            {
//               Console.write("Generating " + template.getTemplateName() + " to console:");
//               Console.write("-------------------------------------------------------------");
//               Console.write(FileUtils.toString(template.instantiate()));
//               Console.write("-------------------------------------------------------------");
//            }
//            else
//            {
//               Path destinationPath = new Path(destination);
//               if (project.exists(destinationPath))
//               {
//                  Console.write("File already exists. Skipping: " + destinationPath);
//                  continue;
//               }
//
//               Console.write("Generating: " + template.getTemplateName() + " to " + destination);
//
//               IFile file = project.getFile(destinationPath);
//
//               FileUtils.createParentFolders(file);
//
//               file.create(template.instantiate(), false, null);
//
//            }
//
//         }

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
   
   /**
    * Defines the values for any preset variables that can be used for substitution
    * in templates.
    * 
    * TODO: make this more flexible, so that more variables can easily be defined
    */
   private void definePresetParameters()
   {
      @SuppressWarnings("cast")
      IResource selectedResource = (IResource) ((IAdaptable) m_selection.getFirstElement()).getAdapter(IResource.class);
      String projectPath = selectedResource.getProjectRelativePath().toString();
      m_model.setParameter("selection_relative_path", projectPath);
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
      
      definePresetParameters();
      
      @SuppressWarnings("cast")
      IProject project = (IProject) ((IResource)((IAdaptable) m_selection.getFirstElement()).getAdapter(IResource.class)).getProject();

      addPage(new GeneratorSelectionPage(project, m_model));
      addPage(new GeneratorParametersPage(m_model));
      addPage(new GenerationCustomizationPage(m_model));
   }
}