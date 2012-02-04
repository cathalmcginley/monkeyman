package nl.flotsam.frontman

import decorator.haml.ScalateToHtmlDecorator
import decorator.markdown.MarkdownToHtmlDecorator
import java.io.File
import org.apache.commons.io.FilenameUtils._
import org.fusesource.scalate.{Template, TemplateEngine}

class FrontmanConfiguration(sourceDir: File, layoutDir: File) {

  private val layoutFileName = "layout"
  
  private val templateEngine =
    new TemplateEngine(List(layoutDir, sourceDir))

  private val fileSystemResourceLoader = 
    new FileSystemResourceLoader(sourceDir)

  private val layoutResolver = new LayoutResolver {
    def resolve(path: String) =
      tryLoadTemplate(new File(layoutDir, getPath(path)))
  }
  
  val resourceLoader = new DecoratingResourceLoader(fileSystemResourceLoader,
    new MarkdownToHtmlDecorator(templateEngine, layoutResolver),
    new ScalateToHtmlDecorator(templateEngine)
  )
  
  private def tryLoadTemplate(dir: File): Option[Template] = {
    val files = 
      TemplateEngine.templateTypes.view.map(ext => new File(dir, layoutFileName + "." + ext))
    files.find(_.exists()) match {
      case Some(file) =>
        Some(templateEngine.load(file))
      case None =>
        if (dir != layoutDir) tryLoadTemplate(dir.getParentFile)
        else None
    }
    
  }

}
