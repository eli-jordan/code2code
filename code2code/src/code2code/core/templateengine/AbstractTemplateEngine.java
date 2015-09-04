package code2code.core.templateengine;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;

import code2code.core.generator.Generator;

/**
 * The base class for the templating engines
 */
public abstract class AbstractTemplateEngine implements TemplateEngine
{
   /**
    * @see code2code.core.templateengine.TemplateEngine#canFindKnownFile(org.eclipse.core.resources.IFolder, java.lang.String)
    */
   @Override
   public boolean canFindKnownFile(IFolder folder, String fileName)
   {
      for (String extension : getKnownExtensions())
      {
         if (folder.getFile(fileName + "." + extension).exists())
         {
            return true;
         }
      }

      return false;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#getKnownFile(org.eclipse.core.resources.IFolder, java.lang.String)
    */
   @Override
   public IFile getKnownFile(IFolder folder, String fileName)
   {
      for (String extension : getKnownExtensions())
      {
         if (folder.getFile(fileName + "." + extension).exists())
         {
            return folder.getFile(fileName + "." + extension);
         }
      }

      return null;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#canProcess(java.lang.String)
    */
   @Override
   public boolean canProcess(String fileName)
   {
      for (String extension : getKnownExtensions())
      {
         if (fileName.endsWith("." + extension))
         {
            return true;
         }
      }

      return false;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#knowExtension(java.lang.String)
    */
   @Override
   public boolean knowExtension(String extension)
   {
      return getKnownExtensions().contains(extension);
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processMap(code2code.core.generator.Generator, java.util.Map, java.util.Map)
    */
   @Override
   public Map<String, String> processMap(Generator generator, Map<String, String> params, Map<String, String> initialContext) throws Exception
   {
      Map<String, String> translatedMap = new LinkedHashMap<String, String>();

      Map<String, String> currentContext = new LinkedHashMap<String, String>();
      currentContext.putAll(initialContext);

      for (Entry<String, String> entry : params.entrySet())
      {
         String param = entry.getKey();
         String processedValue = processString(generator, entry.getValue(), currentContext);

         translatedMap.put(param, processedValue);
         currentContext.put(param, processedValue);
      }

      return translatedMap;
   }

   /**
    * @return
    *   the known extensions
    */
   public abstract List<String> getKnownExtensions();
}
