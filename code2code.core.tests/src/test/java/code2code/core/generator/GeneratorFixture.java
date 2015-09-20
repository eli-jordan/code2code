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

public class GeneratorFixture
{
   private List<FGenerator> m_generators = new ArrayList<FGenerator>();
   
   public File install(File p_root) throws IOException
   {
      for(FGenerator gen : m_generators)
      {
         gen.create(p_root);
      }
      
      return p_root;
   }
   
   public FGenerator newGenerator(String p_name)
   {
      return new FGenerator(p_name);
   }
   
   public class FGenerator
   {
      private final String m_name;
      
      private String m_description;
      
      private List<String> m_params = new ArrayList<String>();
      
      private List<String> m_templateDescriptor = new ArrayList<String>();
      
      private Map<String, List<String>> m_templateFiles = new LinkedHashMap<String, List<String>>();
      

      FGenerator(String p_name)
      {
         m_name = p_name;
         m_generators.add(this);
      }
      
      public FGenerator parameters(String... p_lines)
      {
         m_params.addAll(Arrays.asList(p_lines));
         return this;
      }
      
      public FGenerator description(String p_description)
      {
         m_description = p_description;
         return this;
      }
      
      public FGenerator templateFile(String p_name, String... p_lines)
      {
         m_templateFiles.put(p_name, Arrays.asList(p_lines));
         return this;
      }
      
      public FGenerator templateDescriptor(String... p_lines)
      {
         m_templateDescriptor.addAll(Arrays.asList(p_lines));
         return this;
      }
      
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

