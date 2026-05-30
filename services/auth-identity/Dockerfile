FROM oven/bun:1.3.14-alpine

WORKDIR /app

COPY package.json tsconfig.json ./
RUN bun install --production

COPY scripts ./scripts
COPY src ./src

ENV PORT=3000
EXPOSE 3000

CMD ["bun", "run", "src/index.ts"]

