package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO} TEXT NOT NULL DEFAULT ""
        );
        """.trimIndent()

        val DDLDraft = """
        CREATE TABLE ${DraftPostColumns.TABLE} (
            ${DraftPostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${DraftPostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${DraftPostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${DraftPostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${DraftPostColumns.COLUMN_VIDEO} TEXT NOT NULL DEFAULT ""
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_LIKES = "likesCount"
        const val COLUMN_SHARES = "sharesCount"
        const val COLUMN_VIDEO = "video"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKED_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARES,
            COLUMN_VIDEO
        )
    }

    object DraftPostColumns {
        const val TABLE = "draft"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_VIDEO = "video"
        val ALL_DRAFT_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            put(PostColumns.COLUMN_AUTHOR, "")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_PUBLISHED, "")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likesCount = likesCount + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               sharesCount = sharesCount + 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun saveDraft(post: Post) {
        val values = ContentValues().apply {
            put(DraftPostColumns.COLUMN_AUTHOR, "")
            put(DraftPostColumns.COLUMN_CONTENT, post.content)
            put(DraftPostColumns.COLUMN_PUBLISHED, "")
        }
        db.insert(DraftPostColumns.TABLE, null, values)
    }

    override fun getDraft(): Post? {
        db.query(
            DraftPostColumns.TABLE,
            DraftPostColumns.ALL_DRAFT_COLUMNS,
            null,
            null,
            null,
            null,
            null,
        ).use {
            if(it.count > 0){
                it.moveToNext()
                return mapDraft(it)
            } else return null
        }
    }

    override fun deleteDraft() {
        db.delete(
            DraftPostColumns.TABLE,
            null,
            null)

    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                sharesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARES)),
                video = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO))
            )
        }
    }

    private fun mapDraft(cursor: Cursor): Post? {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(DraftPostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(DraftPostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(DraftPostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(DraftPostColumns.COLUMN_PUBLISHED)),
                likedByMe = false,
                likesCount = 0,
                sharesCount = 0,
                video = getString(getColumnIndexOrThrow(DraftPostColumns.COLUMN_VIDEO))
            )
        }
    }
}
