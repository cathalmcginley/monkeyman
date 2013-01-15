/*
 * Monkeyman static web site generator
 * Copyright (C) 2012  Wilfred Springer
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package nl.flotsam.monkeyman

import decorator.less.LessDecorator
import decorator.markdown.MarkdownDecorator
import decorator.permalink.PermalinkDecorator
import decorator.scalate.ScalateDecorator
import decorator.snippet.SnippetDecorator
import decorator.yaml.YamlFrontmatterDecorator
import decorator.zuss.ZussDecorator
import java.io.File
import org.apache.commons.io.FilenameUtils._
import org.fusesource.scalate.{Binding, Template, TemplateEngine}
import org.fusesource.scalate.util.{ResourceLoader => ScalateResourceLoader}
import org.fusesource.scalate.support.URLTemplateSource

case class MonkeymanConfiguration(sourceDir: File, layoutDir: File) {

  private val layoutFileName = "layout"

  private val sourceDirectories = List(layoutDir, sourceDir)

  private val templateEngine =
    new TemplateEngine(sourceDirectories)

  private val defaultLoader = templateEngine.resourceLoader
  templateEngine.resourceLoader = new ScalateResourceLoader() {
    def resource(uri: String) =
      if (uri == "__default.scaml") Some(new URLTemplateSource(getClass.getResource("/layout.scaml")))
      else defaultLoader.resource(uri)
  }

  private val fileSystemResourceLoader =
    new FileSystemResourceLoader(sourceDir)

  private val layoutResolver = new LayoutResolver {
    def resolve(path: String) =
      tryLoadTemplate(new File(layoutDir, getPath(path)))
  }

  def dispose {
    fileSystemResourceLoader.dispose
  }

  templateEngine.importStatements = "import nl.flotsam.monkeyman.scalate.Imports._" ::
    "import org.joda.time.LocalDateTime" ::
    templateEngine.importStatements

  templateEngine.bindings = new Binding(
    name = "allResources",
    className = "Seq[nl.flotsam.monkeyman.Resource]",
    defaultValue = Some("Seq.empty[nl.flotsam.monkeyman.Resource]")
  ) :: new Binding(
    name = "title",
    className = "Option[String]",
    defaultValue = Some("None")
  ) :: new Binding(
    name = "body",
    className = "String",
    defaultValue = Some(""""No body"""")
  ) :: new Binding(
    name = "tags",
    className = "Set[String]",
    defaultValue = Some("Set.empty[String]")
  ) :: new Binding(
    name = "pubDateTime",
    className = "org.joda.time.LocalDateTime",
    defaultValue = Some("LocalDateTime.now")
  ):: new Binding(
    name = "currentPath",
    className = "String",
    defaultValue = Some(""""index.html"""")
  ):: new Binding(
    name = "Path",
    className = "nl.flotsam.monkeyman.helper.RelativePathHelper",
    defaultValue = None
  ) :: new Binding(
    name = "info",
    className = "Map[String, String]",
    defaultValue = Some("Map.empty[String, String]")
  ) :: templateEngine.bindings


  val registry =
    new Registry(
      new DecoratingResourceLoader(
        new ClasspathResourceLoader(Seq("favicon.ico", "monkeyman/logo.png", "monkeyman/monkeyman.less"), fileSystemResourceLoader),
        new LessDecorator,
        new ZussDecorator,
        new YamlFrontmatterDecorator(),
        new MarkdownDecorator(),
        new SnippetDecorator(layoutResolver, templateEngine, allResources _),
        new ScalateDecorator(templateEngine, allResources _),
        PermalinkDecorator
      )
    )

  def allResources: List[Resource] = registry.allResources
  
  private def tryLoadTemplate(dir: File): Template = {
    val files =
      TemplateEngine.templateTypes.view.map(ext => new File(dir, layoutFileName + "." + ext))
    files.find(_.exists()) match {
      case Some(file) => templateEngine.load(file)
      case None =>
        if (dir != layoutDir) tryLoadTemplate(dir.getParentFile)
        else {
          templateEngine.load("__default.scaml")
        }
    }

  }

}
