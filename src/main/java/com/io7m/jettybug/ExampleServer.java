/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */


package com.io7m.jettybug;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.FileSessionDataStore;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.nio.file.Paths;

/**
 * A trivial example server.
 */

public final class ExampleServer
{
  private ExampleServer()
  {

  }

  /**
   * Create a server.
   *
   * @return The running server
   *
   * @throws Exception On errors
   */

  public static Server create()
    throws Exception
  {
    final var server =
      new Server(new InetSocketAddress("localhost", 50000));

    /*
     * Configure all the servlets.
     */

    final var servlets = new ServletContextHandler();
    servlets.addServlet(ExampleServletMain.class, "/");
    servlets.addServlet(ExampleServletLogin.class, "/login");

    /*
     * Set up a session handler that allows for Servlets to have sessions
     * that can survive server restarts.
     */

    final var sessionIds = new DefaultSessionIdManager(server);
    final var sessionHandler = new SessionHandler();

    final var sessionStore = new FileSessionDataStore();
    sessionStore.setStoreDir(Paths.get("sessions").toFile());

    final var sessionCache = new DefaultSessionCache(sessionHandler);
    sessionCache.setSessionDataStore(sessionStore);

    sessionHandler.setSessionCache(sessionCache);
    sessionHandler.setSessionIdManager(sessionIds);
    sessionHandler.setHandler(servlets);

    /*
     * Set up an MBean container so that the statistics handler can export
     * statistics to JMX.
     */

    final var mbeanContainer =
      new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
    server.addBean(mbeanContainer);

    /*
     * Set up a statistics handler that wraps everything.
     */

    final var statsHandler = new StatisticsHandler();
    statsHandler.setHandler(sessionHandler);

    server.setHandler(statsHandler);
    server.start();
    return server;
  }
}
