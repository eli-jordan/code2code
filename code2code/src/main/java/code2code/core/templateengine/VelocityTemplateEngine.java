package code2code.core.templateengine;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import code2code.core.generator.Generator;

/**
 * A templating engine that uses apache velocity as a backing implementation
 */
public class VelocityTemplateEngine extends AbstractTemplateEngine implements TemplateEngine
{
   /**
    * @see code2code.core.templateengine.AbstractTemplateEngine#getKnownExtensions()
    */
   @Override
   public List<String> getKnownExtensions()
   {
      return Arrays.asList(new String[] {"vm"});
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processTemplate(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processTemplate(Generator generator, String templateName, Map<String, String> context) throws Exception
   {
      VelocityEngine velocityEngine = createVelocityEngine(generator);
      VelocityContext velocityContext = createVelocityContext(context);

      StringWriter writer = new StringWriter();
      velocityEngine.mergeTemplate(templateName, "UTF-8", velocityContext, writer);

      return writer.toString();
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#processString(code2code.core.generator.Generator, java.lang.String, java.util.Map)
    */
   @Override
   public String processString(Generator generator, String templateContent, Map<String, String> context) throws Exception
   {
      VelocityEngine velocityEngine = createVelocityEngine(generator);
      VelocityContext velocityContext = createVelocityContext(context);

      StringWriter writer = new StringWriter();
      velocityEngine.evaluate(velocityContext, writer, "preview", templateContent);

      return writer.toString();
   }

   /**
    * @see code2code.core.templateengine.TemplateEngine#escape(java.lang.String)
    */
   @Override
   public String escape(String contents)
   {
      String escaped = contents
         .replace("$", "${d}")
         .replace("#", "${b}");
      
      escaped = "#set($b='#')\n" + "#set($d='$')\n" + escaped;

      return escaped;
   }

   /**
    * Initialise the engine
    * @param generator
    * @return
    * @throws Exception
    */
   private VelocityEngine createVelocityEngine(Generator generator) throws Exception
   {
      // Velocity uses the thread context class loader to load classes based on name.
      // Since we are in OSGi this doesn't work as expected. To make the this work
      // override the thread context class loader with this bundles loader. This works
      // because velocity and all its dependencies are bundled.
      Thread thread = Thread.currentThread();
      ClassLoader before = thread.getContextClassLoader();
      try
      {
         thread.setContextClassLoader(getClass().getClassLoader());
         
         Properties properties = new Properties();
         properties.setProperty("file.resource.loader.path", generator.getGeneratorFolder().getLocation().toString());
         
         VelocityEngine engine = new VelocityEngine();
         engine.init(properties);

         return engine;
      }
      finally
      {
         thread.setContextClassLoader(before);
      }
   }

   private VelocityContext createVelocityContext(Map<String, String> context)
   {
      VelocityContext velocityContext = new VelocityContext();

      for (String key : context.keySet())
      {
         velocityContext.put(key, context.get(key));
      }
      return velocityContext;
   }
}