package code2code.core.generator;

import java.io.File;
import java.io.Reader;
import java.io.StringWriter;
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
   private final TemplateLocator m_locator;

   /**
    * TODO: Eli: Make a builder for this class
    * 
    * @param p_engine 
    * @param p_locator 
    * @param p_rawLocation 
    */
   public Template(TemplateEngine p_engine, TemplateLocator p_locator, String p_name, String p_rawLocation)
   {
      m_engine = p_engine;
      m_locator = p_locator;
      m_name = p_name;
      m_rawLocation = p_rawLocation;
   }
   
   String getRawLocation()
   {
      return m_rawLocation;
   }
   
   /**
    * @return
    *   the template engine
    */
   TemplateEngine engine()
   {
      return m_engine;
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
      
      Reader contents = m_locator.locate();
      
      StringWriter writer = new StringWriter();
      m_engine.process(getTemplateName(), contents, writer, p_context);
      
      return writer.toString();
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

   @Override
   public String toString()
   {
      return "Template [m_engine=" + m_engine + ", m_name=" + m_name + ", m_rawLocation=" + m_rawLocation + "]";
   }
}