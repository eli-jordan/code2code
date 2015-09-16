package code2code.core.templateengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import code2code.core.utils.FileUtils;
import groovy.lang.Writable;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

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
   public String processTemplate(File p_generatorRoot, String p_templateName, Map<String, Object> p_context) throws Exception
   {
      SimpleTemplateEngine engine = new SimpleTemplateEngine();

      File file = new File(p_generatorRoot, p_templateName);
      Template template = engine.createTemplate(FileUtils.toString(new FileInputStream(file)));
      Writable writable = template.make(p_context);

      StringWriter stringWriter = new StringWriter();
      writable.writeTo(stringWriter);

      return stringWriter.toString();
   }
   
   @Override
   public void process(String p_name, Reader p_reader, Writer p_writer, Map<String, Object> p_context) throws Exception
   {
      SimpleTemplateEngine engine = new SimpleTemplateEngine();
      Template template = engine.createTemplate(p_reader);
      Writable writable = template.make(p_context);
      writable.writeTo(p_writer);
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processString(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processString(String templateContent, Map<String, Object> context) throws Exception
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

   @Override
   public String toString()
   {
      return "GroovyTemplateEngine";
   }
}