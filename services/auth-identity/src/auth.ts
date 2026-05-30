import { betterAuth } from "better-auth";
import { Pool } from "pg";

const databaseUrl = Bun.env.DATABASE_URL;

if (!databaseUrl) {
  throw new Error("DATABASE_URL is required for BetterAuth.");
}

const github =
  Bun.env.GITHUB_CLIENT_ID && Bun.env.GITHUB_CLIENT_SECRET
    ? {
        clientId: Bun.env.GITHUB_CLIENT_ID,
        clientSecret: Bun.env.GITHUB_CLIENT_SECRET,
      }
    : undefined;

const google =
  Bun.env.GOOGLE_CLIENT_ID && Bun.env.GOOGLE_CLIENT_SECRET
    ? {
        clientId: Bun.env.GOOGLE_CLIENT_ID,
        clientSecret: Bun.env.GOOGLE_CLIENT_SECRET,
      }
    : undefined;

export const auth = betterAuth({
  appName: "AkashaDesk",
  baseURL: Bun.env.BETTER_AUTH_URL,
  database: new Pool({ connectionString: databaseUrl }),
  secret: Bun.env.BETTER_AUTH_SECRET,
  trustedOrigins: trustedOrigins(),
  socialProviders: {
    ...(github ? { github } : {}),
    ...(google ? { google } : {}),
  },
});

function trustedOrigins(): string[] {
  return (Bun.env.BETTER_AUTH_TRUSTED_ORIGINS ?? "")
    .split(",")
    .map((origin) => origin.trim())
    .filter(Boolean);
}

