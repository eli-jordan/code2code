package code2code.core.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFolder;

/**
 * Represents a generator that can create multiple files based on the templates within
 */
public class Generator
{
   /** the folder that defines this generator */
   private final IFolder m_generatorFolder;

   /** the generator description */
   private final DescriptionConfig m_descriptionConfig;

   /** the config defined by the 'params' file at the root of the project */
   private final GlobalParamsConfig m_globalParamsConfig;

   /** the parameters defined for this generator */
   private final ParamsConfig m_paramsConfig;

   private final TemplatesConfig m_templatesConfig;

   private final List<Generator> m_nestedGenerators;

   private final List<Template> m_templates;

   private final UserParams m_userParams;
   
   /** preset parameters that are defined by the plugin */
   private Map<String, String> m_presetParameters = new HashMap<String, String>();

   /**
    * Private constructor
    * @param folder
    * @throws Exception
    */
   private Generator(IFolder folder) throws Exception
   {
      m_generatorFolder = folder;

      m_globalParamsConfig = new GlobalParamsConfig(this);
      m_descriptionConfig = new DescriptionConfig(this, m_globalParamsConfig);
      m_paramsConfig = new ParamsConfig(this);
      m_templatesConfig = new TemplatesConfig(this);

      m_nestedGenerators = m_templatesConfig.getNestedGenerators();

      m_templates = getAllTemplates();

      m_userParams = new UserParams(this, m_globalParamsConfig, m_paramsConfig, m_nestedGenerators);
   }

   /**
    * Create a new generated object using the specified folder as the root
    * @param folder
    * @return
    *   a new generator
    * @throws Exception
    */
   public static Generator fromFolder(IFolder folder) throws Exception
   {
      return new Generator(folder);
   }

   private List<Template> getAllTemplates() throws Exception
   {
      List<Template> templates = m_templatesConfig.getTemplates();

      for (Generator nestedGenerator : m_nestedGenerators)
      {
         templates.addAll(nestedGenerator.getAllTemplates());
      }

      return templates;
   }
   
   /**
    * Add a preset parameter. These are parameters that are defined by the 
    * plugin, and can be used by templates.
    * 
    * e.g. the path to the current selection
    * 
    * @param p_key
    * @param p_value
    */
   public void addPresetParameter(String p_key, String p_value)
   {
      m_presetParameters.put(p_key, p_value);
   }
   
   /**
    * Add preset parameters
    * @param p_presets
    */
   public void addPresetParameters(Map<String, String> p_presets)
   {
      m_presetParameters.putAll(p_presets);
   }

   /**
    * @return
    *   the templates that should be instantiated
    * @throws Exception
    */
   public List<Template> getTemplatesToInstantiate() throws Exception
   {
      List<Template> templatesToGenerate = new ArrayList<Template>();

      for (Template template : m_templates)
      {
         if (template.isSelectedToGenerate())
         {
            templatesToGenerate.add(template);
         }
      }

      return templatesToGenerate;
   }

   /**
    * @return
    *   the folder where the generator is defined
    */
   public IFolder getGeneratorFolder()
   {
      return m_generatorFolder;
   }

   /**
    * @return
    *   the generator name 
    */
   public String getName()
   {
      return m_generatorFolder.getFullPath().removeFirstSegments(2).removeFileExtension().toString();
   }

   /**
    * @return
    *   the generator description
    */
   public String getDescription()
   {
      return m_descriptionConfig.getDescription();
   }

   /**
    * @return
    *   the parameters that must be defined in order to instantiate the templates in this generator
    */
   public Map<String, String> getRequiredParameters()
   {
      return m_userParams.getRequiredParameters();
   }

   /**
    * The context is what defines the values that are inserted for placeholders in the template files
    * 
    * @return
    *   the context that is used to apply the templates defined by this generator
    * @throws Exception
    */
   public Map<String, String> getInstantiationContext() throws Exception
   {
      Map<String, String> context = m_userParams.translated();
      
      // add all the preset parameters to the context
      context.putAll(m_presetParameters);
      return context;
   }

   /**
    * Set the parameters that were defined on the parameters page
    * @param p_params
    */
   public void setUserConfiguredParams(Map<String, String> p_params)
   {
      m_userParams.setUserConfiguredParams(p_params);
   }

   /**
    * @return
    *   all templates defined by this generator
    */
   public List<Template> getTemplates()
   {
      return m_templates;
   }
}