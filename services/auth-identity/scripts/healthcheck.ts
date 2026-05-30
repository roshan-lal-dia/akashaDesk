const port = Bun.env.PORT ?? "3000";
const response = await fetch(`http://127.0.0.1:${port}/healthz`);

if (!response.ok) {
  throw new Error(`auth healthcheck failed with ${response.status}`);
}

export {};
