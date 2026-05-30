import { auth } from "./auth";

const port = Number(Bun.env.PORT ?? "3000");

Bun.serve({
  port,
  async fetch(request) {
    const url = new URL(request.url);

    if (url.pathname === "/healthz") {
      return Response.json({ status: "ok", service: "auth-identity" });
    }

    if (url.pathname.startsWith("/api/auth")) {
      return auth.handler(request);
    }

    return Response.json({ error: "not_found" }, { status: 404 });
  },
});

console.log(`auth-identity listening on ${port}`);

