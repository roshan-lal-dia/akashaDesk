import {
  boolean,
  index,
  integer,
  jsonb,
  pgEnum,
  pgTable,
  text,
  timestamp,
  uniqueIndex,
  uuid,
} from "drizzle-orm/pg-core";

export const questType = pgEnum("quest_type", ["endurance", "grind", "sprint"]);
export const questStatus = pgEnum("quest_status", ["active", "paused", "completed", "failed"]);
export const participantRole = pgEnum("participant_role", ["owner", "companion"]);
export const publishJobStatus = pgEnum("publish_job_status", ["accepted", "published", "blocked", "failed"]);

export const profiles = pgTable(
  "profiles",
  {
    id: uuid("id").defaultRandom().primaryKey(),
    authUserId: text("auth_user_id").notNull(),
    displayName: text("display_name").notNull(),
    avatarUrl: text("avatar_url"),
    createdAt: timestamp("created_at", { withTimezone: true }).defaultNow().notNull(),
    updatedAt: timestamp("updated_at", { withTimezone: true }).defaultNow().notNull(),
  },
  (table) => ({
    authUserIdIdx: uniqueIndex("profiles_auth_user_id_idx").on(table.authUserId),
  }),
);

export const quests = pgTable(
  "quests",
  {
    id: uuid("id").defaultRandom().primaryKey(),
    ownerAuthUserId: text("owner_auth_user_id").notNull(),
    type: questType("type").notNull(),
    status: questStatus("status").default("active").notNull(),
    title: text("title").notNull(),
    dailyXpReward: integer("daily_xp_reward"),
    damageOnFailure: integer("damage_on_failure"),
    completionXpBounty: integer("completion_xp_bounty"),
    startsAt: timestamp("starts_at", { withTimezone: true }),
    endsAt: timestamp("ends_at", { withTimezone: true }),
    createdAt: timestamp("created_at", { withTimezone: true }).defaultNow().notNull(),
    updatedAt: timestamp("updated_at", { withTimezone: true }).defaultNow().notNull(),
  },
  (table) => ({
    ownerIdx: index("quests_owner_auth_user_id_idx").on(table.ownerAuthUserId),
    statusIdx: index("quests_status_idx").on(table.status),
  }),
);

export const questParticipants = pgTable(
  "quest_participants",
  {
    id: uuid("id").defaultRandom().primaryKey(),
    questId: uuid("quest_id")
      .references(() => quests.id, { onDelete: "cascade" })
      .notNull(),
    authUserId: text("auth_user_id").notNull(),
    role: participantRole("role").notNull(),
    currentHp: integer("current_hp").default(100).notNull(),
    currentXp: integer("current_xp").default(0).notNull(),
    streakDays: integer("streak_days").default(0).notNull(),
    joinedAt: timestamp("joined_at", { withTimezone: true }).defaultNow().notNull(),
  },
  (table) => ({
    participantUniqueIdx: uniqueIndex("quest_participants_unique_idx").on(table.questId, table.authUserId),
  }),
);

export const arenaInvites = pgTable(
  "arena_invites",
  {
    id: uuid("id").defaultRandom().primaryKey(),
    questId: uuid("quest_id")
      .references(() => quests.id, { onDelete: "cascade" })
      .notNull(),
    inviteCode: text("invite_code").notNull(),
    createdByAuthUserId: text("created_by_auth_user_id").notNull(),
    consumed: boolean("consumed").default(false).notNull(),
    expiresAt: timestamp("expires_at", { withTimezone: true }).notNull(),
    createdAt: timestamp("created_at", { withTimezone: true }).defaultNow().notNull(),
  },
  (table) => ({
    inviteCodeIdx: uniqueIndex("arena_invites_code_idx").on(table.inviteCode),
  }),
);

export const publishJobs = pgTable(
  "publish_jobs",
  {
    id: uuid("id").defaultRandom().primaryKey(),
    authUserId: text("auth_user_id").notNull(),
    status: publishJobStatus("status").default("accepted").notNull(),
    slug: text("slug").notNull(),
    title: text("title").notNull(),
    targetPath: text("target_path").notNull(),
    commitSha: text("commit_sha"),
    metadata: jsonb("metadata").$type<Record<string, unknown>>().default({}).notNull(),
    createdAt: timestamp("created_at", { withTimezone: true }).defaultNow().notNull(),
    updatedAt: timestamp("updated_at", { withTimezone: true }).defaultNow().notNull(),
  },
  (table) => ({
    authUserIdx: index("publish_jobs_auth_user_id_idx").on(table.authUserId),
    slugIdx: uniqueIndex("publish_jobs_slug_idx").on(table.slug),
  }),
);

