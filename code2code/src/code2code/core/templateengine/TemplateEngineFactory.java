/**
 * 
 */
package code2code.core.templateengine;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

/**
 * Provide lookups for template engines based on file extensions
 */
public class TemplateEngineFactory
{
   /** The template engines that this factory is aware of */
   private static List<TemplateEngine> s_engines = new ArrayList<TemplateEngine>();
   static
   {
      s_engines.add(new GroovyTemplateEngine());
      s_engines.add(new FreemarkerTemplateEngine());
      s_engines.add(new VelocityTemplateEngine());
      s_engines.add(new PlainTemplateEngine());
   }

   /**
    * Find a template for the given folder and file that is supported by 
    * one of the template engines.
    * @param folder
    * @param fileName
    * @return
    *   the file, or null
    */
   public static IFile findKnownTemplate(IFolder folder, String fileName)
   {
      for (TemplateEngine templateEngine : s_engines)
      {
         IFile knownFile = templateEngine.getKnownFile(folder, fileName);
         if (knownFile != null)
         {
            return knownFile;
         }
      }
      return null;
   }

   /**
    * Find a template engine for the given file
    * @param p_file
    * @return
    *   the template engine or null if none is found
    */
   public static TemplateEngine forFile(IFile p_file)
   {
      return forFileName(p_file.getName());
   }

   /**
    * Find a template engine for the given file
    * @param p_fileName
    * @return
    *   the template engine or null if none is found
    */
   public static TemplateEngine forFileName(String p_fileName)
   {
      for (TemplateEngine templateEngine : s_engines)
      {
         if (templateEngine.canProcess(p_fileName))
         {
            return templateEngine;
         }
      }

      return null;
   }

   /**
    * @return
    *   a template engine that doesn't perform an substitutions
    */
   public static TemplateEngine noop()
   {
      return new PlainTemplateEngine();
   }

   /**
    * Find a template engine that supports the given file extension
    * @param p_extension
    * @return
    *   the template engine or null if none are found
    */
   public static TemplateEngine forExtension(String p_extension)
   {
      for (TemplateEngine templateEngine : s_engines)
      {
         if (templateEngine.knowExtension(p_extension))
         {
            return templateEngine;
         }
      }

      return null;
   }
}