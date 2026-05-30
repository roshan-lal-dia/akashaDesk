package desk.akasha.obsidian

import java.io.File

actual fun createLocalMarkdownVault(rootPath: String): MarkdownVault {
    return DesktopMarkdownVault(File(rootPath))
}

private class DesktopMarkdownVault(
    private val root: File,
) : MarkdownVault {
    override suspend fun listNotes(): List<VaultNote> {
        if (!root.exists()) {
            return emptyList()
        }
        return root.walkTopDown()
            .filter { it.isFile && it.extension.equals("md", ignoreCase = true) }
            .mapNotNull { file -> readFile(file) }
            .toList()
    }

    override suspend fun readNote(relativePath: String): VaultNote? {
        val target = resolveInsideRoot(relativePath)
        return if (target.isFile) readFile(target) else null
    }

    override suspend fun saveNote(relativePath: String, markdown: String): VaultNote {
        val target = resolveInsideRoot(relativePath)
        require(target.extension.equals("md", ignoreCase = true)) {
            "Only markdown files can be saved through the Obsidian bridge."
        }
        target.parentFile?.mkdirs()
        target.writeText(markdown)
        return readFile(target) ?: error("Saved note could not be read back.")
    }

    private fun readFile(file: File): VaultNote? {
        val relative = file.relativeToOrNull(root)?.invariantSeparatorsPath ?: return null
        return VaultNote(
            relativePath = relative,
            title = file.nameWithoutExtension,
            markdown = file.readText(),
        )
    }

    private fun resolveInsideRoot(relativePath: String): File {
        val rootPath = root.canonicalFile.toPath()
        val targetPath = rootPath.resolve(relativePath).normalize().toFile().canonicalFile.toPath()
        require(targetPath.startsWith(rootPath)) {
            "Path escapes the configured markdown vault."
        }
        return targetPath.toFile()
    }
}
