package code2code.core.templateengine;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * A templating engine that uses free marker as a backing implementation
 */
public class FreemarkerTemplateEngine extends AbstractTemplateEngine implements TemplateEngine
{
   /**
    * @see code2code.core.templateengine.AbstractTemplateEngine#getKnownExtensions()
    */
   @Override
   public List<String> getKnownExtensions()
   {
      return Arrays.asList(new String[] {"ftl"});
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processString(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processString(String templateContent, Map<String, Object> context) throws Exception
   {
      Configuration configuration = new Configuration();

      //configuration.setDirectoryForTemplateLoading(new File(generator.getGeneratorFolder().getLocationURI()));

      StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
      FileTemplateLoader fileTemplateLoader = new FileTemplateLoader();

      TemplateLoader templateLoader = new MultiTemplateLoader(new TemplateLoader[] {stringTemplateLoader, fileTemplateLoader});

      stringTemplateLoader.putTemplate("temp:param", templateContent);

      configuration.setTemplateLoader(templateLoader);

      Template template = configuration.getTemplate("temp:param");

      Writer out = new StringWriter();

      Map<String, Object> root = new HashMap<String, Object>();

      root.putAll(context);

      template.process(root, out, new DefaultObjectWrapper());

      return out.toString();
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processTemplate(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processTemplate(File p_generatorRoot, String p_templateFile, Map<String, Object> p_context) throws Exception
   {
      Configuration configuration = new Configuration();
      configuration.setDirectoryForTemplateLoading(p_generatorRoot);

      Template template = configuration.getTemplate(p_templateFile);

      Writer out = new StringWriter();

      Map<String, Object> root = new HashMap<String, Object>();
      root.putAll(p_context);

      template.process(root, out, new DefaultObjectWrapper());
      return out.toString();
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#escape(java.lang.String)
    */
   public String escape(String contents)
   {
      String escaped = contents
         .replace("${", "${r'${'}")
         .replace("#{", "${r'#{'}")
         .replace("<#", "${r'<#'}")
         .replace("</#", "${r'</#'}");
      return escaped;
   }
}