package code2code.core.templateengine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import code2code.core.generator.Generator;
import code2code.utils.FileUtils;

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
   public Map<String, String> processMap(Generator generator, Map<String, String> params, Map<String, String> context)
   {
      return params;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processString(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processString(Generator generator, String content, Map<String, String> context) throws Exception
   {
      return content;
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processTemplate(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processTemplate(Generator generator, String templateName, Map<String, String> context) throws Exception
   {
      return FileUtils.toString(generator.getGeneratorFolder().getFile(templateName).getContents());
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