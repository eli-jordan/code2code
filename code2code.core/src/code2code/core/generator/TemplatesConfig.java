//package code2code.core.generator;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map.Entry;
//
//import org.eclipse.core.resources.IFile;
//
//import code2code.core.templateengine.TemplateEngine;
//import code2code.core.templateengine.TemplateEngineFactory;
//import code2code.utils.FileUtils;
//
//class TemplatesConfig
//{
//   private final IFile templatesFile;
//
//   private final OldGenerator generator;
//
//   private final List<Template> templates = new ArrayList<Template>();
//
//   private TemplateEngine templateEngine;
//
//   private List<OldGenerator> nestedGenerators = new ArrayList<OldGenerator>();
//
//   public TemplatesConfig(OldGenerator generator) throws Exception
//   {
//      this.generator = generator;
//
//      templatesFile = TemplateEngineFactory.findKnownTemplate(generator.getGeneratorFolder(), "templates");
//      templateEngine = TemplateEngineFactory.forFile(templatesFile);
//
//      for (Entry<String, String> entry : FileUtils.loadProperties(templatesFile.getContents()).entrySet())
//      {
//         String templateName = entry.getKey();
//         String destination = entry.getValue();
//
//         if (templateName.endsWith(".generator"))
//         {
//            nestedGenerators.add(OldGenerator.fromFolder(generator.getGeneratorFolder().getFolder(templateName)));
//         }
//         else
//         {
//            templates.add(new Template(this, templateName, destination));
//         }
//
//      }
//
//   }
//
//   public List<Template> getTemplates() throws Exception
//   {
//      return templates;
//   }
//
//   public TemplateEngine getTemplateEngine()
//   {
//      return templateEngine;
//   }
//
//   public OldGenerator getGenerator()
//   {
//      return generator;
//   }
//
//   public List<OldGenerator> getNestedGenerators()
//   {
//      return nestedGenerators;
//   }
//
//}
