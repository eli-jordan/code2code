/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
   
   public String getDisplayName()
   {
      return m_name.replaceAll("\\.generator$", "");
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
    * @param p_templates
    * @return
    *   a builder that will only instantiate the specified templates
    * @throws Exception
    */
   public InstanceBuilder instantiteTemplates(Collection<Template> p_templates) throws Exception
   {
      return new InstanceBuilder(this, new HashSet<Template>(p_templates));
   }
   
   /**
    * Instantiate all templates in this generator with the specified context
    * @param p_context
    * @throws Exception
    */
   public void instantiate(Map<String, Object> p_context) throws Exception
   {
      instantiate(p_context, m_templates);
   }
   
   void instantiate(Map<String, Object> p_context, Collection<Template> p_templates) throws Exception
   {
      for(Template template : p_templates)
      {
         if(!m_templates.contains(template))
         {
            throw new IllegalStateException("Template " + template + " doesn't belong to this generator");
         }
         
         File location = new File(m_generatorRoot, template.getOutputLocation(p_context));
         if(!location.exists())
         {
            location.getParentFile().mkdirs();
            location.createNewFile();
         }
         
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

   @Override
   public String toString()
   {
      return "Generator [m_name=" + m_name + ", m_description=" + m_description + ", m_generatorRoot=" + m_generatorRoot + "]";
   }
}