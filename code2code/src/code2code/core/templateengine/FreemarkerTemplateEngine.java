package code2code.core.templateengine;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import code2code.core.generator.Generator;
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
   public String processString(Generator generator, String templateContent, Map<String, String> context) throws Exception
   {

      Configuration configuration = new Configuration();

      configuration.setDirectoryForTemplateLoading(new File(generator.getGeneratorFolder().getLocationURI()));

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
   public String processTemplate(Generator generator, String templateName, Map<String, String> context) throws Exception
   {
      Configuration configuration = new Configuration();
      configuration.setDirectoryForTemplateLoading(new File(generator.getGeneratorFolder().getLocationURI()));

      Template template = configuration.getTemplate(templateName);

      Writer out = new StringWriter();

      Map<String, Object> root = new HashMap<String, Object>();
      root.putAll(context);

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