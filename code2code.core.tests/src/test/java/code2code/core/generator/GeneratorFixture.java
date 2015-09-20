/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import code2code.core.utils.FileUtils;

/**
 * A helper to create the generator files and folders in tests
 */
public class GeneratorFixture
{
   /** the generators to create */
   private List<FGenerator> m_generators = new ArrayList<FGenerator>();
   
   /**
    * Create the generators in the specified directory
    * @param p_root
    * @return
    *   the root
    * @throws IOException
    */
   public File install(File p_root) throws IOException
   {
      for(FGenerator gen : m_generators)
      {
         gen.create(p_root);
      }
      
      return p_root;
   }
   
   /**
    * Add a new generator to this fixture
    * @param p_name
    *   the name of the generator
    * @return
    *   the generator
    */
   public FGenerator newGenerator(String p_name)
   {
      return new FGenerator(p_name);
   }
   
   /**
    * Represents a generator
    */
   public class FGenerator
   {
      /** the generator name */
      private final String m_name;
      
      /** the generator description */
      private String m_description;
      
      /** the params file text */
      private List<String> m_params = new ArrayList<String>();
      
      /** the template file descriptor text*/
      private List<String> m_templateDescriptor = new ArrayList<String>();
      
      /** the template files text */
      private Map<String, List<String>> m_templateFiles = new LinkedHashMap<String, List<String>>();
      
      FGenerator(String p_name)
      {
         m_name = p_name;
         m_generators.add(this);
      }
      
      /**
       * Add parameters to the params file
       * @param p_lines
       * @return
       *    this object
       */
      public FGenerator parameters(String... p_lines)
      {
         m_params.addAll(Arrays.asList(p_lines));
         return this;
      }
      
      /**
       * Set the generator description file
       * @param p_description
       * @return
       *    this object
       */
      public FGenerator description(String p_description)
      {
         m_description = p_description;
         return this;
      }
      
      /**
       * Add a template file
       * @param p_name
       * @param p_lines
       * @return
       *    this object
       */
      public FGenerator templateFile(String p_name, String... p_lines)
      {
         m_templateFiles.put(p_name, Arrays.asList(p_lines));
         return this;
      }
      
      /**
       * Add template descriptors to the templates file
       * @param p_lines
       * @return
       *    this object
       */
      public FGenerator templateDescriptor(String... p_lines)
      {
         m_templateDescriptor.addAll(Arrays.asList(p_lines));
         return this;
      }
      
      /**
       * Create the files and directories
       * @param p_root
       * @throws IOException
       */
      void create(File p_root) throws IOException
      {
         // create the generator folder
         File genRoot = new File(p_root, "generators/" + m_name);
         genRoot.mkdirs();
         
         File params = new File(genRoot, "params.vm");
         FileUtils.write(asString(m_params), params);
         
         File description = new File(genRoot, "description.vm");
         FileUtils.write(m_description, description);
         
         File templates = new File(genRoot, "templates.vm");
         FileUtils.write(asString(m_templateDescriptor), templates);
         
         for(Map.Entry<String, List<String>> templateFile : m_templateFiles.entrySet())
         {
            File file = new File(genRoot, "templates/" + templateFile.getKey());
            FileUtils.write(asString(templateFile.getValue()), file);
         }
            
      }
   }
   
   private String asString(Collection<String> p_lines)
   {
      StringBuilder builder = new StringBuilder();
      for(String string : p_lines)
      {
         builder.append(string).append("\n");
      }
      return builder.toString();
      
   }
}