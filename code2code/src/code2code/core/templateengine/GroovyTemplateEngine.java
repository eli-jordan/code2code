package code2code.core.templateengine;

import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import code2code.core.generator.Generator;
import code2code.utils.FileUtils;

/**
 * A templating engine that uses groovy templates as a backing implementation
 */
public class GroovyTemplateEngine extends AbstractTemplateEngine implements TemplateEngine
{
   /**
    * @see code2code.core.templateengine.AbstractTemplateEngine#getKnownExtensions()
    */
   @Override
   public List<String> getKnownExtensions()
   {
      return Arrays.asList(new String[] {"groovy"});
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processTemplate(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processTemplate(Generator generator, String templateName, Map<String, String> context) throws Exception
   {
      SimpleTemplateEngine engine = new SimpleTemplateEngine();

      Template template = engine.createTemplate(FileUtils.toString(generator.getGeneratorFolder().getFile(templateName).getContents()));

      Writable writable = template.make(context);

      StringWriter stringWriter = new StringWriter();
      writable.writeTo(stringWriter);

      return stringWriter.toString();
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processString(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processString(Generator generator, String templateContent, Map<String, String> context) throws Exception
   {
      SimpleTemplateEngine engine = new SimpleTemplateEngine();

      Template template = engine.createTemplate(templateContent);

      Writable writable = template.make(context);

      StringWriter stringWriter = new StringWriter();
      writable.writeTo(stringWriter);

      return stringWriter.toString();
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#escape(java.lang.String)
    */
   @Override
   public String escape(String contents)
   {
      String escaped = contents
         .replace("\\", "\\\\")
         .replace("$", "\\$")
         .replaceAll("(?<=<)%|%(?=>)", "<%=\"%\"%>");

      return escaped;
   }
}