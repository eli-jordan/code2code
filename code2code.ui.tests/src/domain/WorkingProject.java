package domain;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;

import code2code.core.utils.FileUtils;

public class WorkingProject
{

   private static IProject project;

   public static IProject create() throws Exception
   {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      String projectName = "code2code.TestProject";
      project = root.getProject(projectName);
      int index = 0;
      while (project.exists())
      {
         project = root.getProject(projectName + "_" + (++index));
      }
      project.create(null);
      project.open(null);
      return project;
   }

   public static void copyGenerator(String generator) throws Exception
   {
      URI example = FileLocator.resolve(Platform.getBundle("code2code.tests")
         .getResource(Fixtures.exampleGeneratorDir(generator))).toURI();

      URI destination = project.getProject().getFolder("generators").getFolder(generator + ".generator").getLocationURI();

      IFileStore sourceStore = EFS.getStore(example);
      IFileStore destinationStore = EFS.getStore(destination);

      sourceStore.copy(destinationStore, 0, null);

      project.getProject().getFolder("generators").refreshLocal(IResource.DEPTH_INFINITE, null);
   }

   public static IProject project()
   {
      return project;
   }

   public static void delete() throws Exception
   {
      if (project != null)
      {
         project.delete(true, true, null);
      }
   }

   public static void createFolder(String folderName) throws Exception
   {
      refresh();
      project.getFolder(folderName).getFullPath().toFile().mkdirs();
   }

   public static void createFileWithContents(String filePath, String content) throws Exception
   {
      refresh();
      File file = project.getFile(filePath).getRawLocation().toFile();
      FileUtils.write(content, file);
   }

   public static void createFile(String filePath) throws Exception
   {
      createFileWithContents(filePath, "");
   }

   public static boolean fileExists(String file) throws Exception
   {
      refresh();
      return project.getFile(file).exists();
   }

   public static String read(String file) throws Exception
   {
      refresh();
      return FileUtils.toString(project.getFile(file).getContents());
   }
   
   private static void refresh() throws Exception
   {
      if(project != null)
      {
         project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
      }
   }
}
