package com.khannedy.scala.spring

import java.util.logging.Logger
import org.eclipse.jetty.server.{Handler, Server}
import org.eclipse.jetty.server.handler.HandlerCollection
import org.eclipse.jetty.servlet.{ServletHolder, ServletContextHandler}
import org.springframework.web.context.ContextLoaderListener
import org.springframework.web.servlet.DispatcherServlet
import io.dropwizard.servlets.assets.AssetServlet
import com.google.common.base.Charsets

/**
 * @author Eko Khannedy
 */
object AppRun extends App {

  new AppRun

}

class AppRun {

  val log = Logger.getLogger(AppRun.getClass.getName)

  log.info("start application")

  val server: Server = new Server(8080)
  server.setStopAtShutdown(true)
  server.setDumpAfterStart(true)
  server.setDumpBeforeStop(true)

  var handlerCollection = new HandlerCollection()
  handlerCollection.addHandler(createServletHandler())
  server.setHandler(handlerCollection)

  server.start()

  server.join()

  def createServletHandler(): Handler = {
    val handler = new ServletContextHandler()

    handler.setInitParameter("contextConfigLocation",
      "classpath:app-context.xml")
    handler.addEventListener(new ContextLoaderListener())

    val holder = new ServletHolder(new DispatcherServlet())
    holder.setInitParameter("contextConfigLocation", "")
    holder.setInitOrder(1)
    handler.addServlet(holder, "/*")

    val asset = new AssetServlet("/assets", "/assets", "index.html", Charsets.UTF_8)
    handler.addServlet(new ServletHolder(asset), "/assets/*")

    handler
  }

}
