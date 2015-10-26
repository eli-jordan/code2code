/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import code2code.core.generator.GeneratorFixture.FGenerator;
import code2code.core.templateengine.TemplateEngineFactory;

/**
 * Test scanning for generators 
 */
public class GeneratorLocatorTest
{
   /** rule to create temporary files and folders */
   @Rule
   public TemporaryFolder m_folder = new TemporaryFolder();
   
   /**
    * Test that the correct directories are located 
    * @throws Exception
    */
   @Test
   public void find_simple_generator() throws Exception
   {
      GeneratorFixture fixture = new GeneratorFixture();
      fixture.newGenerator("EmptyGenerator.generator");
      
      FGenerator basicFixture = fixture.newGenerator("SimpleGenerator.generator");
      basicFixture.description("${collection_description}");

      basicFixture.templateDescriptor("templates/VelocityTemplate.vm=.");
      basicFixture.templateFile("VelocityTemplate.vm", "this is a constant");
      
      File root = m_folder.newFolder();
      fixture.install(root);
      
      GeneratorLocator locator = new GeneratorLocator(new TemplateEngineFactory());
      Set<Generator> generators = locator.findGenerators(root);
      
      
      assertThat(generators, hasGeneratorNamed("EmptyGenerator.generator"));
      assertThat(generators, hasGeneratorNamed("SimpleGenerator.generator"));
   }
   
   private Matcher<Iterable<? super Generator>> hasGeneratorNamed(String p_name)
   {
      return hasItem(generatorNamed(p_name));
   }
   
   private Matcher<Generator> generatorNamed(final String p_name)
   {
      return new TypeSafeMatcher<Generator>()
      {

         @Override
         public void describeTo(Description p_description)
         {
            p_description.appendText(p_name);
         }

         @Override
         protected boolean matchesSafely(Generator p_item)
         {
            return p_item.getName().equals(p_name);
         }
      };
   }
}

