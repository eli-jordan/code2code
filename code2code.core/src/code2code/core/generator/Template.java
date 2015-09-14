package code2code.core.generator;

import java.io.File;
import java.util.Map;

import code2code.core.templateengine.TemplateEngine;

/**
 * Represents a templated file
 */
public class Template
{   
   /** the engine used to instantiate this template */
   private final TemplateEngine m_engine;
   
   /** the name of this template (i.e. the filename of the template file e.g. my_template.groovy) */
   private final String m_name;
   
   private final String m_rawLocation;
   
   /** the root directory for the generator */
   private final File m_generatorRoot;

   /**
    * TODO: Eli: Make a builder for this class
    * 
    * Constructor
    * @param p_engine
    * @param p_generatorRoot
    * @param p_rawLocation 
    * @param p_name
    */
   public Template(TemplateEngine p_engine, File p_generatorRoot, String p_rawLocation, String p_name)
   {
      m_engine = p_engine;
      m_generatorRoot = p_generatorRoot;
      m_rawLocation = p_rawLocation;
      m_name = p_name;
   }

   /**
    * @return
    *   the name of this template
    */
   public String getTemplateName()
   {
      return m_name;
   }

   /**
    * Apply the template to produce the final output data.
    * 
    * @param p_context 
    *   the context used to instantiate the template
    * @return
    *   an input stream with the data
    * @throws Exception
    */
   public String instantiate(Map<String, Object> p_context) throws Exception
   {
      // TODO: Eli: Add this back
      // if the output has been cached then just return it
      //      if (m_cachedOutput != null)
      //      {
      //         return new ByteArrayInputStream(m_cachedOutput.getBytes());
      //      }
      
      return m_engine.processTemplate(m_generatorRoot, getTemplateName(), p_context);
   }

   /**
    * The path where this template should be instantiated
    * @param p_context 
    *   the substitution context applied to the raw location string
    * @return
    *   the path
    * @throws Exception
    */
   public String getOutputLocation(Map<String, Object> p_context) throws Exception
   {
      return m_engine.processString(m_rawLocation, p_context);
      
//      if (m_selectedLocation != null)
//      {
//         return m_selectedLocation;
//      }
//      else
//      {
//         TemplateEngine engine = m_templatesConfig.getTemplateEngine();
//         Generator generator = m_templatesConfig.getGenerator();
//         return engine.processString(generator, m_initialLocation, generator.getInstantiationContext());
//      }
   }
//
//   /**
//    * @return
//    *   true if this template should be generated, false otherwise
//    */
//   public boolean isSelectedToGenerate()
//   {
//      return m_shouldGenerate;
//   }
//
//   /**
//    * Set whether this template should be generated
//    * @param selectedToGenerate
//    */
//   public void setSelectedToGenerate(boolean selectedToGenerate)
//   {
//      m_shouldGenerate = selectedToGenerate;
//   }
//
//   /**
//    * Cache the output
//    * @param p_cached
//    */
//   public void cacheOutput(String p_cached)
//   {
//      m_cachedOutput = p_cached;
//   }
//
//   /**
//    * Select the location
//    * @param userChoosenDestination
//    */
//   public void selectLocation(String userChoosenDestination)
//   {
//      m_selectedLocation = userChoosenDestination;
//   }
}