/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import code2code.core.generator.GeneratorFixture.FGenerator;
import code2code.core.templateengine.TemplateEngineFactory;
import code2code.core.utils.FileUtils;

/**
 * Simple generator integration test
 */
public class GeneratorIntegrationTest
{
   /** rule to create temporary files and folders */
   @Rule
   public TemporaryFolder m_folder = new TemporaryFolder();
   
   /**
    * Test a simple integration scenario, that transforms a generator
    * from disk and instantiates the files.
    * 
    * @throws Exception
    */
   @Test
   public void simple_instantiation() throws Exception
   {
      GeneratorFixture fixture = new GeneratorFixture();
      FGenerator basicFixture = fixture.newGenerator("GeneratorIntegrationTest.generator");
      basicFixture.description("${collection_description}");
      basicFixture.parameters("salutation=",
         "title=",
         "name=",
         "output_dir=");

      basicFixture.templateDescriptor("templates/VelocityTemplate.vm=${output_dir}");
      basicFixture.templateFile("VelocityTemplate.vm", "${salutation} there ${title} ${name}");
      
      File root = fixture.install(m_folder.newFolder());
      File generatorRoot = new File(root, "generators/GeneratorIntegrationTest.generator");
      
      GeneratorTransformer constructor = new GeneratorTransformer(new TemplateEngineFactory(), null);
      Generator generator = constructor.create(generatorRoot);
      
      File outputFile = m_folder.newFile();
      
      System.out.println(outputFile);
      
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("salutation", "Hi");
      params.put("title", "Mr.");
      params.put("name", "Eli");
      params.put("output_dir", outputFile.getAbsolutePath());
      
      generator.instantiate(params);
      
      String data = FileUtils.toString(new FileInputStream(outputFile));
      assertThat(data, is("Hi there Mr. Eli"));
   }
}

