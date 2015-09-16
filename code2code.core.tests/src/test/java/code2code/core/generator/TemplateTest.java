/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import code2code.core.templateengine.TemplateEngine;
import code2code.core.templateengine.VelocityTemplateEngine;

public class TemplateTest
{
   @Test
   public void simple_velocity_template() throws Exception
   {
      TemplateEngine engine = new VelocityTemplateEngine();
      
      String rawLocation = "foo/bar/${location_one}/${location_two}";
      String name = "simple_velocity_template";
      String contents = "${salutation} there ${name}. How are you ${time}?";
      
      Template template = new Template(engine, locator(contents), name, rawLocation);
      
      Map<String, Object> context = new HashMap<String, Object>();
      context.put("location_one", "baz");
      context.put("location_two", "bing");
      context.put("salutation", "Hi!");
      context.put("name", "Fred");
      context.put("time", "today");
      
      String instance = template.instantiate(context);
      assertThat(instance, is("Hi! there Fred. How are you today?"));
      
      String location = template.getOutputLocation(context);
      System.out.println("Location: " + location);
      assertThat(location, is("foo/bar/baz/bing"));
   }
   
   private TemplateLocator locator(String p_contents) throws IOException
   {
      TemplateLocator locator = mock(TemplateLocator.class);
      when(locator.locate()).thenReturn(new StringReader(p_contents));
      return locator;
   }
}

