/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

/**
 * Represents a template parameter
 */
public class Parameter
{
   /** the name of the parameter */
   private final String m_name;
   
   /** the default value */
   private final String m_defaultValue;

   Parameter(String p_name, String p_defaultValue)
   {
      m_name = p_name;
      m_defaultValue = p_defaultValue;
   }
   
   /**
    * @return
    *   the name of this parameter
    */
   public String name()
   {
      return m_name;
   }

   /**
    * @return
    *   the value of the parameter
    */
   public Object defaultValue()
   {
      return m_defaultValue;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Parameter other = (Parameter) obj;
      if (m_name == null)
      {
         if (other.m_name != null)
            return false;
      }
      else if (!m_name.equals(other.m_name))
         return false;
      return true;
   }

   @Override
   public String toString()
   {
      return "Parameter [m_name=" + m_name + ", m_defaultValue=" + m_defaultValue + "]";
   }
}