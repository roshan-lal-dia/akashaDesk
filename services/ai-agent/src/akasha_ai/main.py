from __future__ import annotations

import hashlib
import os
import re
from datetime import datetime, timezone
from enum import StrEnum

from fastapi import FastAPI, Header, HTTPException, status
from pydantic import BaseModel, Field

app = FastAPI(title="AkashaDesk AI Agent", version="0.1.0")


class PublishStatus(StrEnum):
    ACCEPTED = "ACCEPTED"
    PUBLISHED = "PUBLISHED"
    BLOCKED = "BLOCKED"


class PublishRequest(BaseModel):
    title: str | None = None
    markdown: str = Field(min_length=1)
    tags: list[str] = Field(default_factory=list)


class PublishResult(BaseModel):
    status: PublishStatus
    slug: str
    seoTitle: str
    description: str
    targetPath: str
    commitSha: str | None = None


@app.get("/healthz")
def healthz() -> dict[str, str]:
    return {"status": "ok", "service": "ai-agent"}


@app.post("/internal/publish")
def prepare_publish(
    request: PublishRequest,
    x_internal_token: str | None = Header(default=None),
) -> PublishResult:
    expected_token = os.getenv("AI_AGENT_INTERNAL_TOKEN")
    if expected_token and x_internal_token != expected_token:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid internal service token.",
        )

    sanitized = sanitize_markdown(request.markdown)
    title = request.title or infer_title(sanitized)
    slug = slugify(title, sanitized)
    description = summarize(sanitized)
    content_path = os.getenv("ASTRO_CONTENT_PATH", "src/content/blog").strip("/")

    return PublishResult(
        status=PublishStatus.ACCEPTED,
        slug=slug,
        seoTitle=title[:70],
        description=description,
        targetPath=f"{content_path}/{slug}.md",
        commitSha=None,
    )


def sanitize_markdown(markdown: str) -> str:
    without_scripts = re.sub(
        r"<script[\s\S]*?</script>",
        "",
        markdown,
        flags=re.IGNORECASE,
    )
    return without_scripts.strip()


def infer_title(markdown: str) -> str:
    for line in markdown.splitlines():
        stripped = line.strip()
        if stripped.startswith("#"):
            heading = stripped.lstrip("#").strip()
            if heading:
                return heading
    return f"AkashaDesk Draft {datetime.now(timezone.utc).strftime('%Y%m%d%H%M')}"


def slugify(title: str, markdown: str) -> str:
    normalized = re.sub(r"[^a-z0-9]+", "-", title.lower()).strip("-")
    digest = hashlib.sha256(markdown.encode("utf-8")).hexdigest()[:8]
    return f"{normalized or 'draft'}-{digest}"


def summarize(markdown: str) -> str:
    plain = re.sub(r"[#*_>`\[\]()]", " ", markdown)
    plain = re.sub(r"\s+", " ", plain).strip()
    return plain[:156] or "AkashaDesk markdown draft ready for publication."

