package code2code.core.generator;

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
   
   /** the raw output location */
   private final String m_rawLocation;
   
   /** the root directory for the generator */
   private final TemplateLocator m_locator;
   
   private boolean m_selected = true;
   
   private String m_locationOverride;

   /**
    * Constructor
    * @param p_builer
    */
   private Template(Builder p_builer)
   {
      m_engine = p_builer.m_engine;
      m_locator = p_builer.m_locator;
      m_name = p_builer.m_name;
      m_rawLocation = p_builer.m_rawLocation;
   }
   
   /**
    * @return
    *   a new template builder
    */
   public static Builder builder()
   {
      return new Builder();
   }
   
   public void setSelected(boolean p_selected)
   {
      m_selected = p_selected;
   }
   
   public boolean isSelected()
   {
      return m_selected;
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
      if(m_locationOverride != null)
      {
         return m_locationOverride;
      }
      else
      {
         return m_engine.processString(m_rawLocation, p_context);
      }
   }
   
   /**
    * Override the default output location
    * @param p_location
    */
   public void overrideLocation(String p_location)
   {
      m_locationOverride = p_location;
   }

   /**
    * A builder used to construct a template
    */
   @SuppressWarnings("javadoc")
   public static class Builder
   {
      private TemplateEngine m_engine;
      
      private String m_name;
      
      private String m_rawLocation;
      
      private TemplateLocator m_locator;
      
      public Builder engine(TemplateEngine p_engine)
      {
         m_engine = p_engine;
         return this;
      }
      
      public Builder name(String p_name)
      {
         m_name = p_name;
         return this;
      }
      
      public Builder rawLocation(String p_location)
      {
         m_rawLocation = p_location;
         return this;
      }
      
      public Builder templateData(TemplateLocator p_locator)
      {
         m_locator = p_locator;
         return this;
      }
      
      public Template build()
      {
         return new Template(this);
      }
   }
   
   @Override
   public String toString()
   {
      return "Template [m_engine=" + m_engine + ", m_name=" + m_name + ", m_rawLocation=" + m_rawLocation + "]";
   }
}