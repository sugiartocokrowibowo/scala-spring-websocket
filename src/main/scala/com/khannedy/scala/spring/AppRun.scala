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

  val server: Server = createServer(8080)
  server.setHandler(createHandlerCollection(createServletHandler()))
  server.start()
  server.join()

  def createServer(port: Int): Server = {
    val server: Server = new Server(port)
    server.setStopAtShutdown(true)
    server.setDumpAfterStart(true)
    server.setDumpBeforeStop(true)
    server
  }

  def createHandlerCollection(handler: Handler): HandlerCollection = {
    val handlerCollection = new HandlerCollection()
    handlerCollection.addHandler(handler)
    handlerCollection
  }

  def createServletHandler(): Handler = {
    val handler = new ServletContextHandler()

    handler.setInitParameter("contextConfigLocation", "classpath:app-context.xml")
    handler.addEventListener(new ContextLoaderListener())

    handler.addServlet(createSpringServlet(), "/*")
    handler.addServlet(createAssetServlet(), "/assets/*")

    handler
  }

  def createSpringServlet(): ServletHolder = {
    val holder = new ServletHolder(new DispatcherServlet())
    holder.setInitParameter("contextConfigLocation", "")
    holder.setInitOrder(1)
    holder
  }

  def createAssetServlet(): ServletHolder = {
    val asset = new AssetServlet("/assets", "/assets", "index.html", Charsets.UTF_8)
    new ServletHolder(asset)
  }

}
