/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a collection of templates
 */
public class Generator
{
   /** the generator name */
   private final String m_name;
   
   /** the generator description */
   private final String m_description;
   
   /** the directory that represents the generator */
   @SuppressWarnings("unused")
   private final File m_generatorRoot;

   /** the templates that the generator houses */
   private final List<Template> m_templates;
   
   /** the parameters to the templates */
   private final Parameters m_parameters;

   /**
    * Constructor
    * @param p_generatorRoot
    * @param p_templates
    * @param p_parameters
    */
   Generator(String p_name, String p_description, File p_generatorRoot, List<Template> p_templates, Parameters p_parameters)
   {
      m_name = p_name;
      m_description = p_description;
      m_generatorRoot = p_generatorRoot;
      m_templates = p_templates;
      m_parameters = p_parameters;
   }
   
   /**
    * @return
    *   the name of the generator
    */
   public String getName()
   {
      return m_name;
   }
   
   /**
    * @return
    *   the description of this generator
    */
   public String getDescription()
   {
      return m_description;
   }
   
   /**
    * @return
    *   the templates within this generator
    */
   public List<Template> getTemplates()
   {
      return Collections.unmodifiableList(m_templates);
   }
   
   /**
    * @return
    *   the parameters required to instantiate this generator
    */
   public Parameters getParameters()
   {
      return m_parameters;
   }
   
   /**
    * Instantiate this template
    * @param p_context
    * @throws Exception
    */
   public void instantiate(Map<String, Object> p_context) throws Exception
   {
      for(Template template : m_templates)
      {
         File location = new File(template.getOutputLocation(p_context));
         
         String contents = template.instantiate(p_context);
         FileOutputStream output = new FileOutputStream(location);
         try
         {
            output.write(contents.getBytes());
         }
         finally
         {
            output.flush();
            output.close();
         }
      }
   }
}