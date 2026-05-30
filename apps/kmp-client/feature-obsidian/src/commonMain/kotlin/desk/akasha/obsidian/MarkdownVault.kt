package desk.akasha.obsidian

data class VaultNote(
    val relativePath: String,
    val title: String,
    val markdown: String,
)

interface MarkdownVault {
    suspend fun listNotes(): List<VaultNote>
    suspend fun readNote(relativePath: String): VaultNote?
    suspend fun saveNote(relativePath: String, markdown: String): VaultNote
}

expect fun createLocalMarkdownVault(rootPath: String): MarkdownVault

