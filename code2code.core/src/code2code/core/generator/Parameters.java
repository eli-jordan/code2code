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
   
   /**
    * @return
    *   these parameters as a map
    */
   public Map<String, Object> asMap()
   {
      Map<String, Object> context = new HashMap<String, Object>();
      for(Parameter parameter : m_parameters)
      {
         context.put(parameter.name(), parameter.defaultValue());
      }
      return context;
   }
   
   /**
    * @return
    *   true if there are no parameters, otherwise false
    */
   public boolean isEmpty()
   {
      return m_parameters.isEmpty();
   }

   /**
    * @see java.lang.Iterable#iterator()
    */
   @Override
   public Iterator<Parameter> iterator()
   {
      return m_parameters.iterator();
   }

   @Override
   public String toString()
   {
      return "Parameters [m_parameters=" + m_parameters + "]";
   }
}