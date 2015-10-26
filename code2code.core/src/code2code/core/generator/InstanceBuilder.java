/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.util.Map;
import java.util.Set;

/**
 * Helper object that provides a fluent API for generator instantiation
 */
public class InstanceBuilder
{
   /** the source generator */
   private final Generator m_generator;

   /**  the selected templates */
   private final Set<Template> m_templates;
   
   /**
    * Constructor
    * @param p_generator
    * @param p_templates
    */
   InstanceBuilder(Generator p_generator, Set<Template> p_templates)
   {
      m_generator = p_generator;
      m_templates = p_templates;
   }
   
   /**
    * Specifies the context used for instantiating the templates,
    * and also writes the instantiated files.
    * @param p_context
    * @throws Exception
    */
   public void withContext(Map<String, Object> p_context) throws Exception
   {
      m_generator.instantiate(p_context, m_templates);
   }
}