package code2code.core.generator;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import code2code.core.templateengine.TemplateEngine;
import code2code.core.templateengine.TemplateEngineFactory;

class UserParams
{
   private final GlobalParamsConfig m_globalParamsConfig;

   private Map<String, String> m_userConfiguredParams;

   private final Generator m_generator;

   private TemplateEngine m_templateEngine;

   private final List<Generator> m_nestedGenerators;

   private final ParamsConfig m_paramsConfig;

   public UserParams(Generator generator, GlobalParamsConfig globalParamsConfig, ParamsConfig paramsConfig, List<Generator> nestedGenerators)
   {
      m_globalParamsConfig = globalParamsConfig;

      m_generator = generator;
      m_paramsConfig = paramsConfig;

      m_nestedGenerators = nestedGenerators;

      if (paramsConfig.getTemplateEngine() != null)
      {
         m_templateEngine = paramsConfig.getTemplateEngine();
      }
      else
      {
         m_templateEngine = TemplateEngineFactory.noop();
      }

      setUserConfiguredParams(getRequiredParameters());
   }

   public Map<String, String> translated() throws Exception
   {
      return m_templateEngine.processMap(m_generator, m_userConfiguredParams, m_globalParamsConfig.getProcessedParams());
   }

   public void setUserConfiguredParams(Map<String, String> userConfiguredParams)
   {
      m_userConfiguredParams = userConfiguredParams;

      for (Generator nestedGenerator : m_nestedGenerators)
      {
         nestedGenerator.setUserConfiguredParams(userConfiguredParams);
      }
   }

   public Map<String, String> getRequiredParameters()
   {
      Map<String, String> allRequiredParams = new LinkedHashMap<String, String>();

      for (Generator nestedGenerator : m_nestedGenerators)
      {
         allRequiredParams.putAll(nestedGenerator.getRequiredParameters());
      }

      for (String key : m_paramsConfig.getParams().keySet())
      {
         allRequiredParams.remove(key);
      }
      allRequiredParams.putAll(m_paramsConfig.getParams());

      return allRequiredParams;
   }
}