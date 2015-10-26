/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.ui.wizards.generate;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import code2code.core.generator.Generator;
import code2code.core.generator.Parameter;
import code2code.core.generator.Template;

/**
 * Contains the data input by the user when navigating the wizard
 */
public class GenerateFilesWizardModel
{
   /** the selected generators */
   private Generator m_selected;
   
   /** the templates selected to generate */
   private Set<Template> m_selectedTemplates = new LinkedHashSet<Template>();
   
   /** the parameters specified */
   private Map<String, Object> m_parameters = new HashMap<String, Object>();

   /**
    * @return
    *   the currently selected generator
    */
   public Generator getGenerator()
   {
      return m_selected;
   }

   /**
    * Set the selected generator
    * @param p_selected
    */
   public void setGenerator(Generator p_selected)
   {
      m_selected = p_selected;
      
      // initialise the default values for the parameters
      for (Parameter parameter : p_selected.getParameters())
      {
         setParameter(parameter.name(), parameter.defaultValue());
      }
      
      // initialise all templates as selected
      m_selectedTemplates.addAll(p_selected.getTemplates());
   }
   
   /**
    * Set a parameter value 
    * @param p_key
    * @param p_value
    */
   public void setParameter(String p_key, Object p_value)
   {
      m_parameters.put(p_key, p_value);
   }

   /**
    * Set the specified template as selected
    * @param p_template
    */
   public void selectTemplate(Template p_template)
   {
      m_selectedTemplates.add(p_template);
   }
   
   /**
    * Set the specified template as unselected
    * @param p_template
    */
   public void unselectTemplate(Template p_template)
   {
      m_selectedTemplates.remove(p_template);
   }
   
   /**
    * @return
    *   the templates that have been selected to generate
    */
   public Set<Template> getSelectedTemplates()
   {
      return Collections.unmodifiableSet(m_selectedTemplates);
   }
   
   /**
    * @return
    *   the context to instantiate the generator
    */
   public Map<String, Object> getParameters()
   {
      return new HashMap<String, Object>(m_parameters);
   }
}