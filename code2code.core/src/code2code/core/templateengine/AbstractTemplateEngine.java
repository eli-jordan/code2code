package code2code.core.templateengine;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The base class for the templating engines
 */
abstract class AbstractTemplateEngine implements TemplateEngine
{
   /**
    * @see code2code.core.templateengine.TemplateEngine#getKnownFile(org.eclipse.core.resources.IFolder, java.lang.String)
    */
   @Override
   public File getKnownFile(File folder, String fileName)
   {
      for (String extension : getKnownExtensions())
      {
         File file = new File(folder, fileName + "." + extension);
         if (file.exists())
         {
            return file;
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
    * @see code2code.core.templateengine.TemplateEngine#processMap(code2code.core.generator.OldGenerator, java.util.Map, java.util.Map)
    */
   @Override
   public Map<String, String> processMap(Map<String, String> p_params, Map<String, Object> initialContext) throws Exception
   {
      Map<String, String> translatedMap = new LinkedHashMap<String, String>();

      Map<String, Object> currentContext = new LinkedHashMap<String, Object>();
      currentContext.putAll(initialContext);

      for (Entry<String, String> entry : p_params.entrySet())
      {
         String param = entry.getKey();
         String processedValue = processString(entry.getValue(), currentContext);

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
