package com.survivalwiki.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.survivalwiki.data.entity.Article;
import com.survivalwiki.data.entity.Category;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ArticleDao_Impl implements ArticleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Category> __insertionAdapterOfCategory;

  private final EntityInsertionAdapter<Article> __insertionAdapterOfArticle;

  private final SharedSQLiteStatement __preparedStmtOfToggleBookmark;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLastReadTime;

  private final SharedSQLiteStatement __preparedStmtOfClearHistory;

  public ArticleDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategory = new EntityInsertionAdapter<Category>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `categories` (`id`,`title`,`description`,`iconResName`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Category entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        if (entity.getDescription() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescription());
        }
        statement.bindString(4, entity.getIconResName());
      }
    };
    this.__insertionAdapterOfArticle = new EntityInsertionAdapter<Article>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `articles` (`id`,`categoryId`,`title`,`content`,`tags`,`iconResName`,`lastReadTimestamp`,`level`,`readTimeMin`,`lastUpdated`,`isBookmarked`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Article entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCategoryId());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getContent());
        statement.bindString(5, entity.getTags());
        statement.bindString(6, entity.getIconResName());
        statement.bindLong(7, entity.getLastReadTimestamp());
        if (entity.getLevel() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLevel());
        }
        statement.bindLong(9, entity.getReadTimeMin());
        if (entity.getLastUpdated() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastUpdated());
        }
        final int _tmp = entity.isBookmarked() ? 1 : 0;
        statement.bindLong(11, _tmp);
      }
    };
    this.__preparedStmtOfToggleBookmark = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE articles SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateLastReadTime = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE articles SET lastReadTimestamp = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearHistory = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE articles SET lastReadTimestamp = 0";
        return _query;
      }
    };
  }

  @Override
  public Object insertCategories(final List<Category> categories,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCategory.insert(categories);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertArticles(final List<Article> articles,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfArticle.insert(articles);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object toggleBookmark(final int articleId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfToggleBookmark.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, articleId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfToggleBookmark.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLastReadTime(final int articleId, final long timestamp,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLastReadTime.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, timestamp);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, articleId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateLastReadTime.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object clearHistory(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearHistory.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearHistory.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Article>> getAllArticles() {
    final String _sql = "SELECT * FROM articles";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"articles"}, new Callable<List<Article>>() {
      @Override
      @NonNull
      public List<Article> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final List<Article> _result = new ArrayList<Article>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Article _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            final long _tmpLastReadTimestamp;
            _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
            final String _tmpLevel;
            if (_cursor.isNull(_cursorIndexOfLevel)) {
              _tmpLevel = null;
            } else {
              _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            }
            final int _tmpReadTimeMin;
            _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp != 0;
            _item = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Article> getIntroductionArticle() {
    final String _sql = "SELECT * FROM articles WHERE title LIKE 'Введение%' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"articles"}, new Callable<Article>() {
      @Override
      @Nullable
      public Article call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final Article _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            final long _tmpLastReadTimestamp;
            _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
            final String _tmpLevel;
            if (_cursor.isNull(_cursorIndexOfLevel)) {
              _tmpLevel = null;
            } else {
              _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            }
            final int _tmpReadTimeMin;
            _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp != 0;
            _result = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Category>> getAllCategories() {
    final String _sql = "SELECT * FROM categories";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categories"}, new Callable<List<Category>>() {
      @Override
      @NonNull
      public List<Category> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final List<Category> _result = new ArrayList<Category>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Category _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            _item = new Category(_tmpId,_tmpTitle,_tmpDescription,_tmpIconResName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Article>> getArticlesByCategory(final int categoryId) {
    final String _sql = "SELECT * FROM articles WHERE categoryId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, categoryId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"articles"}, new Callable<List<Article>>() {
      @Override
      @NonNull
      public List<Article> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final List<Article> _result = new ArrayList<Article>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Article _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            final long _tmpLastReadTimestamp;
            _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
            final String _tmpLevel;
            if (_cursor.isNull(_cursorIndexOfLevel)) {
              _tmpLevel = null;
            } else {
              _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            }
            final int _tmpReadTimeMin;
            _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp != 0;
            _item = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Article> getArticleById(final int id) {
    final String _sql = "SELECT * FROM articles WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"articles"}, new Callable<Article>() {
      @Override
      @Nullable
      public Article call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final Article _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            final long _tmpLastReadTimestamp;
            _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
            final String _tmpLevel;
            if (_cursor.isNull(_cursorIndexOfLevel)) {
              _tmpLevel = null;
            } else {
              _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            }
            final int _tmpReadTimeMin;
            _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp != 0;
            _result = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Article>> getRecentlyReadArticles() {
    final String _sql = "SELECT * FROM articles WHERE lastReadTimestamp > 0 ORDER BY lastReadTimestamp DESC LIMIT 10";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"articles"}, new Callable<List<Article>>() {
      @Override
      @NonNull
      public List<Article> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final List<Article> _result = new ArrayList<Article>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Article _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            final long _tmpLastReadTimestamp;
            _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
            final String _tmpLevel;
            if (_cursor.isNull(_cursorIndexOfLevel)) {
              _tmpLevel = null;
            } else {
              _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            }
            final int _tmpReadTimeMin;
            _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp != 0;
            _item = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Article>> getBookmarkedArticles() {
    final String _sql = "SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY title ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"articles"}, new Callable<List<Article>>() {
      @Override
      @NonNull
      public List<Article> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
          final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
          final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
          final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
          final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
          final List<Article> _result = new ArrayList<Article>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Article _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpTags;
            _tmpTags = _cursor.getString(_cursorIndexOfTags);
            final String _tmpIconResName;
            _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
            final long _tmpLastReadTimestamp;
            _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
            final String _tmpLevel;
            if (_cursor.isNull(_cursorIndexOfLevel)) {
              _tmpLevel = null;
            } else {
              _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            }
            final int _tmpReadTimeMin;
            _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
            final String _tmpLastUpdated;
            if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
              _tmpLastUpdated = null;
            } else {
              _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
            }
            final boolean _tmpIsBookmarked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
            _tmpIsBookmarked = _tmp != 0;
            _item = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Article>> searchArticles(final String query) {
    final String _sql = "\n"
            + "        SELECT articles.* FROM articles \n"
            + "        JOIN articles_fts ON articles.id = articles_fts.docid \n"
            + "        WHERE articles_fts MATCH ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"articles",
        "articles_fts"}, new Callable<List<Article>>() {
      @Override
      @NonNull
      public List<Article> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
            final int _cursorIndexOfTags = CursorUtil.getColumnIndexOrThrow(_cursor, "tags");
            final int _cursorIndexOfIconResName = CursorUtil.getColumnIndexOrThrow(_cursor, "iconResName");
            final int _cursorIndexOfLastReadTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadTimestamp");
            final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
            final int _cursorIndexOfReadTimeMin = CursorUtil.getColumnIndexOrThrow(_cursor, "readTimeMin");
            final int _cursorIndexOfLastUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "lastUpdated");
            final int _cursorIndexOfIsBookmarked = CursorUtil.getColumnIndexOrThrow(_cursor, "isBookmarked");
            final List<Article> _result = new ArrayList<Article>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final Article _item;
              final int _tmpId;
              _tmpId = _cursor.getInt(_cursorIndexOfId);
              final int _tmpCategoryId;
              _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpContent;
              _tmpContent = _cursor.getString(_cursorIndexOfContent);
              final String _tmpTags;
              _tmpTags = _cursor.getString(_cursorIndexOfTags);
              final String _tmpIconResName;
              _tmpIconResName = _cursor.getString(_cursorIndexOfIconResName);
              final long _tmpLastReadTimestamp;
              _tmpLastReadTimestamp = _cursor.getLong(_cursorIndexOfLastReadTimestamp);
              final String _tmpLevel;
              if (_cursor.isNull(_cursorIndexOfLevel)) {
                _tmpLevel = null;
              } else {
                _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
              }
              final int _tmpReadTimeMin;
              _tmpReadTimeMin = _cursor.getInt(_cursorIndexOfReadTimeMin);
              final String _tmpLastUpdated;
              if (_cursor.isNull(_cursorIndexOfLastUpdated)) {
                _tmpLastUpdated = null;
              } else {
                _tmpLastUpdated = _cursor.getString(_cursorIndexOfLastUpdated);
              }
              final boolean _tmpIsBookmarked;
              final int _tmp;
              _tmp = _cursor.getInt(_cursorIndexOfIsBookmarked);
              _tmpIsBookmarked = _tmp != 0;
              _item = new Article(_tmpId,_tmpCategoryId,_tmpTitle,_tmpContent,_tmpTags,_tmpIconResName,_tmpLastReadTimestamp,_tmpLevel,_tmpReadTimeMin,_tmpLastUpdated,_tmpIsBookmarked);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
