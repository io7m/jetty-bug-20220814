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

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

/**
 * A main servlet that indicates if the user is logged in.
 */

public final class ExampleServletMain extends HttpServlet
{
  /**
   * A main servlet that indicates if the user is logged in.
   */

  public ExampleServletMain()
  {

  }

  @Override
  protected void service(
    final HttpServletRequest request,
    final HttpServletResponse response)
    throws IOException
  {
    final var session = request.getSession(false);
    if (session != null) {
      final var userId = (UUID) session.getAttribute("UserID");
      response.setStatus(200);
      response.setContentType("text/plain");
      try (var output = response.getOutputStream()) {
        output.print("You are: %s".formatted(userId));
        output.println();
        output.flush();
      }
      return;
    }

    response.setStatus(401);
    response.setContentType("text/plain");
    try (var output = response.getOutputStream()) {
      output.print("Use /login to log in.");
      output.println();
      output.flush();
    }
  }
}
