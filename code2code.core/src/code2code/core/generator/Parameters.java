/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the parameters to a generator
 */
public class Parameters implements Iterable<Parameter>
{
   /** the set of raw parameters parameters */
   private final Set<Parameter> m_parameters = new LinkedHashSet<Parameter>();
   
   /**
    * Add a new parameter
    * @param p_parameter
    */
   public void addParameter(Parameter p_parameter)
   {
      m_parameters.add(p_parameter);
   }
   
   void merge(Parameters p_parameters)
   {
      m_parameters.addAll(p_parameters.m_parameters);
   }
   
   Map<String, Object> asMap()
   {
      Map<String, Object> context = new HashMap<String, Object>();
      for(Parameter parameter : m_parameters)
      {
         context.put(parameter.name(), parameter.value());
      }
      return context;
   }

   /**
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<Parameter> iterator()
   {
      return m_parameters.iterator();
   }
}

