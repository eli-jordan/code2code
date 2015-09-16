/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import code2code.core.generator.Parameter;

/**
 * Matches against a parameter name
 */
public class ParameterNamed extends TypeSafeMatcher<Parameter>
{
   /** the name to match against */
   private final String p_name;
   
   /**
    * Constructor
    * @param p_p_name
    */
   private ParameterNamed(String p_p_name)
   {
      p_name = p_p_name;
   }

   /**
    * Factory method
    * @param p_name
    * @return
    *   the matcher
    */
   public static Matcher<Parameter> named(String p_name)
   {
      return new ParameterNamed(p_name);
   }
   
   /**
    * @see org.hamcrest.SelfDescribing#describeTo(org.hamcrest.Description)
    */
   @Override
   public void describeTo(Description p_desc)
   {
      p_desc.appendText(p_name);
   }

   /**
    *
    * @see org.hamcrest.TypeSafeMatcher#matchesSafely(java.lang.Object)
    */
   @Override
   protected boolean matchesSafely(Parameter p_parameter)
   {
      return p_name.equals(p_parameter.name());
   }
}