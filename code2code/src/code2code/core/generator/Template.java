package code2code.core.generator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import code2code.core.templateengine.TemplateEngine;
import code2code.core.templateengine.TemplateEngineFactory;

/**
 * Represents a templated file
 */
public class Template
{
   /** whether this template should be written out, default to true */
   private boolean m_shouldGenerate = true;

   /** if the output has already been generated, cache it. This happens if the preview button is used */
   private String m_cachedOutput;

   /** the file location specified in the wizard */
   private String m_selectedLocation;
   
   /** the location as defined in the config file */
   private final String m_initialLocation;

   /** the name of the template */
   private final String m_templateName;

   /** the template configuration */
   private final TemplatesConfig m_templatesConfig;

   /**
    * Constructor
    * @param p_templatesConfig
    * @param p_templateName
    * @param p_initialDestination
    */
   public Template(TemplatesConfig p_templatesConfig, String p_templateName, String p_initialDestination)
   {
      m_templatesConfig = p_templatesConfig;
      m_templateName = p_templateName;
      m_initialLocation = p_initialDestination;
   }

   /**
    * @return
    *   the name of this template
    */
   public String getTemplateName()
   {
      return m_templateName;
   }

   /**
    * Apply the template to produce the final output data
    * @return
    *   an input stream with the data
    * @throws Exception
    */
   public InputStream instantiate() throws Exception
   {
      // if the output has been cached then just return it
      if (m_cachedOutput != null)
      {
         return new ByteArrayInputStream(m_cachedOutput.getBytes());
      }

      TemplateEngine templateEngine = TemplateEngineFactory.forFileName(m_templateName);

      if (templateEngine != null)
      {
         Generator generator = m_templatesConfig.getGenerator();
         Map<String, String> context = generator.getInstantiationContext();
         String output = templateEngine.processTemplate(generator, m_templateName, context);
         
         return new ByteArrayInputStream(output.getBytes());
      }
      else
      {
         return m_templatesConfig.getGenerator().getGeneratorFolder().getFile(m_templateName).getContents();
      }
   }

   /**
    * The path where this template should be instantiated
    * @return
    *   the path
    * @throws Exception
    */
   public String getOutputLocation() throws Exception
   {
      if (m_selectedLocation != null)
      {
         return m_selectedLocation;
      }
      else
      {
         TemplateEngine engine = m_templatesConfig.getTemplateEngine();
         Generator generator = m_templatesConfig.getGenerator();
         return engine.processString(generator, m_initialLocation, generator.getInstantiationContext());
      }
   }

   /**
    * @return
    *   true if this template should be generated, false otherwise
    */
   public boolean isSelectedToGenerate()
   {
      return m_shouldGenerate;
   }

   /**
    * Set whether this template should be generated
    * @param selectedToGenerate
    */
   public void setSelectedToGenerate(boolean selectedToGenerate)
   {
      m_shouldGenerate = selectedToGenerate;
   }

   /**
    * Cache the output
    * @param p_cached
    */
   public void cacheOutput(String p_cached)
   {
      m_cachedOutput = p_cached;
   }

   /**
    * Select the location
    * @param userChoosenDestination
    */
   public void selectLocation(String userChoosenDestination)
   {
      m_selectedLocation = userChoosenDestination;
   }
}