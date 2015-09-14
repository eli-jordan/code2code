//package code2code.core.generator;
//
//import java.util.Comparator;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.TreeSet;
//
//import org.eclipse.core.resources.IFolder;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IResource;
//import org.eclipse.core.resources.IResourceVisitor;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.ui.PlatformUI;
//
//import code2code.core.templateengine.TemplateEngineFactory;
//import code2code.utils.EclipseGuiUtils;
//
///**
// * Creates the generator type from files in the workspace
// */
//public class GeneratorFactory
//{
//   /** the folder name within a project that houses generators */
//   private static final String GENERATORS_FOLDER_NAME = "generators";
//   
//   /**
//    * The generator comparator
//    */
//   private static class GeneratorNameComparator implements Comparator<Generator>
//   {
//      public int compare(Generator p_gen1, Generator p_gen2)
//      {
//         return p_gen1.getName().compareTo(p_gen2.getName());
//      }
//   }
//   
//   /**
//    * Visits the resources in a folder and finds any generators
//    */
//   private static class TemplateIdentificationVisitor implements IResourceVisitor
//   {
//      /** the generators that have been found by the visitation */
//      private Set<Generator> m_generators = new TreeSet<Generator>(new GeneratorNameComparator());
//
//      /**
//       * @see org.eclipse.core.resources.IResourceVisitor#visit(org.eclipse.core.resources.IResource)
//       */
//      public boolean visit(IResource p_resource) throws CoreException
//      {
//         if (p_resource.getType() == IResource.FOLDER)
//         {
//            IFolder folder = (IFolder) p_resource;
//
//            if (isGeneratorFolder(folder))
//            {
//               try
//               {
//                  m_generators.add(OldGenerator.fromFolder(folder));
//               }
//               catch (Exception e)
//               {
//                  EclipseGuiUtils.showErrorDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), e);
//                  throw new RuntimeException(e);
//               }
//            }
//         }
//         return true;
//      }
//      
//      /**
//       * @return
//       *    the generators that were found
//       */
//      Set<Generator> getGenerators()
//      {
//         return m_generators;
//      }
//      
//      private boolean isGeneratorFolder(IFolder p_folder)
//      {
//         return p_folder.getName().matches(".+\\.generator$") && 
//                new TemplateEngineFactory().findKnownTemplate(p_folder, "templates") != null;
//      }
//   }
//   
//   /**
//    * Find all the generators defined in a project
//    * @param project
//    * @return
//    *   the identified generators 
//    * @throws Exception
//    */
//   public static Set<OldGenerator> fromProject(IProject project) throws Exception
//   {
//      IFolder defaultGeneratorsFolder = project.getFolder(GENERATORS_FOLDER_NAME);
//
//      // early exit if there is no 'generators' folder'
//      if (!defaultGeneratorsFolder.exists())
//      {
//         return new HashSet<OldGenerator>();
//      }
//      
//      TemplateIdentificationVisitor visitor = new TemplateIdentificationVisitor();
//      defaultGeneratorsFolder.accept(visitor);
//
//      return visitor.getGenerators();
//   }
//
//   /**
//    * Determine if the named generator exists in the specified project.
//    * 
//    * @param project
//    * @param name
//    * @return
//    *   true if it exists, false otherwise
//    */
//   public static boolean exists(IProject project, String name)
//   {
//      IFolder defaultGeneratorsFolder = project.getFolder(new Path("generators"));
//      return defaultGeneratorsFolder.exists(new Path(name + ".generator"));
//   }
//}