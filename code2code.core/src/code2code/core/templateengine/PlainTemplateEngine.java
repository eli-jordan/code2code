package code2code.core.templateengine;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import code2code.core.utils.FileUtils;

/**
 * A templating engine that performs no substitutions
 */
public class PlainTemplateEngine extends AbstractTemplateEngine implements TemplateEngine
{
   /**
    * @see code2code.core.templateengine.AbstractTemplateEngine#getKnownExtensions()
    */
   @Override
   public List<String> getKnownExtensions()
   {
      return Arrays.asList(new String[] {"txt", "properties", "params"});
   }

   /**
    * @see code2code.core.templateengine.AbstractTemplateEngine#processMap(code2code.core.generator.Generator, java.util.Map, java.util.Map)
    */
   @Override
   public Map<String, String> processMap(Map<String, String> params, Map<String, Object> context)
   {
      return params;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processString(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processString(String content, Map<String, Object> context) throws Exception
   {
      return content;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processTemplate(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processTemplate(File p_generatorRoot, String templateName, Map<String, Object> context) throws Exception
   {
      File templateFile = new File(p_generatorRoot, templateName);
      return FileUtils.toString(new FileInputStream(templateFile));
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#escape(java.lang.String)
    */
   @Override
   public String escape(String contents)
   {
      return contents;
   }
}