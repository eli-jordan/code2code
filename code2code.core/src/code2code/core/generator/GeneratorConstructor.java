/********************************************************************/
/* (c) Elias Jordan 2015                                            */
/********************************************************************/

package code2code.core.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import code2code.core.templateengine.TemplateEngine;
import code2code.core.templateengine.TemplateEngineFactory;
import code2code.core.utils.FileUtils;

/**
 * Knows how to create a Generator based on a directory
 */
public class GeneratorConstructor
{
   /** factory for accessing the template engines */
   private final TemplateEngineFactory m_engines;
   
   /** the generator root */
   private final File m_root;
   
   /**
    * Constructor
    * @param p_engines 
    * @param p_root
    */
   public GeneratorConstructor(TemplateEngineFactory p_engines, File p_root)
   {
      m_engines = p_engines;
      m_root = p_root;
   }

   Generator create(File p_generatorRoot) throws Exception
   {
      Parameters global = getGlobalParameters();
      
      String description = getDescription(p_generatorRoot, global);
      String name = getGeneratorName(p_generatorRoot);
      
      Parameters generator = getGeneratorParameters(p_generatorRoot, global);
      List<Template> templates = getTemplates(p_generatorRoot);
      
      return new Generator(name, description, m_root, templates, generator);
   }
   
   /**
    * @param p_generatorRoot
    * @return
    *   the name of the generator
    */
   private String getGeneratorName(File p_generatorRoot)
   {
      return p_generatorRoot.getName();
   }
   
   /**
    * Read the templates config file and create the template domain objects
    * @param p_generatorRoot
    * @return
    * @throws IOException
    */
   private List<Template> getTemplates(File p_generatorRoot) throws IOException
   {
      List<Template> templates = new ArrayList<Template>();
      
      File templatesFile = m_engines.findKnownTemplate(p_generatorRoot, "templates");
      if(templatesFile != null)
      {
         Map<String, String> templatesConfig = FileUtils.loadProperties(new FileInputStream(templatesFile));
         for (Entry<String, String> entry : templatesConfig.entrySet())
         {
            String templateName = entry.getKey();
            String rawLocation = entry.getValue();
            
            TemplateEngine engine = m_engines.forFileName(templateName);
            TemplateLocator locator = new TemplateLocator(p_generatorRoot, templateName);
            
            templates.add(new Template(engine, locator, templateName, rawLocation));

            //TODO: Eli: Do I want to keep the nested generators? Not sure what the use case is
            //         if (templateName.endsWith(".generator"))
            //         {
            //            nestedGenerators.add(Generator.fromFolder(generator.getGeneratorFolder().getFolder(templateName)));
            //         }
         }
      }
      
      return templates;
   }
   
   /**
    * Get the parameters required for the template
    * 
    * Location: <generator root>/params.{engine ext}
    * 
    * @param p_generatorFolder
    * @return
    *   the parameters
    */
   private Parameters getGeneratorParameters(File p_generatorFolder, Parameters p_global) throws Exception
   {
      File parametersFile = m_engines.findKnownTemplate(p_generatorFolder, "params");
      if(parametersFile != null)
      {
         TemplateEngine engine = m_engines.forFile(parametersFile);
         
         Parameters parameters = new Parameters();
         
         for(Map.Entry<String, String> entry : loadPropertiesFile(parametersFile).entrySet())
         {
            String name = entry.getKey();
            String rawValue = entry.getValue();
            String processedValue = engine.processString(rawValue, p_global.asMap());
            
            parameters.addParameter(new Parameter(name, processedValue));
            
         }
         
         return parameters;
      }
      
      return new Parameters();
   }
   
   /**
    * Get the generator description.
    * 
    * Location: <generator root>/description
    * 
    * The global parameters are used as a substitution context
    * 
    * @param p_generatorFolder
    *   the root folder for the generator
    * @param p_global
    *   the global parameters, used as a substitution context
    * @return
    *   the description
    */
   private String getDescription(File p_generatorFolder, Parameters p_global)
   {
      String description = "";
      
      try
      {
         File descriptionFile = m_engines.findKnownTemplate(p_generatorFolder, "description");
         if(descriptionFile != null)
         {
            TemplateEngine engine = m_engines.forFile(descriptionFile);
            String fileData = FileUtils.toString(new FileInputStream(descriptionFile));
            description = engine.processString(fileData, p_global.asMap());
         }
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      
      return description;
   }
   
   /**
    * Get an parameters defined in the <project root>/generator/params file
    * @return
    *   the global parameters
    */
   private Parameters getGlobalParameters()
   {
      if(m_root != null)
      {
         File rootGenerator = new File(m_root, "generators");
         if(rootGenerator.exists())
         {
            File rootParams = m_engines.findKnownTemplate(rootGenerator, "params");
            if(rootParams != null)
            {
               return asParameters(loadPropertiesFile(rootParams));
            }
         }
      }
      
      return new Parameters();
   }
   
   /**
    * Convert a map to a set of parameters
    * @param p_properties
    * @return
    */
   private Parameters asParameters(Map<String, String> p_properties)
   {
      Parameters parameters = new Parameters();
      
      for(Map.Entry<String, String> entry : p_properties.entrySet())
      {
         parameters.addParameter(new Parameter(entry.getKey(), entry.getValue()));
      }
      
      return parameters;
   }
   
   private Map<String, String> loadPropertiesFile(File p_file)
   {
      try
      {
         return FileUtils.loadProperties(new FileInputStream(p_file));
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
      // if loding failed, just use empty properties
      return new LinkedHashMap<String, String>();
   }
}

