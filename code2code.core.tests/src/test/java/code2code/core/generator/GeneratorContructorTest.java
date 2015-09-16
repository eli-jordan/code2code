/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import static code2code.core.matchers.ParameterNamed.named;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import java.io.File;

import org.hamcrest.Matcher;
import org.junit.Test;

import code2code.core.templateengine.TemplateEngineFactory;
import code2code.core.templateengine.VelocityTemplateEngine;

/**
 * Tests transforming files on disk into a generator object
 */
public class GeneratorContructorTest
{
   @Test
   public void basic_template_structure() throws Exception
   {
      String projectRoot = "/home/elijordan/git/code2code/code2code.core.tests";
      String generatorRoot = "/home/elijordan/git/code2code/code2code.core.tests/generators/BasicTemplateStructure.generator";
      
      GeneratorConstructor constructor = new GeneratorConstructor(new TemplateEngineFactory(), new File(projectRoot));
      Generator generator = constructor.create(new File(generatorRoot));
      
      assertThat(generator.getName(), is("BasicTemplateStructure.generator"));
      assertThat(generator.getDescription(), is("A collection of templates used for testing"));
      
      Parameters parameters = generator.getParameters();
      assertThat(parameters, hasItemNamed("salutation"));
      assertThat(parameters, hasItemNamed("title"));
      assertThat(parameters, hasItemNamed("name"));
      assertThat(parameters, hasItemNamed("output_dir"));
      
      assertThat("The correct number of generators were found", generator.getTemplates(), hasSize(1));
      
      Template template = generator.getTemplates().get(0);
      assertThat("The correct template engine was identified", template.engine(), instanceOf(VelocityTemplateEngine.class));
      assertThat("The default location before template application is correct", template.getRawLocation(), is("${output_dir}"));
      assertThat("The correct template name was identified",  template.getTemplateName(), is("templates/VelocityTemplate.vm"));
   }
   
   private Matcher<Iterable<? super Parameter>> hasItemNamed(String p_name)
   {
      return hasItem(named(p_name));
   }
   
   @Test
   public void basic_template_structure_with_no_root() throws Exception
   {
      String generatorRoot = "/home/elijordan/git/code2code/code2code.core.tests/generators/BasicTemplateStructure.generator";
      
      GeneratorConstructor constructor = new GeneratorConstructor(new TemplateEngineFactory(), null);
      Generator generator = constructor.create(new File(generatorRoot));
      
      assertThat(generator.getName(), is("BasicTemplateStructure.generator"));
      assertThat(generator.getDescription(), is("${collection_description}"));
      
      Parameters parameters = generator.getParameters();
      assertThat(parameters, hasItemNamed("salutation"));
      assertThat(parameters, hasItemNamed("title"));
      assertThat(parameters, hasItemNamed("name"));
      assertThat(parameters, hasItemNamed("output_dir"));
      
      assertThat("The correct number of generators were found", generator.getTemplates(), hasSize(1));
      
      Template template = generator.getTemplates().get(0);
      assertThat("The correct template engine was identified", template.engine(), instanceOf(VelocityTemplateEngine.class));
      assertThat("The default location before template application is correct", template.getRawLocation(), is("${output_dir}"));
      assertThat("The correct template name was identified",  template.getTemplateName(), is("templates/VelocityTemplate.vm"));
   }
}