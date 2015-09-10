package code2code.core.generator;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;

import code2code.core.templateengine.TemplateEngine;
import code2code.core.templateengine.TemplateEngineFactory;
import code2code.utils.FileUtils;

/**
 * Represents the parameters file
 */
class ParamsConfig
{
   /** the parameters as defined in the file (i.e. before applying the template engine) */
   private Map<String, String> m_initialParams;

   /** the generator these parameters belong to */
   private final Generator m_generator;

   /** the template engine that will be applied to the value */
   private TemplateEngine m_templateEngine;

   public ParamsConfig(Generator generator) throws Exception
   {
      m_generator = generator;

      IFile file = TemplateEngineFactory.findKnownTemplate(generator.getGeneratorFolder(), "params");

      if (file == null)
      {
         m_initialParams = new LinkedHashMap<String, String>();
      }
      else
      {
         this.m_templateEngine = TemplateEngineFactory.forFile(file);
         m_initialParams = FileUtils.loadProperties(file.getContents());
      }
   }

   public TemplateEngine getTemplateEngine()
   {
      return m_templateEngine;
   }

   public Map<String, String> translated(Map<String, String> context) throws Exception
   {
      return m_templateEngine.processMap(m_generator, m_initialParams, context);
   }

   public Map<String, String> getParams()
   {
      return m_initialParams;
   }
}